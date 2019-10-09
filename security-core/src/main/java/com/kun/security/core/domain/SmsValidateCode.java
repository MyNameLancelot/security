package com.kun.security.core.domain;

/**
 * 图片验证码
 * 包含：手机号、验证码基类
 */
public class SmsValidateCode extends BaseValidateCode{

    private String mobile;

    public SmsValidateCode(String code, String mobile) {
        super(code);
        this.mobile = mobile;
    }

    public SmsValidateCode(String code, Long expireDate, String mobile) {
        super(code, expireDate);
        this.mobile = mobile;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
