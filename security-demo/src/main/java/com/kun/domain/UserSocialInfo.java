package com.kun.domain;

/**
 * 第三方账户得登陆信息
 */
public class UserSocialInfo {
    private String providerId;
    private String providerUserId;
    private String nickName;
    private String headUrl;

    public UserSocialInfo(String providerId, String providerUserId, String nickName, String head) {
        this.providerId = providerId;
        this.providerUserId = providerUserId;
        this.nickName = nickName;
        this.headUrl = head;
    }

    public UserSocialInfo() {
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getProviderUserId() {
        return providerUserId;
    }

    public void setProviderUserId(String providerUserId) {
        this.providerUserId = providerUserId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }
}
