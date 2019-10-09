package com.kun.security.core.auth2.wechat.connect;

import com.kun.security.core.auth2.wechat.api.WeChat;
import com.kun.security.core.auth2.wechat.api.impl.WeChatImpl;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;

/**
 * 和服务提供商交换信息的类
 */
public class WeChatServiceProvider extends AbstractOAuth2ServiceProvider<WeChat> {

    // 微信登录页URL
    private static final String AUTHORIZE_URL = "https://open.weixin.qq.com/connect/qrconnect";

    // 微信获取Token的URL
    private static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";

    // 微信互联申请的App ID
    private String oauthConsumerKey;

    /**
     * clientId : 微信申请的App ID
     * clientSecret : 微信申请的APP Key
     */
    public WeChatServiceProvider(String clientId, String clientSecret) {
        super(new WeChatOAuthTemplate(clientId, clientSecret, AUTHORIZE_URL, ACCESS_TOKEN_URL));
        this.oauthConsumerKey = clientId;
    }

    @Override
    public WeChat getApi(String accessToken) {
        return new WeChatImpl(accessToken);
    }
}
