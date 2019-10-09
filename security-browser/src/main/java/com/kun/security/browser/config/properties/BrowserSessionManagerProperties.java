package com.kun.security.browser.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "browser.session.manager")
public class BrowserSessionManagerProperties {

    private String  invalidSessionUrl;
    private Integer maximumSessions;
    private Boolean maxSessionsPreventsLogin;

    public String getInvalidSessionUrl() {
        return invalidSessionUrl;
    }

    public void setInvalidSessionUrl(String invalidSessionUrl) {
        this.invalidSessionUrl = invalidSessionUrl;
    }

    public Integer getMaximumSessions() {
        return maximumSessions;
    }

    public void setMaximumSessions(Integer maximumSessions) {
        this.maximumSessions = maximumSessions;
    }

    public Boolean getMaxSessionsPreventsLogin() {
        return maxSessionsPreventsLogin;
    }

    public void setMaxSessionsPreventsLogin(Boolean maxSessionsPreventsLogin) {
        this.maxSessionsPreventsLogin = maxSessionsPreventsLogin;
    }
}
