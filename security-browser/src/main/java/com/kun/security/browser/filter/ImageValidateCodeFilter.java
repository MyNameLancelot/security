package com.kun.security.browser.filter;

import com.kun.security.browser.controller.ImageCodeController;
import com.kun.security.core.CoreSecurityProperties;
import com.kun.security.core.domain.ImageValidateCode;
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
 * 图片验证码拦截匹配器
 */
public class ImageValidateCodeFilter extends OncePerRequestFilter {

    @Autowired
    private CoreSecurityProperties coreSecurityProperties;

    private AuthenticationFailureHandler authenticationFailureHandler;

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    public ImageValidateCodeFilter(AuthenticationFailureHandler authenticationFailureHandler) {
        this.authenticationFailureHandler = authenticationFailureHandler;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 是登陆请求而且为POST
        if(StringUtils.equals(coreSecurityProperties.getAuthenticationUrl(), request.getRequestURI()) && StringUtils.equalsIgnoreCase(request.getMethod(), "POST")) {
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

    private void validate(ServletWebRequest servletWebRequest) throws ServletRequestBindingException{
        ImageValidateCode codeInSession = (ImageValidateCode) sessionStrategy.getAttribute(servletWebRequest, ImageCodeController.SESSION_IMAGE_VERIFY_CODE);
        String codeInRequest = ServletRequestUtils.getStringParameter(servletWebRequest.getRequest(),"imageCode");

        if(StringUtils.isEmpty(codeInRequest)) {
            throw new ValidateCodeException("提交的验证码不能为空");
        }

        if(codeInSession == null) {
            throw new ValidateCodeException("验证码不存在");
        }
        if(codeInSession.compareExpired()) {
            sessionStrategy.removeAttribute(servletWebRequest, ImageCodeController.SESSION_IMAGE_VERIFY_CODE);
            throw new ValidateCodeException("验证码已过期");
        }


        if(!StringUtils.equalsIgnoreCase(codeInRequest, codeInSession.getCode())) {
            throw new ValidateCodeException("验证码不匹配");
        }

        // 匹配成功
        sessionStrategy.removeAttribute(servletWebRequest, ImageCodeController.SESSION_IMAGE_VERIFY_CODE);
    }
}
