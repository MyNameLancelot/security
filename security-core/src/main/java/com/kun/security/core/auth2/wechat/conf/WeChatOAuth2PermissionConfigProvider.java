package com.kun.security.core.auth2.wechat.conf;

import com.kun.security.core.auth2.wechat.WeChatSocialProperties;
import com.kun.security.core.conf.PermissionConfigProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "wechat.social", name = "enable")
public class WeChatOAuth2PermissionConfigProvider implements PermissionConfigProvider {

    @Autowired
    private WeChatSocialProperties WeChatSocialProperties;

    @Override
    public void configure(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionInterceptUrlRegistry) {
        expressionInterceptUrlRegistry.antMatchers(HttpMethod.GET, WeChatSocialProperties.getCallBackUri() + "/" + WeChatSocialProperties.getProviderId()).permitAll();
    }
}
