package com.github.dactiv.framework.citic.service;

import com.citicbank.baselib.crypto.protocol.PKCS7Signature;
import com.citicbank.baselib.crypto.util.CryptUtil;
import com.citicbank.baselib.crypto.util.FileUtil;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dactiv.framework.citic.config.CiticProperties;
import com.github.dactiv.framework.citic.domain.CiticApiResult;
import com.github.dactiv.framework.citic.domain.body.request.BankCardRequestBody;
import com.github.dactiv.framework.citic.domain.body.request.SearchUserStatusRequestBody;
import com.github.dactiv.framework.citic.domain.body.request.UserRegistrationRequestBody;
import com.github.dactiv.framework.citic.domain.body.response.SearchUserStatusResponseBody;
import com.github.dactiv.framework.citic.domain.body.response.UserRegistrationResponseBody;
import com.github.dactiv.framework.citic.domain.body.response.VoidResponseBody;
import com.github.dactiv.framework.citic.domain.metadata.BasicRequestMetadata;
import com.github.dactiv.framework.citic.domain.metadata.BasicResponseMetadata;
import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.commons.exception.ErrorCodeException;
import com.github.dactiv.framework.commons.exception.SystemException;
import com.github.dactiv.framework.crypto.algorithm.Base64;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.*;

public class CiticService {

    public static final Logger LOGGER = LoggerFactory.getLogger(CiticService.class);

    private final RestTemplate restTemplate;

    private final CiticProperties citicConfig;

    private final MappingJackson2XmlHttpMessageConverter mappingJackson2XmlHttpMessageConverter;

    public CiticService(RestTemplate restTemplate, CiticProperties citicConfig, MappingJackson2XmlHttpMessageConverter mappingJackson2XmlHttpMessageConverter) {
        this.restTemplate = restTemplate;
        this.citicConfig = citicConfig;
        this.mappingJackson2XmlHttpMessageConverter = mappingJackson2XmlHttpMessageConverter;
    }

    public UserRegistrationResponseBody userRegistration(UserRegistrationRequestBody body) {
        body.setTransCode("21000001");
        return executeApi(body, UserRegistrationResponseBody.class);
    }

    public SearchUserStatusResponseBody searchUserStatus(SearchUserStatusRequestBody body) {
        body.setTransCode("22000001");
        return executeApi(body, SearchUserStatusResponseBody.class);
    }

    public VoidResponseBody bankCard(BankCardRequestBody body) {
        body.setTransCode("21000024");
        return executeApi(body, VoidResponseBody.class);
    }

    public <T extends BasicRequestMetadata, R extends BasicResponseMetadata> R executeApi(T body, Class<R> responseType) {
        body.setMerchantId(citicConfig.getMerchantId());
        body.setReqSn(citicConfig.getMerchantId() + BasicRequestMetadata.getRequestSsnValue() + RandomStringUtils.secure().nextAlphanumeric(citicConfig.getRandomRequestSsnNumber()));
        sign(body);

        HttpHeaders headers = getHttpHeaders(body);

        HttpEntity<T> entity = new HttpEntity<>(body, headers);
        ObjectMapper objectMapper = mappingJackson2XmlHttpMessageConverter.getObjectMapper();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(
                    "[中信银行 E 管家]:开始请求 {} 接口, 请求体为:{}",
                    body.getTransCode(),
                    SystemException.convertSupplier(() -> objectMapper.writeValueAsString(body))
            );
        }

        long startTime = System.currentTimeMillis();
        ResponseEntity<byte[]> response = restTemplate.exchange(citicConfig.getBaseUrl(), HttpMethod.POST, entity, new ParameterizedTypeReference<>() {});
        long endTime = System.currentTimeMillis();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(
                    "[中信银行 E 管家]:请求 {} 接口结束, 用时:{} 毫秒, 响应结果为:{}",
                    body.getTransCode(), (endTime - startTime),
                    SystemException.convertSupplier(() -> new String(Objects.requireNonNull(response.getBody()), StandardCharsets.UTF_8))
            );
        }
        JavaType type = objectMapper.getTypeFactory().constructParametricType(CiticApiResult.class, responseType);
        CiticApiResult<R> result = SystemException.convertSupplier(() -> objectMapper.readValue(response.getBody(), type));

        R data =  result.getData();
        SystemException.isTrue(StringUtils.isNotEmpty(data.getSign()), "[中信银行 E 管家]:" + Objects.toString(data.getMessage(), result.getMessage()));

        Boolean verifyResult = SystemException.convertSupplier(
                () -> verifySign(data),
                (e) -> new SystemException("[中信银行 E 管家]:验签出现异常", e)
        );
        SystemException.isTrue(Objects.nonNull(verifyResult) && verifyResult, "[中信银行 E 管家]:响应数据验签不通过");

        SystemException.isTrue(citicConfig.getApiSuccessCodeValue().equals(data.getCode()), () -> new ErrorCodeException("[中信银行 E 管家]: 执行接口 [" + body.getTransCode() + "] 错误, " + data.getMessage(), data.getCode()));

        return data;
    }

    private <T extends BasicRequestMetadata> HttpHeaders getHttpHeaders(T body) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(BasicRequestMetadata.MERCHANT_NO_HEADER_NAME, body.getMerchantId());
        headers.add(BasicRequestMetadata.TRAN_CODE_HEADER_NAME, body.getTransCode());
        headers.add(BasicRequestMetadata.SERIAL_NO_HEADER_NAME, BasicRequestMetadata.getTransTimestampValue() + body.getTransCode());
        headers.add(BasicRequestMetadata.TRAN_TIMESTAMP_HEADER_NAME, BasicRequestMetadata.getTransTimestampValue());
        headers.add(BasicRequestMetadata.VERSION_HEADER_NAME, citicConfig.getApiVersionValue());

        headers.setContentType(new MediaType(MediaType.APPLICATION_XML, StandardCharsets.UTF_8));
        return headers;
    }

    public boolean verifySign(BasicResponseMetadata body) throws Exception {
        String sign = body.getSign();
        String signString = getSignString(body);
        return verifySign(signString.getBytes(StandardCharsets.UTF_8), sign);
    }

    private void sign(BasicRequestMetadata body) {
        String signString = getSignString(body);
        String sign = SystemException.convertSupplier(() ->
                sign(
                        signString.getBytes(StandardCharsets.UTF_8),
                        citicConfig.getSignPassword(),
                        citicConfig.getPrivateKeyPath(),
                        citicConfig.getCertFilePath()
                )
        );
        body.setSign(sign);
    }

    private String getSignString(Object o) {
        Map<String, Object> map = Casts.convertValue(o, Casts.MAP_TYPE_REFERENCE);
        List<String> values = new LinkedList<>();
        map
                .entrySet()
                .stream()
                .filter(e -> !citicConfig.getIgnoreSignProperties().contains(e.getKey()))
                .forEach(e -> values.add(e.getValue().toString()));

        StringBuilder signString = new StringBuilder();

        Collections.sort(values);
        values.forEach(signString::append);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(
                    "[中信银行 E 管家]:对 {} 数据进行排序，加签值为:{}",
                    SystemException.convertSupplier(() -> Casts.getObjectMapper().writeValueAsString(map)),
                    signString
            );
        }

        return signString.toString();
    }

    /**
     * 加签
     *
     * @param plaintext 明文报文 byte
     * @param password 发送方私钥文件密码
     * @param secretFile 发送方私钥文件名
     * @param certFile 发送方 cer证书文件名
     */
    private String sign(byte[] plaintext, String password, String secretFile, String certFile) throws Exception {
        char[] keyPassword = password.toCharArray();

        byte[] privateKey = FileUtil.read4file(secretFile);
        PrivateKey signerPrivatekey = CryptUtil.decryptPrivateKey(Base64.decode(privateKey), keyPassword);

        byte[] encodedCert = FileUtil.read4file(certFile);
        X509Certificate signerCertificate = CryptUtil.generateX509Certificate(Base64.decode(encodedCert));

        byte[] signature = PKCS7Signature.sign(plaintext, signerPrivatekey, signerCertificate, null, false);

        return new String(Base64.encode(signature));
    }

    /***
     * 验签
     * @param msg 待验签数据
     * @param sign 发送方生成的签名信息
     * @return 验签结果
     */
    public Boolean verifySign(byte[] msg, String sign) throws Exception {
        byte[] base64EncodedSenderCert = FileUtil.read4file(citicConfig.getPublicKeyPath());

        X509Certificate signerCertificate = CryptUtil.generateX509Certificate(Base64.decode(base64EncodedSenderCert));
        if (Objects.isNull(signerCertificate)) {
            return Boolean.FALSE;
        }
        PublicKey senderPubKey = signerCertificate.getPublicKey();
        return PKCS7Signature.verifyDetachedSignature(msg, Base64.decode(sign.getBytes()), senderPubKey);
    }

}
