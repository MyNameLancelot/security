package com.kun.security.app.filter;

import com.kun.security.app.controller.SmsCodeController;
import com.kun.security.core.CoreSecurityProperties;
import com.kun.security.core.domain.SmsValidateCode;
import com.kun.security.core.exception.ValidateCodeException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 短信验证码拦截匹配器
 */
public class SmsValidateCodeFilter extends OncePerRequestFilter {

    private AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private CoreSecurityProperties coreSecurityProperties;

    @Autowired
    private RedisTemplate redisTemplate;

    public SmsValidateCodeFilter(AuthenticationFailureHandler authenticationFailureHandler) {
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 是登陆请求而且为POST
        if(StringUtils.equals(coreSecurityProperties.getSmsAuthenticationUrl(), request.getRequestURI()) && StringUtils.equalsIgnoreCase(request.getMethod(), "POST")) {
            try {
                validate(new ServletWebRequest(request));
                filterChain.doFilter(request, response);
            } catch (ValidateCodeException e) {
                authenticationFailureHandler.onAuthenticationFailure(request, response, e);
            }
        }else {
            filterChain.doFilter(request, response);
        }
    }

    private void validate(ServletWebRequest servletWebRequest) {

        String questInMobile = null;
        String questInCode   = null;
        String questInDevice = null;

        try {
            questInMobile = ServletRequestUtils.getRequiredStringParameter(servletWebRequest.getRequest(), "mobile");
            questInCode = ServletRequestUtils.getRequiredStringParameter(servletWebRequest.getRequest(), "code");
            questInDevice = ServletRequestUtils.getRequiredStringParameter(servletWebRequest.getRequest(), "device");
        }catch (ServletRequestBindingException e) {
            throw new ValidateCodeException("提交的短信验证码参数缺失");
        }

        String keyName = SmsCodeController.SMS_CODE_MOBILE_PREFIX + questInDevice + ":" +questInMobile;
        SmsValidateCode smsValidateCode = (SmsValidateCode) redisTemplate.boundValueOps(keyName).get();

        if(StringUtils.isEmpty(questInMobile)) {
            throw new ValidateCodeException("提交的手机号不能为空");
        }

        if(StringUtils.isEmpty(questInCode)) {
            throw new ValidateCodeException("提交的短信验证码不能为空");
        }

        if(smsValidateCode == null) {
            // 过期判断由Redis控制,Redis设置了过期时间，程序不判断过期行为
            throw new ValidateCodeException("手机号" + questInMobile + "短信验证码不存在或已过期");
        }

        if(!StringUtils.equalsIgnoreCase(questInCode, smsValidateCode.getCode())) {
            throw new ValidateCodeException("验证码不匹配");
        }

        // 匹配成功
        redisTemplate.delete(keyName);
    }
}
