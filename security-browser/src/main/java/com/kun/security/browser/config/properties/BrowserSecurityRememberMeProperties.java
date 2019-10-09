package com.kun.security.browser.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "browser.remember.me")
public class BrowserSecurityRememberMeProperties {
    private Boolean rememberMe;                 //是否开启rememberMe
    private Integer rememberMeValiditySeconds;  //rememberMe有效时间
    private String  rememberMeParameter;        //是否勾选rememberMe的参数

    public Boolean getRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(Boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public Integer getRememberMeValiditySeconds() {
        return rememberMeValiditySeconds;
    }

    public void setRememberMeValiditySeconds(Integer rememberMeValiditySeconds) {
        this.rememberMeValiditySeconds = rememberMeValiditySeconds;
    }

    public String getRememberMeParameter() {
        return rememberMeParameter;
    }

    public void setRememberMeParameter(String rememberMeParameter) {
        this.rememberMeParameter = rememberMeParameter;
    }
}
