package com.kun.security.core.controller;

public interface SecurityBrowserConstants {
    String VALIDATE_IMAGE_URL = "/verify/image/code"; // 图片验证码允许访问地址
    String VALIDATE_SMS_URL   = "/verify/sms/code/*"; // 短信验证码允许访问地址
}
