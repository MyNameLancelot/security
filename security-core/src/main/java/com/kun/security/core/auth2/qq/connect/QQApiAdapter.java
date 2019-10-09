package com.kun.security.core.auth2.qq.connect;

import com.kun.security.core.auth2.qq.api.QQ;
import com.kun.security.core.auth2.qq.domain.QQUserInfo;
import com.kun.security.core.exception.AuthenticationException;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

/**
 * 将返回的QQ用户信息转换为标准的用户信息用于存储
 */
public class QQApiAdapter implements ApiAdapter<QQ> {

    /**
     * 不对网络进行检测
     */
    @Override
    public boolean test(QQ api) {
        return true;
    }

    /**
     * 获取用户信息
     */
    @Override
    public void setConnectionValues(QQ api, ConnectionValues values) {
        QQUserInfo qqUserInfo = api.getUserInfo();
        // 显示的用户名（QQ为昵称）
        values.setDisplayName(qqUserInfo.getNickname());
        // 头像（QQ头像）
        values.setImageUrl(qqUserInfo.getFigureurl_qq_1());
        // 个人主页（QQ没有）
        values.setProfileUrl(null);
        // 用户的唯一标识ID
        values.setProviderUserId(qqUserInfo.getOpenId());
    }

    /**
     * 解绑
     */
    @Override
    public UserProfile fetchUserProfile(QQ api) {
        return null;
    }

    @Override
    public void updateStatus(QQ api, String message) {
        throw new AuthenticationException("QQ没有个人主页,不能发布消息,时间线不能更新");
    }
}
