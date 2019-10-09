package com.kun.config;

import com.kun.security.core.conf.PermissionConfigProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

@Component
public class DemoPermissionConfigProvider  implements PermissionConfigProvider {
    @Override
    public void configure(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionInterceptUrlRegistry) {
//        expressionInterceptUrlRegistry.antMatchers("/user/add").hasAnyAuthority("ADMIN");
    }
}
