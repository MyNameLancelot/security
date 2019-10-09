package com.kun.security.core.auth2.wechat.connect;

import com.kun.security.core.auth2.wechat.api.WeChat;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.support.OAuth2Connection;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2ServiceProvider;

/**
 * 获取连接信息的类
 */
public class WeChatConnectFactory extends OAuth2ConnectionFactory<WeChat> {

    public WeChatConnectFactory(String providerId, String appId, String appSecret) {
        super(providerId, new WeChatServiceProvider(appId, appSecret), new WeChatApiAdapter(null));
    }

    /**
     * 由于微信的openId是和accessToken一起返回的，所以在这里直接根据accessToken设置providerUserId即可，不用像QQ那样通过QQAdapter来获取
     */
    @Override
    protected String extractProviderUserId(AccessGrant accessGrant) {
        if(accessGrant instanceof WeChatAccessGrant) {
            return ((WeChatAccessGrant)accessGrant).getOpenId();
        }
        return null;
    }

    public Connection<WeChat> createConnection(AccessGrant accessGrant) {
        return new OAuth2Connection<WeChat>(getProviderId(), extractProviderUserId(accessGrant), accessGrant.getAccessToken(),
                accessGrant.getRefreshToken(), accessGrant.getExpireTime(), getOAuth2ServiceProvider(), getApiAdapter(extractProviderUserId(accessGrant)));
    }

    public Connection<WeChat> createConnection(ConnectionData data) {
        return new OAuth2Connection<WeChat>(data, getOAuth2ServiceProvider(), getApiAdapter(data.getProviderUserId()));
    }

    private ApiAdapter<WeChat> getApiAdapter(String providerUserId) {
        return new WeChatApiAdapter(providerUserId);
    }

    private OAuth2ServiceProvider<WeChat> getOAuth2ServiceProvider() {
        return (OAuth2ServiceProvider<WeChat>) getServiceProvider();
    }
}