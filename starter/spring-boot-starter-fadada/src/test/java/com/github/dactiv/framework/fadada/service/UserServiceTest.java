package com.github.dactiv.framework.fadada.service;


import com.github.dactiv.framework.fadada.domain.body.user.*;
import com.github.dactiv.framework.fadada.domain.metadata.user.ClientUserIdRequestMetadata;
import com.github.dactiv.framework.fadada.domain.metadata.user.OpenUserIdRequestMetdata;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTest {

    private final UserService userService;

    @Autowired
    public UserServiceTest(UserService userService) {
        this.userService = userService;
    }

    @Test
    public void testUnbindUser() {
        OpenUserIdRequestMetdata userId = new OpenUserIdRequestMetdata("84a44f32ed614822bab88292993161b4");
        GetUserIdentityResponseBody body = userService.getUserIdentity(userId);
        //ClientUserIdRequestMetadata requestBody = new ClientUserIdRequestMetadata("CONSOLE:60");
        //userService.unbind(userId);
        //GetUserResponseBody body = userService.getUser(requestBody);
    }

    @Test
    public void testUserAuth() {
        GetUserAuthUrlRequestBody getUserAuthUrlRequestBody = new GetUserAuthUrlRequestBody();

        getUserAuthUrlRequestBody.setClientUserId("CONSOLE:60");
        getUserAuthUrlRequestBody.setAccountName("18776974353");

        GetUserAuthIdentRequestBody body = new GetUserAuthIdentRequestBody();
        body.setMobile("18776974353");
        body.setUserName("陈小菠");
        getUserAuthUrlRequestBody.setUserIdentInfo(body);

        AuthUrlResponseBody authUrlResponseBody = userService.getUserAuthUrl(getUserAuthUrlRequestBody);
        Assertions.assertTrue(StringUtils.isNotEmpty(authUrlResponseBody.getAuthUrl()));
        Assertions.assertTrue(StringUtils.isNotEmpty(authUrlResponseBody.getAuthShortUrl()));
    }

    @Test
    public void testUserAuthStatus() {
        ClientUserIdRequestMetadata requestBody = new ClientUserIdRequestMetadata();

        requestBody.setClientUserId("c1d9d3eed6b243c9bb5f54da712798e3");

        GetUserResponseBody responseBody = userService.getUser(requestBody);
        Assertions.assertEquals("c1d9d3eed6b243c9bb5f54da712798e3", responseBody.getClientUserId());

    }
}
