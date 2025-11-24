package com.github.dactiv.framework.socketio;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.github.dactiv.framework.socketio.domain.SocketPrincipal;
import com.github.dactiv.framework.socketio.interceptor.SocketServerInterceptor;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.CommandLineRunner;

import java.util.List;
import java.util.UUID;

/**
 * @author maurie.chen
 */
public class SocketServerManager implements CommandLineRunner, DisposableBean {

    private final SocketIOServer socketServer;

    private final SocketUserDetailsAuthentication userDetailsAuthentication;

    private final List<SocketServerInterceptor> socketServerInterceptors;

    public SocketServerManager(SocketIOServer socketServer,
                               SocketUserDetailsAuthentication userDetailsAuthentication,
                               List<SocketServerInterceptor> socketServerInterceptors) {
        this.socketServer = socketServer;
        this.userDetailsAuthentication = userDetailsAuthentication;
        this.socketServerInterceptors = socketServerInterceptors;
    }

    @Override
    public void destroy() throws Exception {
        for (SocketServerInterceptor socketServerInterceptor : socketServerInterceptors) {
            socketServerInterceptor.destroy();
        }
        socketServer.stop();
    }

    @Override
    public void run(String... args) throws Exception {
        for (SocketServerInterceptor socketServerInterceptor : socketServerInterceptors) {
            socketServerInterceptor.run(args);
        }
        socketServer.startAsync();
    }

    public SocketIOClient getClient(String deviceIdentified) {
        return socketServer.getClient(UUID.fromString(deviceIdentified));
    }

    public SocketIOClient getClientByPrincipal(String principal) {
        SocketPrincipal socketPrincipal = userDetailsAuthentication.getSocketPrincipal(principal);
        return getClient(socketPrincipal.getDeviceIdentified());
    }

    public SocketPrincipal getSocketPrincipal(String principal) {
        return userDetailsAuthentication.getSocketPrincipal(principal);
    }

    public SocketPrincipal getSocketPrincipalByDeviceIdentified(String deviceIdentified) {
        return userDetailsAuthentication.getSocketPrincipalByDeviceIdentified(deviceIdentified);
    }

    public SocketIOServer getSocketServer() {
        return socketServer;
    }
}
