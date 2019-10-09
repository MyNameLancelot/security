package com.kun.security.core.auth2.wechat;

import com.kun.security.core.CoreSecurityProperties;
import com.kun.security.core.auth2.AuthenticationIdUserIdSource;
import com.kun.security.core.auth2.SocialAuthenticationPostProcessor;
import com.kun.security.core.auth2.qq.QQSocialProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.social.security.SpringSocialConfigurer;

@ConditionalOnProperty(prefix = "wechat.social", name = "enable")
@EnableConfigurationProperties(QQSocialProperties.class)
@Configuration
@EnableSocial
public class WeChatSpringSocialConfigurer extends SpringSocialConfigurer {

    private WeChatSocialProperties weChatSocialProperties;

    @Autowired(required = false)
    private SocialAuthenticationPostProcessor socialAuthenticationPostProcessor;

    public WeChatSpringSocialConfigurer(WeChatSocialProperties weChatSocialProperties, CoreSecurityProperties coreSecurityProperties) {
        this.weChatSocialProperties = weChatSocialProperties;
        super.userIdSource(new AuthenticationIdUserIdSource());
        if(coreSecurityProperties.getRegisterHtmlUrl() != null) {
            super.signupUrl(coreSecurityProperties.getRegisterHtmlUrl());
        }
    }

    @Override
    protected <T> T postProcess(T object) {
        SocialAuthenticationFilter filter = (SocialAuthenticationFilter)super.postProcess(object);
        filter.setFilterProcessesUrl(weChatSocialProperties.getCallBackUri());
        if(socialAuthenticationPostProcessor !=null ) {
            socialAuthenticationPostProcessor.process(filter);
        }
        return (T) filter;
    }
}
