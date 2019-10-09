package com.kun.security.core.untils;

import com.kun.security.core.domain.SmsValidateCode;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;

@Configuration
@ConditionalOnProperty(prefix = "login.verify.sms", name = "enable")
@EnableConfigurationProperties(SmsCodeProperties.class)
public class SmsCodeUtil {

    private Logger log = LoggerFactory.getLogger(SmsCodeUtil.class);

    private SmsCodeProperties smsCodeProperties;

    public SmsCodeUtil(SmsCodeProperties smsCodeProperties) {
        this.smsCodeProperties = smsCodeProperties;
    }

    /**
     * code为生成的验证码
     * codePic为生成的验证码BufferedImage对象
     */
    public SmsValidateCode generateCode(String mobile) {
        Long expireDate = smsCodeProperties.getExpireDate();      // 过期时间
        int  codeCount  = smsCodeProperties.getCodeCount();       // 定义短信验证码的个数

        SmsValidateCode smsCode = new SmsValidateCode(RandomStringUtils.randomNumeric(codeCount), mobile);
        if (expireDate != null){
            smsCode.setExpireDate(Instant.now().getEpochSecond() + expireDate);
        }
        log.debug("生成短信验证码{}", smsCode.getCode());
        return smsCode ;
    }
}
