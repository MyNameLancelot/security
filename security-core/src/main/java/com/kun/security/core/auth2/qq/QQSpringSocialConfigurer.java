package com.kun.security.core.auth2.qq;

import com.kun.security.core.CoreSecurityProperties;
import com.kun.security.core.auth2.AuthenticationIdUserIdSource;
import com.kun.security.core.auth2.SocialAuthenticationPostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.social.security.SpringSocialConfigurer;

@ConditionalOnProperty(prefix = "qq.social", name = "enable")
@EnableConfigurationProperties(QQSocialProperties.class)
@Configuration
@EnableSocial
public class QQSpringSocialConfigurer extends SpringSocialConfigurer {

    private QQSocialProperties qqSocialProperties;

    @Autowired(required = false)
    private SocialAuthenticationPostProcessor socialAuthenticationPostProcessor;

    public QQSpringSocialConfigurer(QQSocialProperties qqSocialProperties, CoreSecurityProperties coreSecurityProperties) {
        this.qqSocialProperties = qqSocialProperties;
        super.userIdSource(new AuthenticationIdUserIdSource());
        if(coreSecurityProperties.getRegisterHtmlUrl() != null) {
            super.signupUrl(coreSecurityProperties.getRegisterHtmlUrl());
        }
    }

    @Override
    protected <T> T postProcess(T object) {
        SocialAuthenticationFilter filter = (SocialAuthenticationFilter)super.postProcess(object);
        filter.setFilterProcessesUrl(qqSocialProperties.getCallBackUri());;
        // APP模块时下方配置有用
        if(socialAuthenticationPostProcessor !=null ) {
            socialAuthenticationPostProcessor.process(filter);
        }
        return (T) filter;
    }
}
