package com.kun.security.browser.config.provider;

import com.kun.security.browser.config.properties.BrowserSecurityLogoutProperties;
import com.kun.security.browser.config.properties.BrowserSessionManagerProperties;
import com.kun.security.browser.controller.ImageCodeController;
import com.kun.security.browser.controller.SmsCodeController;
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

/**
 * 游览器端权限设置
 */
public class BrowserPermissionConfigProvider implements PermissionConfigProvider {

    private static Logger log = LoggerFactory.getLogger(BrowserPermissionConfigProvider.class);

    private ApplicationContext applicationContext;

    private CoreSecurityProperties coreSecurityProperties;

    private BrowserSecurityLogoutProperties browserSecurityLogoutProperties;

    private BrowserSessionManagerProperties browserSessionManagerProperties;

    public BrowserPermissionConfigProvider(ApplicationContext applicationContext,
                                           CoreSecurityProperties coreSecurityProperties,
                                           BrowserSecurityLogoutProperties browserSecurityLogoutProperties,
                                           BrowserSessionManagerProperties browserSessionManagerProperties) {
        this.applicationContext = applicationContext;
        this.coreSecurityProperties = coreSecurityProperties;
        this.browserSecurityLogoutProperties = browserSecurityLogoutProperties;
        this.browserSessionManagerProperties = browserSessionManagerProperties;
    }

    @Override
    public void configure(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionInterceptUrlRegistry) {
        log.debug("=================Browser模块权限设置开始=================");
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
        expressionInterceptUrlRegistry.antMatchers(HttpMethod.GET, coreSecurityProperties.getLoginPageUrl(), coreSecurityProperties.getLoginHtmlUrl()).permitAll();
        log.debug("登陆页面访问放开");
        expressionInterceptUrlRegistry.antMatchers(HttpMethod.POST, coreSecurityProperties.getAuthenticationUrl()).permitAll();
        log.debug("登陆请求访问放开");
        // sessionInvalid路径可访问
        if(browserSessionManagerProperties != null && browserSessionManagerProperties.getInvalidSessionUrl() != null) {
            expressionInterceptUrlRegistry.antMatchers(HttpMethod.GET, browserSessionManagerProperties.getInvalidSessionUrl()).permitAll();
            log.debug("session失效访问放开");
        }
        // logoutSuccessURL路径可访问
        if(browserSecurityLogoutProperties != null && browserSecurityLogoutProperties.getLogoutSuccessUrl() != null) {
            expressionInterceptUrlRegistry.antMatchers(HttpMethod.GET, browserSecurityLogoutProperties.getLogoutSuccessUrl()).permitAll();
            log.debug("注销账户访问放开");
        }
        // register URL路径可访问
        if(coreSecurityProperties != null && coreSecurityProperties.getRegisterHtmlUrl() != null) {
            expressionInterceptUrlRegistry.antMatchers(HttpMethod.GET, coreSecurityProperties.getRegisterHtmlUrl()).permitAll();
            expressionInterceptUrlRegistry.antMatchers(HttpMethod.POST, coreSecurityProperties.getRegisterPostUrl()).permitAll();
            log.debug("注册账户访问放开");
        }
    }
}