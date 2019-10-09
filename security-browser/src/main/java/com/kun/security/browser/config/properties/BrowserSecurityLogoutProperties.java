package com.kun.security.browser.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "browser.logout")
public class BrowserSecurityLogoutProperties {

    private String logoutUrl;

    private String logoutSuccessUrl;

    private String[] deleteCookies;

    public String getLogoutUrl() {
        return logoutUrl;
    }

    public void setLogoutUrl(String logoutUrl) {
        this.logoutUrl = logoutUrl;
    }

    public String getLogoutSuccessUrl() {
        return logoutSuccessUrl;
    }

    public void setLogoutSuccessUrl(String logoutSuccessUrl) {
        this.logoutSuccessUrl = logoutSuccessUrl;
    }

    public String[] getDeleteCookies() {
        return deleteCookies;
    }

    public void setDeleteCookies(String[] deleteCookies) {
        this.deleteCookies = deleteCookies;
    }
}
