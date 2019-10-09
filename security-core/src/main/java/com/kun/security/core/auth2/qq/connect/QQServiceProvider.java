package com.kun.security.core.auth2.qq.connect;

import com.kun.security.core.auth2.qq.api.QQ;
import com.kun.security.core.auth2.qq.api.impl.QQImpl;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;

/**
 * 和服务提供商交换信息的类
 */
public class QQServiceProvider extends AbstractOAuth2ServiceProvider<QQ> {

    // QQ登录页URL
    private static final String AUTHORIZE_URL =    "https://graph.qq.com/oauth2.0/authorize";

    // QQ获取Token的URL
    private static final String ACCESS_TOKEN_URL = "https://graph.qq.com/oauth2.0/token";

    // QQ互联申请的App ID
    private String oauthConsumerKey;

    /**
     * clientId : QQ互联申请的App ID
     * clientSecret : QQ互联申请的APP Key
     */
    public QQServiceProvider(String clientId, String clientSecret) {
        super(new QQOAuthTemplate(clientId, clientSecret, AUTHORIZE_URL, ACCESS_TOKEN_URL));
        this.oauthConsumerKey = clientId;
    }

    @Override
    public QQ getApi(String accessToken) {
        return new QQImpl(accessToken, oauthConsumerKey);
    }
}
