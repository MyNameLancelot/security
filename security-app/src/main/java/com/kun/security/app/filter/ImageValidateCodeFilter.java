package com.kun.security.app.filter;

import com.kun.security.app.controller.ImageCodeController;
import com.kun.security.core.CoreSecurityProperties;
import com.kun.security.core.domain.ImageValidateCode;
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
 * 图片验证码拦截匹配器
 */
public class ImageValidateCodeFilter extends OncePerRequestFilter {

    @Autowired
    private CoreSecurityProperties coreSecurityProperties;

    private AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private RedisTemplate redisTemplate;

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
        String questInDevice = null;
        String questInCode   = null;
        try {
            questInDevice = ServletRequestUtils.getRequiredStringParameter(servletWebRequest.getRequest(), "device");
            questInCode   = ServletRequestUtils.getStringParameter(servletWebRequest.getRequest(),"imageCode");
        }catch (ServletRequestBindingException e) {
            throw new ValidateCodeException("提交的图片验证码参数缺失");
        }

        String keyName = ImageCodeController.REDIS_IMAGE_VERIFY_CODE + questInDevice;
        ImageValidateCode codeInRedis = (ImageValidateCode)redisTemplate.boundValueOps(keyName).get();

        if(codeInRedis == null) {
            // 过期判断由Redis控制,Redis设置了过期时间，程序不判断过期行为
            throw new ValidateCodeException("验证码不存在，或已过期");
        }
        if(!StringUtils.equalsIgnoreCase(questInCode, codeInRedis.getCode())) {
            throw new ValidateCodeException("验证码不匹配");
        }
        // 匹配成功
        redisTemplate.delete(keyName);
    }
}
