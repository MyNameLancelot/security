package com.kun.security.browser.config;

import com.kun.security.browser.config.properties.BrowserSecurityLogoutProperties;
import com.kun.security.browser.config.properties.BrowserSecurityRememberMeProperties;
import com.kun.security.browser.config.properties.BrowserSessionManagerProperties;
import com.kun.security.browser.controller.SmsCodeController;
import com.kun.security.browser.filter.ImageValidateCodeFilter;
import com.kun.security.browser.filter.SmsValidateCodeFilter;
import com.kun.security.core.CoreSecurityProperties;
import com.kun.security.core.conf.PermissionConfigManager;
import com.kun.security.core.conf.SmsSecurityConfig;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.social.security.SpringSocialConfigurer;

import java.util.Set;

/**
 * 游览器端安全配置
 */
@Configuration
@EnableConfigurationProperties({BrowserSecurityRememberMeProperties.class, BrowserSessionManagerProperties.class, BrowserSecurityLogoutProperties.class})
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PermissionConfigManager permissionConfigManager;

    @Autowired
    private CoreSecurityProperties coreSecurityProperties;

    @Autowired
    private BrowserSecurityRememberMeProperties browserSecurityRememberMeProperties;

    @Autowired
    private BrowserSessionManagerProperties browserSessionManagerProperties;

    @Autowired
    private BrowserSecurityLogoutProperties browserSecurityLogoutProperties;

    @Autowired(required = false)
    private ImageValidateCodeFilter imageValidateCodeFilter;

    @Autowired(required = false)
    private SmsValidateCodeFilter smsValidateCodeFilter;

    @Autowired(required = false)
    private SmsSecurityConfig smsSecurityConfig;

    @Autowired(required = false)
    private PersistentTokenRepository persistentTokenRepository;

    @Autowired(required = false)
    private SessionInformationExpiredStrategy sessionInformationExpiredStrategy;

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

    @Autowired(required = false)
    private Set<SpringSocialConfigurer> springSocialConfigurers;

    /**
     * 登陆、安全拦截配置
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
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

        //记住我功能
        if(Boolean.TRUE.equals(browserSecurityRememberMeProperties.getRememberMe())) {
            RememberMeConfigurer<HttpSecurity> httpSecurityRememberMeConfigurer = http.rememberMe();
            httpSecurityRememberMeConfigurer.userDetailsService(userDetailsService);
            httpSecurityRememberMeConfigurer.tokenValiditySeconds(browserSecurityRememberMeProperties.getRememberMeValiditySeconds());
            httpSecurityRememberMeConfigurer.rememberMeParameter(browserSecurityRememberMeProperties.getRememberMeParameter());
            httpSecurityRememberMeConfigurer.tokenRepository(persistentTokenRepository);
        }

        //session管理
        if(browserSessionManagerProperties != null) {
            SessionManagementConfigurer<HttpSecurity> httpSecuritySessionManagementConfigurer = http.sessionManagement();
            if(browserSessionManagerProperties.getInvalidSessionUrl() != null) {
                httpSecuritySessionManagementConfigurer.invalidSessionUrl(browserSessionManagerProperties.getInvalidSessionUrl());
            }
            if(browserSessionManagerProperties.getMaximumSessions() != null) {
                //只允许用户登陆个数，如果为一则只允许一个用户登录
                SessionManagementConfigurer<HttpSecurity>.ConcurrencyControlConfigurer concurrencyControlConfigurer = httpSecuritySessionManagementConfigurer.maximumSessions(browserSessionManagerProperties.getMaximumSessions());
                //true:否允许在别处登陆，挤掉当前账户
                //false:不允许在别处登陆
                if(browserSessionManagerProperties.getMaxSessionsPreventsLogin() != null) {
                    concurrencyControlConfigurer.maxSessionsPreventsLogin(browserSessionManagerProperties.getMaxSessionsPreventsLogin());
                    //重复登陆提示
                    concurrencyControlConfigurer.expiredSessionStrategy(sessionInformationExpiredStrategy);
                }
            }
        }

        // 注销功能
        if(browserSecurityLogoutProperties != null) {
            LogoutConfigurer<HttpSecurity> logoutConfigurer = http.logout();
            logoutConfigurer.logoutUrl(browserSecurityLogoutProperties.getLogoutUrl());
            if(browserSecurityLogoutProperties.getLogoutSuccessUrl() != null) {
                logoutConfigurer.logoutSuccessUrl(browserSecurityLogoutProperties.getLogoutSuccessUrl());
            }

            if(browserSecurityLogoutProperties.getDeleteCookies() != null) {
                logoutConfigurer.deleteCookies(browserSecurityLogoutProperties.getDeleteCookies());
            }
        }

        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionInterceptUrlRegistry = http.authorizeRequests();
        permissionConfigManager.managerSecurityConfigProvider(expressionInterceptUrlRegistry);

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
