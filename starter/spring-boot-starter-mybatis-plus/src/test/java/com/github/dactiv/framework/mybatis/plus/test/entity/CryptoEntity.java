package com.github.dactiv.framework.mybatis.plus.test.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.github.dactiv.framework.commons.id.BasicIdentification;
import com.github.dactiv.framework.mybatis.plus.annotation.Decryption;
import com.github.dactiv.framework.mybatis.plus.annotation.Encryption;
import com.github.dactiv.framework.mybatis.plus.crypto.DataAesCryptoService;

@TableName(value = "tb_crypto_entity", autoResultMap = true)
public class CryptoEntity implements BasicIdentification<Integer> {

    private Integer id;

    @Decryption(serviceClass = DataAesCryptoService.class, beanName = "aesEcbCryptoService")
    @Encryption(serviceClass = DataAesCryptoService.class, beanName = "aesEcbCryptoService")
    private String cryptoValue;

    public CryptoEntity() {
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getCryptoValue() {
        return cryptoValue;
    }

    public void setCryptoValue(String cryptoValue) {
        this.cryptoValue = cryptoValue;
    }
}
