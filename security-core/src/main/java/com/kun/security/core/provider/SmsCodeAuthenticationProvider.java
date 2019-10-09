package com.kun.security.core.provider;

import com.kun.security.core.authentication.token.SmsCodeAuthenticationToken;
import com.kun.security.core.service.AbstractUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;


/**
 * 手机短信验证插拔器
 */
public class SmsCodeAuthenticationProvider implements AuthenticationProvider {

    private Logger log = LoggerFactory.getLogger(SmsCodeAuthenticationProvider.class);

    private AbstractUserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.debug("根据手机号{}查找用户", authentication.getPrincipal());

        SmsCodeAuthenticationToken smsCodeAuthenticationToken =  (SmsCodeAuthenticationToken) authentication;
        UserDetails user = userService.getUserByMobile((String) smsCodeAuthenticationToken.getPrincipal());

        return new SmsCodeAuthenticationToken(user.getUsername(),user.getPassword(),user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return  SmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public void setUserService(AbstractUserService userService) {
        this.userService = userService;
    }
}
