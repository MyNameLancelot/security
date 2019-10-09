package com.kun.security.core.auth2.qq.api.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kun.security.core.auth2.qq.api.QQ;
import com.kun.security.core.auth2.qq.domain.QQUserInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;

import java.io.IOException;
import java.text.MessageFormat;

/**
 * 获取QQ用户信息实现
 */
public class QQImpl extends AbstractOAuth2ApiBinding implements QQ{

    // 获取openid【用户的唯一标识】的URL
    private static final String GET_OPENID_URL = "https://graph.qq.com/oauth2.0/me?access_token={0}";

    // 获取用户信息的URL，accessToken会因为TokenStrategy.ACCESS_TOKEN_PARAMETER自动带上
    private static final String GET_USER_INFO_URL = "https://graph.qq.com/user/get_user_info?oauth_consumer_key={0}&openid={1}";

    // 申请QQ互联的APP ID
    private String oauthConsumerKey;

    // 用户openid【用户的唯一标识】
    private String openid;

    public QQImpl(String accessToken, String oauthConsumerKey) {
        super(accessToken, TokenStrategy.ACCESS_TOKEN_PARAMETER);
        this.oauthConsumerKey = oauthConsumerKey;
        // 获取openid
        String openidResult = getRestTemplate().getForObject(MessageFormat.format(GET_OPENID_URL, accessToken), String.class);
        if(StringUtils.isEmpty(openidResult)) {
            throw new RuntimeException("openid获取失败");
        }
        this.openid = StringUtils.substringBetween(openidResult, "\"openid\":\"", "\"}");
    }

    @Override
    public QQUserInfo getUserInfo() {
        // 填充数据
        String url = MessageFormat.format(GET_USER_INFO_URL, oauthConsumerKey, openid);
        String userInfoJson = getRestTemplate().getForObject(url, String.class);
        QQUserInfo qqUserInfo = null;
        try {
            // 解析用户信息
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            qqUserInfo = objectMapper.readValue(userInfoJson, QQUserInfo.class);
            qqUserInfo.setOpenId(openid);
        } catch (IOException e) {
            throw new RuntimeException("json转换失败");
        }
        return qqUserInfo;
    }
}
