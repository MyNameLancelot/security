package com.kun.security.core.auth2.wechat.api.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kun.security.core.auth2.wechat.api.WeChat;
import com.kun.security.core.auth2.wechat.domain.WeChatUserInfo;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * 获取微信用户信息实现
 */
public class WeChatImpl extends AbstractOAuth2ApiBinding implements WeChat {

    // 获取用户信息的URL，accessToken会因为TokenStrategy.ACCESS_TOKEN_PARAMETER自动带上
    private static final String GET_USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo?openid=";

    public WeChatImpl(String accessToken) {
        super(accessToken, TokenStrategy.ACCESS_TOKEN_PARAMETER);
    }

    /**
     * 默认注册的StringHttpMessageConverter字符集为ISO-8859-1，而微信返回的是UTF-8的，所以覆盖了原来的方法。
     */
    protected List<HttpMessageConverter<?>> getMessageConverters() {
        List<HttpMessageConverter<?>> messageConverters = super.getMessageConverters();
        messageConverters.remove(0);
        messageConverters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        return messageConverters;
    }

    // 用户openid【用户的唯一标识，微信不需要单独获取】
    @Override
    public WeChatUserInfo getUserInfo(String openId) {
        // 填充数据
        String url = GET_USER_INFO_URL + openId;
        String userInfoJson = getRestTemplate().getForObject(url, String.class);
        WeChatUserInfo weChatUserInfo = null;
        try {
            // 解析用户信息
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            weChatUserInfo = objectMapper.readValue(userInfoJson, WeChatUserInfo.class);
            weChatUserInfo.setOpenid(openId);
        } catch (IOException e) {
            throw new RuntimeException("json转换失败");
        }
        return weChatUserInfo;
    }
}
