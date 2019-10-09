package com.kun.security.core.auth2.wechat.api;

import com.kun.security.core.auth2.wechat.domain.WeChatUserInfo;

/**
 * 获取微信用户信息
 */
public interface WeChat {

    // 微信省略了一步，直接将accessToken和openId全部返回了
    WeChatUserInfo getUserInfo(String openId);
}
