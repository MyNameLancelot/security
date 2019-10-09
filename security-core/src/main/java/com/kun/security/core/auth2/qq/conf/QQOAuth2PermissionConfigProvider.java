package com.kun.security.core.auth2.qq.conf;

import com.kun.security.core.auth2.qq.QQSocialProperties;
import com.kun.security.core.conf.PermissionConfigProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "qq.social", name = "enable")
public class QQOAuth2PermissionConfigProvider implements PermissionConfigProvider {

    @Autowired
    private QQSocialProperties qqSocialProperties;

    @Override
    public void configure(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionInterceptUrlRegistry) {
        expressionInterceptUrlRegistry.antMatchers(HttpMethod.GET, qqSocialProperties.getCallBackUri() + "/" + qqSocialProperties.getProviderId()).permitAll();
    }
}
