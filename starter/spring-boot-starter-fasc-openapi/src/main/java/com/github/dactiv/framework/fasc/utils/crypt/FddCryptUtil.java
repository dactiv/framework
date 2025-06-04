package com.github.dactiv.framework.fasc.utils.crypt;

import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.crypto.algorithm.SimpleByteSource;
import com.github.dactiv.framework.fasc.utils.string.StringUtil;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author Fadada
 * 2021/9/8 16:09:38
 */
public class FddCryptUtil {


    private FddCryptUtil() {
    }

    private static final Charset UTF8 = StandardCharsets.UTF_8;

    public static byte[] hmac256(byte[] key, String msg) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, mac.getAlgorithm());
        mac.init(secretKeySpec);
        return mac.doFinal(msg.getBytes(UTF8));
    }

    public static String sha256Hex(String s) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] d = md.digest(s.getBytes(UTF8));
        return new SimpleByteSource(d).getHex();
    }

    /**
     * @param sortParam 排序后得参数字符串
     * @param timestamp 时间戳
     * @param appSecret 应用秘钥
     * @return 签名值
     * @throws Exception 异常
     */
    public static String sign(String sortParam,
                              String timestamp,
                              String appSecret) throws Exception {
        //将排序后字符串转为sha256Hex
        String signText = sha256Hex(sortParam);
        // ************* 计算签名 *************
        byte[] secretSigning = hmac256((appSecret).getBytes(UTF8), timestamp);
        //计算后得到签名
        return new SimpleByteSource(hmac256(secretSigning, signText)).getHex();
    }


    public static String sortParameters(Map<String, String> parameters) {
        if (parameters.isEmpty()) {
            return null;
        }
        List<String> removeKeys = new ArrayList<>();
        for (Entry<String, String> entry : parameters.entrySet()) {
            if (StringUtil.isBlank(entry.getValue())) {
                removeKeys.add(entry.getKey());
            }
        }

        for (String key : removeKeys) {
            parameters.remove(key);
        }
        StringBuilder stringBuilder = new StringBuilder();
        SortedMap<String, String> paramMap = new TreeMap<>(parameters);
        int index = 0;
        for (Entry<String, String> entry : paramMap.entrySet()) {
            stringBuilder.append(entry.getKey()).append(Casts.EQ).append(entry.getValue());
            index++;
            if (index != parameters.size()) {
                stringBuilder.append(Casts.HTTP_AND);
            }
        }
        return stringBuilder.toString();
    }

}

