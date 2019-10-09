package com.kun.security.core;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.core")
public class CoreSecurityProperties {
    private String  loginPageUrl;               //登陆页面路径URL【当发现需要身份认证时跳入到这个请求】
    private String  loginHtmlUrl;               //登陆页面URL
    private String  authenticationUrl;          //提交登陆信息的URL
    private String  smsAuthenticationUrl;       //短信信息的URL
    private String  registerHtmlUrl;            //注册页的URL
    private String  registerPostUrl;            //注册页提交的URL

    public String getLoginPageUrl() {
        return loginPageUrl;
    }

    public void setLoginPageUrl(String loginPageUrl) {
        this.loginPageUrl = loginPageUrl;
    }

    public String getAuthenticationUrl() {
        return authenticationUrl;
    }

    public void setAuthenticationUrl(String authenticationUrl) {
        this.authenticationUrl = authenticationUrl;
    }

    public String getLoginHtmlUrl() {
        return loginHtmlUrl;
    }

    public void setLoginHtmlUrl(String loginHtmlUrl) {
        this.loginHtmlUrl = loginHtmlUrl;
    }

    public String getSmsAuthenticationUrl() {
        return smsAuthenticationUrl;
    }

    public void setSmsAuthenticationUrl(String smsAuthenticationUrl) {
        this.smsAuthenticationUrl = smsAuthenticationUrl;
    }

    public String getRegisterHtmlUrl() {
        return registerHtmlUrl;
    }

    public void setRegisterHtmlUrl(String registerHtmlUrl) {
        this.registerHtmlUrl = registerHtmlUrl;
    }

    public String getRegisterPostUrl() {
        return registerPostUrl;
    }

    public void setRegisterPostUrl(String registerPostUrl) {
        this.registerPostUrl = registerPostUrl;
    }
}
