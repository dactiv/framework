package com.github.dactiv.framework.mybatis.plus.crypto;

import com.github.dactiv.framework.crypto.CipherAlgorithmService;
import com.github.dactiv.framework.crypto.algorithm.Base64;
import com.github.dactiv.framework.crypto.algorithm.ByteSource;
import com.github.dactiv.framework.crypto.algorithm.CodecUtils;
import com.github.dactiv.framework.crypto.algorithm.SimpleByteSource;
import com.github.dactiv.framework.crypto.algorithm.cipher.AesCipherService;
import com.github.dactiv.framework.mybatis.plus.DecryptService;
import com.github.dactiv.framework.mybatis.plus.EncryptService;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * aes 的数据加解密服务实现
 *
 * @author maurice.chen
 */
public class DataAesCryptoService implements DecryptService, EncryptService {

    private final AesCipherService aesCipherService;

    private final String key;

    public DataAesCryptoService(AesCipherService aesCipherService, String key) {
        this.aesCipherService = aesCipherService;
        this.key = key;
    }

    public static ByteSource generate16ByteKey(String key) {
        byte[] finalKey = new byte[16];
        int i = 0;

        for (byte b : key.getBytes(StandardCharsets.UTF_8)) {
            finalKey[i++ % 16] ^= b;
        }

        SecretKeySpec secretKeySpec = new SecretKeySpec(finalKey, CipherAlgorithmService.AES_ALGORITHM);

        return new SimpleByteSource(secretKeySpec.getEncoded());
    }

    @Override
    public String decrypt(String cipherText) {
        ByteSource byteSource = aesCipherService.decrypt(Base64.decode(cipherText), Base64.decode(key));
        return byteSource.obtainString();
    }

    @Override
    public String encrypt(String plainText) {
        ByteSource byteSource = aesCipherService.encrypt(CodecUtils.toBytes(plainText), Base64.decode(key));
        return byteSource.getBase64();
    }
}
