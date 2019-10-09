package com.kun.security.core.auth2.qq.connect;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * 发出请求的类
 */
public class QQOAuthTemplate extends OAuth2Template {

    public QQOAuthTemplate(String clientId, String clientSecret, String authorizeUrl, String accessTokenUrl) {
        super(clientId, clientSecret, authorizeUrl, accessTokenUrl);
        // setUseParametersForClientAuthentication设置为true才能将clientId、clientSecret以请求参数的方式带出去
        setUseParametersForClientAuthentication(true);
    }

    /***
     * QQ返回的用户信息是text/html，所以需要处理返回类型
     */
    @Override
    protected RestTemplate createRestTemplate() {
        RestTemplate restTemplate = super.createRestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
        return restTemplate;
    }

    /**
     * 解析授权
     */
    @Override
    protected AccessGrant postForAccessGrant(String accessTokenUrl, MultiValueMap<String, String> parameters) {
        String response = getRestTemplate().postForObject(accessTokenUrl, parameters, String.class);
        String[] items = response.split("&");
        String accessToken = StringUtils.substringAfterLast(items[0], "=");
        Long expiresIn = Long.valueOf(StringUtils.substringAfterLast(items[1], "="));
        String refreshToken = StringUtils.substringAfterLast(items[2], "=");
        return new AccessGrant(accessToken, null, refreshToken, expiresIn);
    }
}
