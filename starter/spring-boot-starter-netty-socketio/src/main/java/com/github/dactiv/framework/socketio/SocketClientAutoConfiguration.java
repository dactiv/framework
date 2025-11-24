package com.github.dactiv.framework.socketio;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.store.RedissonStoreFactory;
import com.github.dactiv.framework.socketio.config.SocketConfig;
import com.github.dactiv.framework.socketio.holder.Interceptor.SocketMessageInterceptor;
import com.github.dactiv.framework.socketio.holder.SocketMessagePointcutAdvisor;
import com.github.dactiv.framework.socketio.resolver.MessageSenderResolver;
import com.github.dactiv.framework.spring.security.authentication.AccessTokenContextRepository;
import com.github.dactiv.framework.spring.security.authentication.config.AuthenticationProperties;
import com.github.dactiv.framework.spring.web.SpringWebMvcAutoConfiguration;
import com.github.dactiv.framework.spring.web.SpringWebMvcProperties;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * socket 客户端自动配置累
 *
 * @author maurice.chen
 */
@Configuration
@AutoConfigureBefore(SpringWebMvcAutoConfiguration.class)
@EnableConfigurationProperties({SpringWebMvcProperties.class, AuthenticationProperties.class, SocketConfig.class})
@ConditionalOnProperty(prefix = "dactiv.socketio.client", value = "enabled", matchIfMissing = true)
public class SocketClientAutoConfiguration {

    @Bean
    public SocketUserDetailsAuthentication socketUserDetailsAuthentication(AccessTokenContextRepository accessTokenContextRepository,
                                                                           SocketConfig socketConfig,
                                                                           RedissonClient redissonClient,
                                                                           SocketMessageClient socketMessageClient) {
        return new SocketUserDetailsAuthentication(accessTokenContextRepository, socketConfig, redissonClient, socketMessageClient);
    }

    /**
     * socket io 服务配置
     *
     * @param userDetailsAuthentication socket 用户认证
     *
     * @return socket io 服务
     */
    @Bean
    public SocketIOServer socketIoServer(SocketUserDetailsAuthentication userDetailsAuthentication, RedissonClient redissonClient) {
        userDetailsAuthentication.getSocketConfig().setAuthorizationListener(userDetailsAuthentication);
        userDetailsAuthentication.getSocketConfig().setStoreFactory(new RedissonStoreFactory(redissonClient));
        SocketIOServer socketIoServer = new SocketIOServer(userDetailsAuthentication.getSocketConfig());

        socketIoServer.addConnectListener(userDetailsAuthentication);
        socketIoServer.addDisconnectListener(userDetailsAuthentication);

        return socketIoServer;
    }

    @Bean
    @ConditionalOnMissingBean(SocketServerManager.class)
    public SocketServerManager socketServerManager(SocketUserDetailsAuthentication userDetailsAuthentication, SocketIOServer socketIoServer) {
        return new SocketServerManager(socketIoServer, userDetailsAuthentication);
    }

    @Bean
    @ConditionalOnMissingBean(SocketMessageClient.class)
    public SocketMessageClient socketMessageClient(SocketIOServer socketIoServer,
                                                   ThreadPoolTaskExecutor threadPoolTaskExecutor,
                                                   SocketUserDetailsAuthentication socketUserDetailsAuthentication,
                                                   ObjectProvider<MessageSenderResolver> messageSenderResolvers) {
        return new SocketMessageClient(
                socketIoServer,
                threadPoolTaskExecutor,
                socketUserDetailsAuthentication,
                messageSenderResolvers.stream().toList()
        );
    }

    @Bean
    public SocketResultResponseBodyAdvice socketResultResponseBodyAdvice(SpringWebMvcProperties properties,
                                                                         SocketMessageClient socketMessageClient) {
        return new SocketResultResponseBodyAdvice(properties, socketMessageClient);
    }

    @Bean
    public SocketMessageInterceptor socketMessageInterceptor(SocketMessageClient messageClient) {
        return new SocketMessageInterceptor(messageClient);
    }

    @Bean
    public SocketMessagePointcutAdvisor socketMessagePointcutAdvisor(SocketMessageInterceptor socketMessageInterceptor) {
        return new SocketMessagePointcutAdvisor(socketMessageInterceptor);
    }
}
