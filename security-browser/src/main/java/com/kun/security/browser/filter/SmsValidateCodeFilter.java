package com.kun.security.browser.filter;

import com.kun.security.browser.controller.SmsCodeController;
import com.kun.security.core.CoreSecurityProperties;
import com.kun.security.core.domain.SmsValidateCode;
import com.kun.security.core.exception.ValidateCodeException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
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

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

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

        String questInMobile =  null;
        String questInCode =  null;

        try {
            questInMobile = ServletRequestUtils.getRequiredStringParameter(servletWebRequest.getRequest(), "mobile");
            questInCode = ServletRequestUtils.getRequiredStringParameter(servletWebRequest.getRequest(), "code");
        }catch (ServletRequestBindingException e) {
            throw new ValidateCodeException("提交的短信验证码不能为空");
        }

        SmsValidateCode smsValidateCode = (SmsValidateCode)sessionStrategy.getAttribute(servletWebRequest, SmsCodeController.SMS_CODE_MOBILE_PREFIX + questInMobile);

        if(StringUtils.isEmpty(questInMobile)) {
            throw new ValidateCodeException("提交的手机号不能为空");
        }

        if(StringUtils.isEmpty(questInCode)) {
            throw new ValidateCodeException("提交的短信验证码不能为空");
        }

        if(smsValidateCode == null) {
            throw new ValidateCodeException("手机号" + questInMobile + "短信验证码不存在");
        }
        if(smsValidateCode.compareExpired()) {
            throw new ValidateCodeException("短信验证码已过期");
        }

        if(!StringUtils.equalsIgnoreCase(questInCode, smsValidateCode.getCode())) {
            throw new ValidateCodeException("验证码不匹配");
        }
    }
}
