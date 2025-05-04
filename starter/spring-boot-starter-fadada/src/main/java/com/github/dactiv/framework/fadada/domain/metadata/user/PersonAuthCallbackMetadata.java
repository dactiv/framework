package com.github.dactiv.framework.fadada.domain.metadata.user;

import java.io.Serial;

public class PersonAuthCallbackMetadata extends ClientUserIdRequestMetadata {

    public static final String SIGNATURE_FIELD_NAME = "signature";

    @Serial
    private static final long serialVersionUID = 8422998972507169524L;

    private String timestamp;

    private String signature;

    private String authResult;
    private String authFailedReason;
    private String authScope;

    public PersonAuthCallbackMetadata() {
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getAuthResult() {
        return authResult;
    }

    public void setAuthResult(String authResult) {
        this.authResult = authResult;
    }

    public String getAuthFailedReason() {
        return authFailedReason;
    }

    public void setAuthFailedReason(String authFailedReason) {
        this.authFailedReason = authFailedReason;
    }

    public String getAuthScope() {
        return authScope;
    }

    public void setAuthScope(String authScope) {
        this.authScope = authScope;
    }
}
