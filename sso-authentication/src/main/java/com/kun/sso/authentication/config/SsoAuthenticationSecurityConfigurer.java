package com.kun.sso.authentication.config;

import com.kun.security.core.service.impl.SecurityUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class SsoAuthenticationSecurityConfigurer extends WebSecurityConfigurerAdapter {

    // 需要设置
    @Autowired
    @Qualifier(value = "securityUserServiceImpl")
    private UserDetailsService userDetailsService;

    // 需要设置
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http.userDetailsService(userDetailsService);
    }

    @Bean
    public UserDetailsService  securityUserServiceImpl() {
        return new SecurityUserServiceImpl();
    }
}