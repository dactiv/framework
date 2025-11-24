package com.github.dactiv.framework.socketio;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.github.dactiv.framework.socketio.domain.SocketPrincipal;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.task.TaskExecutor;

import java.util.UUID;

/**
 * @author maurie.chen
 */
public class SocketServerManager implements CommandLineRunner, DisposableBean {

    private final SocketIOServer socketServer;

    private final TaskExecutor taskExecutor;

    private final SocketUserDetailsAuthentication userDetailsAuthentication;

    public SocketServerManager(SocketIOServer socketServer,
                               SocketUserDetailsAuthentication userDetailsAuthentication) {
        this.socketServer = socketServer;
        this.userDetailsAuthentication = userDetailsAuthentication;
    }

    @Override
    public void destroy() throws Exception {
        socketServer.stop();
    }

    @Override
    public void run(String... args) throws Exception {
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
