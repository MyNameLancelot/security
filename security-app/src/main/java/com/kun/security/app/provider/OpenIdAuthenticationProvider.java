package com.kun.security.app.provider;

import com.kun.security.app.authentication.token.OpenIdCodeAuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.security.SocialUserDetailsService;

import java.util.HashSet;
import java.util.Set;


/**
 * OpenId验证插拔器
 */
public class OpenIdAuthenticationProvider implements AuthenticationProvider {

    private static Logger log = LoggerFactory.getLogger(OpenIdAuthenticationProvider.class);

    private UsersConnectionRepository usersConnectionRepository;

    private SocialUserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.debug("根据openId:{}和providerId{}查找用户", authentication.getPrincipal(),authentication.getCredentials());
        Set<String> providerUserIds = new HashSet<>(1);
        providerUserIds.add((String)authentication.getPrincipal());
        Set<String> userIdsConnectedTo = usersConnectionRepository.findUserIdsConnectedTo((String) authentication.getCredentials(), providerUserIds);
        if(userIdsConnectedTo.isEmpty()) {
            log.error("根据openId:{}和providerId{}未查找用户", authentication.getPrincipal(),authentication.getCredentials());
            throw new ProviderNotFoundException("根据openId" + authentication.getPrincipal() + "和providerId" + authentication.getCredentials() + "未查找用户");
        }
        if(userIdsConnectedTo.size() != 1) {
            log.error("根据openId:{}和providerId{}找到多个用户", authentication.getPrincipal(),authentication.getCredentials());
            throw new ProviderNotFoundException("根据openId" + authentication.getPrincipal() + "和providerId" + authentication.getCredentials() + "找到多个用户");
        }
        String loadUserId = null;
        for (String userId : userIdsConnectedTo) {
            loadUserId = userId;
        }
        UserDetails user = userDetailsService.loadUserByUserId(loadUserId);
        return new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword(),user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return  OpenIdCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public void setUsersConnectionRepository(UsersConnectionRepository usersConnectionRepository) {
        this.usersConnectionRepository = usersConnectionRepository;
    }

    public void setUserDetailsService(SocialUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
}
