package com.github.dactiv.framework.mybatis.plus;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.github.dactiv.framework.crypto.algorithm.cipher.AesCipherService;
import com.github.dactiv.framework.crypto.algorithm.cipher.RsaCipherService;
import com.github.dactiv.framework.mybatis.MybatisAutoConfiguration;
import com.github.dactiv.framework.mybatis.config.OperationDataTraceProperties;
import com.github.dactiv.framework.mybatis.interceptor.audit.OperationDataTraceResolver;
import com.github.dactiv.framework.mybatis.plus.audit.MybatisPlusOperationDataTraceResolver;
import com.github.dactiv.framework.mybatis.plus.config.CryptoProperties;
import com.github.dactiv.framework.mybatis.plus.crypto.DataAesCryptoService;
import com.github.dactiv.framework.mybatis.plus.crypto.DataRsaCryptoService;
import com.github.dactiv.framework.mybatis.plus.interceptor.DecryptInterceptor;
import com.github.dactiv.framework.mybatis.plus.interceptor.EncryptInnerInterceptor;
import com.github.dactiv.framework.mybatis.plus.interceptor.LastModifiedDateInnerInterceptor;
import com.github.dactiv.framework.spring.web.query.QueryGenerator;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis-plus 自动配置实现
 *
 * @author maurice.chen
 */
@Configuration
@AutoConfigureBefore(MybatisAutoConfiguration.class)
@EnableConfigurationProperties({CryptoProperties.class, OperationDataTraceProperties.class})
@ConditionalOnProperty(prefix = "dactiv.mybatis.plus", value = "enabled", matchIfMissing = true)
public class MybatisPlusAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(QueryGenerator.class)
    public MybatisPlusQueryGenerator<?> mybatisPlusQueryGenerator() {
        return new MybatisPlusQueryGenerator<>();
    }

    @Bean
    @ConditionalOnMissingBean(OperationDataTraceResolver.class)
    @ConditionalOnProperty(prefix = "dactiv.mybatis.operation-data-trace", value = "enabled", matchIfMissing = true)
    public MybatisPlusOperationDataTraceResolver mybatisPlusOperationDataTraceRepository(OperationDataTraceProperties operationDataTraceProperties) {
        return new MybatisPlusOperationDataTraceResolver(operationDataTraceProperties);
    }

    @Bean
    @ConditionalOnMissingBean(MybatisPlusInterceptor.class)
    public MybatisPlusInterceptor mybatisPlusInterceptor(ApplicationContext applicationContext) {

        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor(true));
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        interceptor.addInnerInterceptor(new LastModifiedDateInnerInterceptor(true));
        interceptor.addInnerInterceptor(new EncryptInnerInterceptor(true, applicationContext));

        return interceptor;
    }

    @Bean
    @ConditionalOnMissingBean(DecryptInterceptor.class)
    public DecryptInterceptor decryptInterceptor(ApplicationContext applicationContext) {
        return new DecryptInterceptor(applicationContext);
    }

    @ConditionalOnMissingBean(DataAesCryptoService.class)
    @Bean(CryptoProperties.MYBATIS_PLUS_DATA_AES_CRYPTO_SERVICE_NAME)
    @ConditionalOnProperty(prefix = "dactiv.mybatis.plus.crypto", value = "enabled", matchIfMissing = true)
    public DataAesCryptoService dataAesCryptoService(CryptoProperties cryptoProperties) {
        return new DataAesCryptoService(new AesCipherService(), cryptoProperties.getDataAesCryptoKey());
    }

    @ConditionalOnMissingBean(DataRsaCryptoService.class)
    @Bean(CryptoProperties.MYBATIS_PLUS_DATA_RSA_CRYPTO_SERVICE_NAME)
    @ConditionalOnProperty(prefix = "dactiv.mybatis.plus.crypto", value = "enabled", matchIfMissing = true)
    public DataRsaCryptoService dataRsaCryptoService(CryptoProperties cryptoProperties) {
        return new DataRsaCryptoService(new RsaCipherService(), cryptoProperties.getDataRasCryptoPublicKey(), cryptoProperties.getDataRasCryptoPrivateKey());
    }

}
