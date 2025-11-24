package com.github.dactiv.framework.socketio.interceptor;

public interface SocketServerInterceptor {

    void destroy() throws Exception;

    void run(String... args) throws Exception;
}
