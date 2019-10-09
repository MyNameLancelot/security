package com.kun.security.app.config;

import com.kun.security.app.controller.SmsCodeController;
import com.kun.security.app.filter.ImageValidateCodeFilter;
import com.kun.security.app.filter.SmsValidateCodeFilter;
import com.kun.security.app.oauth2.OpenIdSecurityConfig;
import com.kun.security.core.CoreSecurityProperties;
import com.kun.security.core.conf.PermissionConfigManager;
import com.kun.security.core.conf.SmsSecurityConfig;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.social.security.SpringSocialConfigurer;

import java.util.Set;

/**
 * 访问资源权限设置
 */
@Configuration
public class AppResourceServerConfigurer extends ResourceServerConfigurerAdapter {

    @Autowired
    private PermissionConfigManager permissionConfigManager;

    @Autowired
    private CoreSecurityProperties coreSecurityProperties;

    @Autowired(required = false)
    private ImageValidateCodeFilter imageValidateCodeFilter;

    @Autowired(required = false)
    private SmsValidateCodeFilter smsValidateCodeFilter;

    @Autowired(required = false)
    private SmsSecurityConfig smsSecurityConfig;

    @Autowired(required = false)
    private OpenIdSecurityConfig openIdSecurityConfig;

    @Autowired(required = false)
    private Set<SpringSocialConfigurer> springSocialConfigurers;

    /**
     * 注入密码加密器，WebSecurityConfigurerAdapter会扫描Context中的所有在PasswordEncoder，进行使用
     */
    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 登陆成功和失败处理器，用于统计信息和返回成功/失败JSON回复
     */
    @Autowired
    private AuthenticationSuccessHandler successHandler;
    @Autowired
    private AuthenticationFailureHandler failureHandler;

    // 用户验证服务
    @Autowired
    @Qualifier(value = "securityUserServiceImpl")
    private UserDetailsService userDetailsService;

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 登陆、安全拦截配置
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        if(imageValidateCodeFilter != null) {
            http.addFilterBefore(imageValidateCodeFilter, UsernamePasswordAuthenticationFilter.class);
        }
        if(smsValidateCodeFilter != null) {
            http.addFilterBefore(smsValidateCodeFilter, UsernamePasswordAuthenticationFilter.class);
        }
        http.userDetailsService(userDetailsService);
        //表单登陆功能
        FormLoginConfigurer<HttpSecurity> httpSecurityFormLoginConfigurer = http.formLogin(); //基于表单验证
        httpSecurityFormLoginConfigurer.loginPage(coreSecurityProperties.getLoginPageUrl());
        httpSecurityFormLoginConfigurer.loginProcessingUrl(coreSecurityProperties.getAuthenticationUrl());
        httpSecurityFormLoginConfigurer.successHandler(successHandler);
        httpSecurityFormLoginConfigurer.failureHandler(failureHandler);

        // 其余请求需要验证
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionInterceptUrlRegistry = http.authorizeRequests();
        permissionConfigManager.managerSecurityConfigProvider(expressionInterceptUrlRegistry);

        // 第三方登陆,简化模式使用openid和providerId登陆
        if(openIdSecurityConfig != null) {
            http.apply(openIdSecurityConfig);
        }

        // 第三方登陆
        if(springSocialConfigurers != null) {
            for (SpringSocialConfigurer springSocialConfigurer : springSocialConfigurers) {
                http.apply(springSocialConfigurer);
            }
        }

        //暂时屏蔽csrf安全问题
        http.csrf().disable();

        // 应用短信过滤器等
        if(ArrayUtils.isNotEmpty(applicationContext.getBeanNamesForType(SmsCodeController.class))){
            http.apply(smsSecurityConfig);
        }
    }
}
