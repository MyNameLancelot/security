package com.kun.security.app.oauth2.processor;

import com.kun.security.core.CoreSecurityProperties;
import com.kun.security.core.auth2.SocialAuthenticationPostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.social.security.SocialAuthenticationFilter;

/**
 * 社交登陆设置后置拦截器，spring默认是跳转页面，但是app应该是返回token和为注册跳转保存当前connect信息
 */
public class SocialAuthenticationPostProcessorImpl implements SocialAuthenticationPostProcessor {

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private CoreSecurityProperties coreSecurityProperties;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void process(SocialAuthenticationFilter socialAuthenticationFilter) {
        socialAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        socialAuthenticationFilter.setAuthenticationFailureHandler(new AppSocialAuthenticationFailureHandler(redisTemplate,new SimpleUrlAuthenticationFailureHandler(coreSecurityProperties.getRegisterHtmlUrl())));
    }
}
