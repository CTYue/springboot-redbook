package com.chuwa.redbook.payload.security;

/**
 * @author b1go
 * @date 7/1/22 1:08 AM
 */
public class JWTAuthResponse {

    private String accessToken;
    private String tokenType;

    public JWTAuthResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}
