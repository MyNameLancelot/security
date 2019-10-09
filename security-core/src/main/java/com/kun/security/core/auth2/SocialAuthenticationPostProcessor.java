package com.kun.security.core.auth2;

import org.springframework.social.security.SocialAuthenticationFilter;

public interface SocialAuthenticationPostProcessor {
    void process(SocialAuthenticationFilter socialAuthenticationFilter);
}
