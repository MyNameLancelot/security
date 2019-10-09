package com.kun.security.core.conf;

import com.kun.security.core.domain.SmsValidateCode;

public interface LoginVerifySmsSender {

    boolean sendVerifyCode(SmsValidateCode smsValidateCode);
}
