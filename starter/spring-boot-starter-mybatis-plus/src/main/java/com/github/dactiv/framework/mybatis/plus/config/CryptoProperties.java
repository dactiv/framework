package com.github.dactiv.framework.mybatis.plus.config;


import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 加解密配置
 *
 * @author maurice.chen
 *
 */
@ConfigurationProperties("dactiv.mybatis.plus.crypto")
public class CryptoProperties {

    public static final String MYBATIS_PLUS_DATA_AES_CRYPTO_SERVICE_NAME = "mybatisPlusDataAesCryptoService";

    public static final String MYBATIS_PLUS_DATA_RSA_CRYPTO_SERVICE_NAME = "mybatisPlusDataRsaCryptoService";

    /**
     * aes 加密 key
     */
    private String dataAesCryptoKey;

    /**
     * rsa 加密 key
     */
    private String dataRasCryptoPublicKey;

    /**
     * rsa 解密 key
     */
    private String dataRasCryptoPrivateKey;

    public CryptoProperties() {

    }

    public String getDataAesCryptoKey() {
        return dataAesCryptoKey;
    }

    public void setDataAesCryptoKey(String dataAesCryptoKey) {
        this.dataAesCryptoKey = dataAesCryptoKey;
    }

    public String getDataRasCryptoPublicKey() {
        return dataRasCryptoPublicKey;
    }

    public void setDataRasCryptoPublicKey(String dataRasCryptoPublicKey) {
        this.dataRasCryptoPublicKey = dataRasCryptoPublicKey;
    }

    public String getDataRasCryptoPrivateKey() {
        return dataRasCryptoPrivateKey;
    }

    public void setDataRasCryptoPrivateKey(String dataRasCryptoPrivateKey) {
        this.dataRasCryptoPrivateKey = dataRasCryptoPrivateKey;
    }
}
