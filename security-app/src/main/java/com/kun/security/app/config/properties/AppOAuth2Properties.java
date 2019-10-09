package com.kun.security.app.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "app.oauth2")
public class AppOAuth2Properties {

    /**
     * 每个不同产品需要配置不同的客户端信息
     */
    private List<OAuth2ClientProperties> client;

    /**
     * jwt方式需要配置key
     */
    private String jwtSigningKey;

    /**
     * 存储类型可选择jwt和redis
     */
    private String storeType;

    /**
     * 简化授权模式登陆url地址
     */
    private String openIdLoginUrl;

    public List<OAuth2ClientProperties> getClient() {
        return client;
    }

    public void setClient(List<OAuth2ClientProperties> client) {
        this.client = client;
    }

    public String getJwtSigningKey() {
        return jwtSigningKey;
    }

    public void setJwtSigningKey(String jwtSigningKey) {
        this.jwtSigningKey = jwtSigningKey;
    }

    public String getStoreType() {
        return storeType;
    }

    public void setStoreType(String storeType) {
        this.storeType = storeType;
    }

    public String getOpenIdLoginUrl() {
        return openIdLoginUrl;
    }

    public void setOpenIdLoginUrl(String openIdLoginUrl) {
        this.openIdLoginUrl = openIdLoginUrl;
    }
}
