package com.kun.security.core.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 校验码异常类
 */
public class ValidateCodeException extends AuthenticationException {

    public ValidateCodeException(String msg, Throwable t) {
        super(msg, t);
    }

    public ValidateCodeException(String msg) {
        super(msg);
    }
}
