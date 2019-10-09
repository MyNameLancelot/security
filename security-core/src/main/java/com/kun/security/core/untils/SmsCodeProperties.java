package com.kun.security.core.untils;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "login.verify.sms", ignoreUnknownFields = true)
public class SmsCodeProperties {

    private static final int DEFAULT_CODE_COUNT   = 4;
    private static final Long DEFAULT_EXPIRE_DATE = 6000L;

    private Long    expireDate = DEFAULT_EXPIRE_DATE;  // 过期时间
    private Integer codeCount  = DEFAULT_CODE_COUNT;   // 长度
    private Boolean enable;

    public Long getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Long expireDate) {
        this.expireDate = expireDate;
    }

    public Integer getCodeCount() {
        return codeCount;
    }

    public void setCodeCount(Integer codeCount) {
        this.codeCount = codeCount;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }
}
