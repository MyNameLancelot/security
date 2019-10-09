package com.kun.security.app.config.provider;

import com.kun.security.app.controller.ImageCodeController;
import com.kun.security.app.controller.SmsCodeController;
import com.kun.security.core.CoreSecurityProperties;
import com.kun.security.core.conf.PermissionConfigProvider;
import com.kun.security.core.controller.SecurityBrowserConstants;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

public class AppPermissionConfigProvider implements PermissionConfigProvider {

    private static Logger log = LoggerFactory.getLogger(AppPermissionConfigProvider.class);

    private ApplicationContext applicationContext;

    private CoreSecurityProperties coreSecurityProperties;

    public AppPermissionConfigProvider(ApplicationContext applicationContext, CoreSecurityProperties coreSecurityProperties) {
        this.applicationContext = applicationContext;
        this.coreSecurityProperties = coreSecurityProperties;
    }

    @Override
    public void configure(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionInterceptUrlRegistry) {
        log.debug("=================APP模块权限设置开始=================");
        // 图片验证码url
        if(ArrayUtils.isNotEmpty(applicationContext.getBeanNamesForType(ImageCodeController.class))){
            expressionInterceptUrlRegistry.antMatchers(HttpMethod.GET, SecurityBrowserConstants.VALIDATE_IMAGE_URL).permitAll();
            log.debug("图片验证码访问放开");
        }
        // 短信验证码url
        if(ArrayUtils.isNotEmpty(applicationContext.getBeanNamesForType(SmsCodeController.class))){
            expressionInterceptUrlRegistry.antMatchers(HttpMethod.GET, SecurityBrowserConstants.VALIDATE_SMS_URL).permitAll();
            expressionInterceptUrlRegistry.antMatchers(HttpMethod.POST, coreSecurityProperties.getSmsAuthenticationUrl()).permitAll();
            log.debug("短信验证码访问放开");
        }
        // 不需要限制的请求
        expressionInterceptUrlRegistry.antMatchers(HttpMethod.POST, coreSecurityProperties.getAuthenticationUrl()).permitAll();
        log.debug("登陆请求访问放开");
        // register URL路径可访问
        if(coreSecurityProperties != null && coreSecurityProperties.getRegisterHtmlUrl() != null) {
            expressionInterceptUrlRegistry.antMatchers(HttpMethod.GET, coreSecurityProperties.getRegisterHtmlUrl()).permitAll();
            expressionInterceptUrlRegistry.antMatchers(HttpMethod.POST, coreSecurityProperties.getRegisterPostUrl()).permitAll();
            log.debug("注册信息请求访问放开");
        }
        log.debug("=================APP模块权限设置结束=================");
    }
}