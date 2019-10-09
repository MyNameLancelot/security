package com.kun.security.core.auth2.wechat.connect;

import com.kun.security.core.auth2.wechat.api.WeChat;
import com.kun.security.core.auth2.wechat.domain.WeChatUserInfo;
import com.kun.security.core.exception.AuthenticationException;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

/**
 * 将返回的微信用户信息转换为标准的用户信息用于存储
 */
public class WeChatApiAdapter implements ApiAdapter<WeChat> {

    private String openId;

    public WeChatApiAdapter(String openId){
        this.openId = openId;
    }

    /**
     * 不对网络进行检测
     */
    @Override
    public boolean test(WeChat api) {
        return true;
    }

    /**
     * 获取用户信息
     */
    @Override
    public void setConnectionValues(WeChat api, ConnectionValues values) {
        WeChatUserInfo weChatUserInfo = api.getUserInfo(openId);
        // 显示的用户名（微信昵称）
        values.setDisplayName(weChatUserInfo.getNickname());
        // 头像（微信头像）
        values.setImageUrl(weChatUserInfo.getHeadimgurl());
        // 个人主页（微信没有）
        values.setProfileUrl(null);
        // 用户的唯一标识ID
        values.setProviderUserId(weChatUserInfo.getOpenid());
    }

    /**
     * 解绑
     */
    @Override
    public UserProfile fetchUserProfile(WeChat api) {
        return null;
    }

    @Override
    public void updateStatus(WeChat api, String message) {
        throw new AuthenticationException("微信没有个人主页,不能发布消息,时间线不能更新");
    }
}
