package com.dependencyimpact.serviceregistry.dto;

public class LoginResponse {

    private final String accessToken;
    private final String tokenType;
    private final long expiresIn;
    private final CurrentUserResponse user;

    public LoginResponse(String accessToken, String tokenType, long expiresIn, CurrentUserResponse user) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public CurrentUserResponse getUser() {
        return user;
    }
}
