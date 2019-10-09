package com.kun.security.app.config.properties;

public class OAuth2ClientProperties {
    private String  clientId;
    private String  secret;
    private Integer tokenValiditySeconds;
    private String  scope;
    private String  redirectUrl;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public Integer getTokenValiditySeconds() {
        return tokenValiditySeconds;
    }

    public void setTokenValiditySeconds(Integer tokenValiditySeconds) {
        this.tokenValiditySeconds = tokenValiditySeconds;
    }
}
