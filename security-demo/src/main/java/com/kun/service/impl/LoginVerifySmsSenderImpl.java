package com.kun.service.impl;

import com.kun.security.core.conf.LoginVerifySmsSender;
import com.kun.security.core.domain.SmsValidateCode;
import org.springframework.stereotype.Service;

@Service
public class LoginVerifySmsSenderImpl implements LoginVerifySmsSender {
    @Override
    public boolean sendVerifyCode(SmsValidateCode smsValidateCode) {
        return true;
    }
}
