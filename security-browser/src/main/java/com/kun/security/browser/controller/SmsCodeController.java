package com.kun.security.browser.controller;

import com.kun.security.core.conf.LoginVerifySmsSender;
import com.kun.security.core.domain.SmsValidateCode;
import com.kun.security.core.response.SimpleResponse;
import com.kun.security.core.untils.SmsCodeUtil;
import org.apache.commons.codec.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ConditionalOnProperty(prefix = "login.verify.sms", value = "enable", havingValue = "true")
@Controller
public class SmsCodeController {

    private static Logger log = LoggerFactory.getLogger(SmsCodeController.class);

    public static final String SMS_CODE_MOBILE_PREFIX = "SMS_CODE_MOBILE_";

    @Autowired
    private SmsCodeUtil smsCodeUtil;

    // spring工具类用于操作session
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    @Autowired
    private LoginVerifySmsSender loginVerifySmsSender;

    @GetMapping(value = "/verify/sms/code/{mobile}", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseBody
    public SimpleResponse getVerifyCode(@PathVariable("mobile") String mobile, HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setCharacterEncoding(CharEncoding.UTF_8);
        response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
        SmsValidateCode smsValidateCode = smsCodeUtil.generateCode(mobile);
        if(loginVerifySmsSender.sendVerifyCode(smsValidateCode)) {
            //发送短信
            log.debug("给{}手机发送验证短信，验证码{}成功", smsValidateCode.getMobile(), smsValidateCode.getCode());
        }else {
            log.debug("给{}手机发送验证短信，验证码{}失败", smsValidateCode.getMobile(), smsValidateCode.getCode());
            return new SimpleResponse(String.valueOf(HttpStatus.OK.value()), "短信发送失败");
        }
        sessionStrategy.setAttribute(new ServletWebRequest(request), SMS_CODE_MOBILE_PREFIX + mobile, smsValidateCode);
        SimpleResponse simpleResponse = new SimpleResponse(String.valueOf(HttpStatus.OK.value()), "短信发送成功");
        return simpleResponse;
    }
}
