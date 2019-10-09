package com.kun.security.core.auth2;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.UserIdSource;
import org.springframework.social.security.SocialUserDetails;

public class AuthenticationIdUserIdSource implements UserIdSource {
    @Override
    public String getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new IllegalStateException("Unable to get a ConnectionRepository: no user signed in");
        }
        return ((SocialUserDetails)authentication.getPrincipal()).getUserId();
    }
}
