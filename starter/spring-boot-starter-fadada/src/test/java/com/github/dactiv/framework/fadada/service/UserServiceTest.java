package com.github.dactiv.framework.fadada.service;


import com.github.dactiv.framework.fadada.domain.body.user.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class UserServiceTest {

    private final UserService userService;

    @Autowired
    public UserServiceTest(UserService userService) {
        this.userService = userService;
    }

    @Test
    public void testUserAuth() {
        GetUserAuthUrlRequestBody getUserAuthUrlRequestBody = new GetUserAuthUrlRequestBody();

        getUserAuthUrlRequestBody.setClientUserId("c1d9d3eed6b243c9bb5f54da712798e3");
        getUserAuthUrlRequestBody.setAccountName("18776974353");
        getUserAuthUrlRequestBody.setAuthScopes(List.of("ident_info","seal_info","signtask_init","signtask_info","signtask_file"));

        GetUserAuthIdentRequestBody body = new GetUserAuthIdentRequestBody();
        body.setMobile("18776974353");
        body.setUserName("陈小菠");
        body.setFaceauthMode("tencent");
        getUserAuthUrlRequestBody.setUserIdentInfo(body);

        AuthUrlResponseBody authUrlResponseBody = userService.getUserAuthUrl(getUserAuthUrlRequestBody);
        Assertions.assertTrue(StringUtils.isNotEmpty(authUrlResponseBody.getAuthUrl()));
        Assertions.assertTrue(StringUtils.isNotEmpty(authUrlResponseBody.getAuthShortUrl()));
    }

    @Test
    public void testUserAuthStatus() {
        GetUserAutStatusRequestBody requestBody = new GetUserAutStatusRequestBody();

        requestBody.setClientUserId("c1d9d3eed6b243c9bb5f54da712798e3");

        UserAuthStatusResponseBody responseBody = userService.getUserAuthStatus(requestBody);
        Assertions.assertTrue(responseBody.getClientUserId().equals("c1d9d3eed6b243c9bb5f54da712798e3"));

    }
}
