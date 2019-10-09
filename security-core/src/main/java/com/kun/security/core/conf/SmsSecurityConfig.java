package com.kun.security.core.conf;

import com.kun.security.core.CoreSecurityProperties;
import com.kun.security.core.authentication.filter.SmsAuthenticationFilter;
import com.kun.security.core.provider.SmsCodeAuthenticationProvider;
import com.kun.security.core.service.AbstractUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class SmsSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    private AuthenticationSuccessHandler successHandler;
    @Autowired
    private AuthenticationFailureHandler failureHandler;
    @Autowired
    private AbstractUserService userService;
    @Autowired
    private CoreSecurityProperties coreSecurityProperties;

    @Override
    public void configure(HttpSecurity http) {
        // 设置过滤器
        SmsAuthenticationFilter smsAuthenticationFilter = new SmsAuthenticationFilter();
        smsAuthenticationFilter.setFilterProcessesUrl(coreSecurityProperties.getSmsAuthenticationUrl());
        smsAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        smsAuthenticationFilter.setAuthenticationSuccessHandler(successHandler);
        smsAuthenticationFilter.setAuthenticationFailureHandler(failureHandler);

        SmsCodeAuthenticationProvider smsCodeAuthenticationProvider = new SmsCodeAuthenticationProvider();
        smsCodeAuthenticationProvider.setUserService(userService);

        http.authenticationProvider(smsCodeAuthenticationProvider)
            .addFilterAfter(smsAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    }
}
