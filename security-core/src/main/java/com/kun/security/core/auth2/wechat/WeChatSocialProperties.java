package com.kun.security.core.auth2.wechat;

import com.kun.security.core.auth2.AuthenticationIdUserIdSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.social.UserIdSource;

@ConfigurationProperties(prefix = "wechat.social")
public class WeChatSocialProperties {

    // SpringSecurity默认使用AuthenticationNameUserIdSource【用户名对应方式】，现改为使用ID方式
    static private Class<AuthenticationIdUserIdSource> DEFAULT_USER_ID_SOURCE_TYPE = AuthenticationIdUserIdSource.class;

    private String appId;
    private String appKey;
    private String callBackUri;
    private String providerId;
    private Class<? extends UserIdSource> UserIdSourceType = DEFAULT_USER_ID_SOURCE_TYPE;
    private Boolean enable;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getCallBackUri() {
        return callBackUri;
    }

    public void setCallBackUri(String callBackUri) {
        this.callBackUri = callBackUri;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public Class<? extends UserIdSource> getUserIdSourceType() {
        return UserIdSourceType;
    }

    public void setUserIdSourceType(Class<UserIdSource> userIdSourceType) {
        UserIdSourceType = userIdSourceType;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }
}
