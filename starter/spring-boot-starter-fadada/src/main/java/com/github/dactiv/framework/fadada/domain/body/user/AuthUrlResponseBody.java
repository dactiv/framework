package com.github.dactiv.framework.fadada.domain.body.user;

public class AuthUrlResponseBody {

    private String authUrl;
    private String authShortUrl;

    public AuthUrlResponseBody() {
    }

    public String getAuthUrl() {
        return authUrl;
    }

    public void setAuthUrl(String authUrl) {
        this.authUrl = authUrl;
    }

    public String getAuthShortUrl() {
        return authShortUrl;
    }

    public void setAuthShortUrl(String authShortUrl) {
        this.authShortUrl = authShortUrl;
    }
}
