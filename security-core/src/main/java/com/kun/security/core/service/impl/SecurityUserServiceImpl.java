package com.kun.security.core.service.impl;

import com.kun.security.core.exception.AuthenticationException;
import com.kun.security.core.service.AbstractOAUth2UserService;
import com.kun.security.core.service.AbstractUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;

/**
 * 按需实现 UserDetailsService【必须】、SocialUserDetails【如果需要第三方登陆】 接口，用户获取用户登陆信息
 */
public class SecurityUserServiceImpl implements UserDetailsService, SocialUserDetailsService {

    private static Logger log = LoggerFactory.getLogger(SecurityUserServiceImpl.class);

    @Autowired
    private AbstractUserService userService;

    @Autowired(required = false)
    private AbstractOAUth2UserService oauth2UserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("根据用户名{}查找用户", username);
        // 查询user
        UserDetails user = userService.getUserByUserName(username);
        // 用户不存在
        if (user== null) {
            throw new UsernameNotFoundException( "用户名" + username + "账户不存在");
        }
        return user;
    }

    @Override
    public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
        if(oauth2UserService == null) {
            throw new AuthenticationException("不支持OAuth2登陆");
        }
        log.debug("根据用户ID{}查找用户", userId);
        // 查询user
        Object user = oauth2UserService.getUserByUserId(userId);
        // 用户不存在
        if (user== null) {
            throw new UsernameNotFoundException( "用户ID【" + userId + "】账户不存在");
        }
        if( user instanceof SocialUserDetails) {
            return (SocialUserDetails)user;
        }
        throw new ClassCastException("用户类必须实现SocialUserDetails接口");
    }
}
