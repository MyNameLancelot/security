package com.kun.security.app.oauth2;

import com.kun.security.app.authentication.filter.OpenIdAuthenticationFilter;
import com.kun.security.app.config.properties.AppOAuth2Properties;
import com.kun.security.app.provider.OpenIdAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.security.SocialUserDetailsService;

/**
 * App简化流程实现登陆
 */
public class OpenIdSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    /**
     * 登陆成功和失败处理器，用于统计信息和返回成功/失败JSON回复
     */
    @Autowired
    private AuthenticationSuccessHandler successHandler;
    
    @Autowired
    private AuthenticationFailureHandler failureHandler;
    
    @Autowired
    private SocialUserDetailsService userService;
    
    @Autowired
    private UsersConnectionRepository usersConnectionRepository;
    
    @Autowired
    private AppOAuth2Properties apOAuth2Properties;

    @Override
    public void configure(HttpSecurity http) {
        // 设置过滤器
        OpenIdAuthenticationFilter openIdAuthenticationFilter = new OpenIdAuthenticationFilter();
        if(apOAuth2Properties.getOpenIdLoginUrl() != null) {
            openIdAuthenticationFilter.setFilterProcessesUrl(apOAuth2Properties.getOpenIdLoginUrl());
        }
        openIdAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        openIdAuthenticationFilter.setAuthenticationSuccessHandler(successHandler);
        openIdAuthenticationFilter.setAuthenticationFailureHandler(failureHandler);

        OpenIdAuthenticationProvider openIdAuthenticationProvider = new OpenIdAuthenticationProvider();
        openIdAuthenticationProvider.setUserDetailsService(userService);
        openIdAuthenticationProvider.setUsersConnectionRepository(usersConnectionRepository);

        http.authenticationProvider(openIdAuthenticationProvider)
            .addFilterAfter(openIdAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    }
}
