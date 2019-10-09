package com.kun.security.app.controller;

import com.kun.security.core.conf.LoginVerifySmsSender;
import com.kun.security.core.domain.SmsValidateCode;
import com.kun.security.core.response.SimpleResponse;
import com.kun.security.core.untils.SmsCodeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@ConditionalOnProperty(prefix = "login.verify.sms", value = "enable", havingValue = "true")
@Controller
public class SmsCodeController {

    private static Logger log = LoggerFactory.getLogger(SmsCodeController.class);

    public static final String SMS_CODE_MOBILE_PREFIX = "VERIFY_CODE:SMS_CODE_MOBILE:";

    @Autowired
    private SmsCodeUtil smsCodeUtil;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private LoginVerifySmsSender loginVerifySmsSender;

    @GetMapping(value = "/verify/sms/code/{mobile}", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseBody
    public SimpleResponse getVerifyCode(@PathVariable("mobile") String mobile, @RequestParam("device") String device, HttpServletRequest request, HttpServletResponse response) throws IOException {
        SmsValidateCode smsValidateCode = smsCodeUtil.generateCode(mobile);
        if(loginVerifySmsSender.sendVerifyCode(smsValidateCode)) {
            //发送短信
            log.debug("给{}手机发送验证短信，验证码{}成功", smsValidateCode.getMobile(), smsValidateCode.getCode());
        }else {
            log.debug("给{}手机发送验证短信，验证码{}失败", smsValidateCode.getMobile(), smsValidateCode.getCode());
            return new SimpleResponse(String.valueOf(HttpStatus.OK.value()), "短信发送失败");
        }
        redisTemplate.boundValueOps(SMS_CODE_MOBILE_PREFIX + device + ":" + mobile).set(smsValidateCode, smsValidateCode.getExpireDate(), TimeUnit.MILLISECONDS);
        SimpleResponse simpleResponse = new SimpleResponse(String.valueOf(HttpStatus.OK.value()), "短信发送成功");
        return simpleResponse;
    }
}
