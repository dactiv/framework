package com.github.dactiv.framework.spring.security.authentication.handler;

import com.github.dactiv.framework.commons.Casts;
import com.github.dactiv.framework.commons.RestResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

/**
 * Forbidden 访问 handler 处理
 *
 * @author maurice.chen
 */
public class ForbiddenAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        RestResult<?> result = RestResult.of(
                accessDeniedException.getMessage(),
                HttpStatus.FORBIDDEN.value(),
                String.valueOf(HttpStatus.FORBIDDEN.value())
        );
        response.getWriter().write(Casts.writeValueAsString(result));
    }
}
