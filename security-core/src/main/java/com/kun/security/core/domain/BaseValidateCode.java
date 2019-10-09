package com.kun.security.core.domain;


import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;

/**
 * 验证码基类
 * 包含：过期时间和验证码
 */
public class BaseValidateCode implements Serializable {
    // 验证码
    private String code;
    // 验证码过期时间
    private Long expireDate;

    public BaseValidateCode() {
    }

    public BaseValidateCode(String code) {
        this.code = code;
    }

    public BaseValidateCode(String code, Long expireDate) {
        this.code = code;
        this.expireDate = expireDate;
    }

    /**
     * 与现在时间比较是否过期
     */
    public boolean compareExpired() {
        if(expireDate == null) {
            return false;
        }
        Duration nowDuration = Duration.ofSeconds(Instant.now().getEpochSecond());
        Duration expireDuration = Duration.ofSeconds(expireDate);
        return nowDuration.compareTo(expireDuration) <= 0 ? false : true;
    }

    // =========================GET/SET=========================
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Long expireDate) {
        this.expireDate = expireDate;
    }
}
