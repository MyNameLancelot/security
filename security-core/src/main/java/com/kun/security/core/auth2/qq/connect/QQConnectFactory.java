package com.kun.security.core.auth2.qq.connect;

import com.kun.security.core.auth2.qq.api.QQ;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;

/**
 * 获取连接信息的类
 */
public class QQConnectFactory extends OAuth2ConnectionFactory<QQ> {

    /**
     * providerId：要与发出请求的路径最后结尾相同
     * clientId：APP ID
     * clientSecret：APP Key
     */
    public QQConnectFactory(String providerId, String clientId, String clientSecret ) {
        super(providerId, new QQServiceProvider(clientId, clientSecret), new QQApiAdapter());
    }
}
