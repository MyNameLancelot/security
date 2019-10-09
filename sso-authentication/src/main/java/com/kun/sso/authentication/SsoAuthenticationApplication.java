package com.kun.sso.authentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@SpringBootApplication
public class SsoAuthenticationApplication {

    @Bean
    public JwtTokenStore tokenStore(JwtAccessTokenConverter jwtTokenEnhancer) {
        return new JwtTokenStore(jwtTokenEnhancer);
    }

    /**
     * 注入密码加密器，WebSecurityConfigurerAdapter会扫描Context中的所有在PasswordEncoder，进行使用
     */
    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey("key");
        return jwtAccessTokenConverter;
    }

    public static void main(String[] args) {
        SpringApplication.run(SsoAuthenticationApplication.class, args);
    }
}
