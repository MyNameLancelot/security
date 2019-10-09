package com.kun.security.core.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

import java.util.Set;

/**
 * 加载所有权限信息的处理类
 */
public class DefaultSecurityConfigManager implements PermissionConfigManager{

    @Autowired
    private Set<PermissionConfigProvider> permissionConfigProviders;

    @Override
    public void managerSecurityConfigProvider(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionInterceptUrlRegistry) {
        // 调用所有的permissionConfigProvider配置所有模块权限信息
        for (PermissionConfigProvider permissionConfigProvider : permissionConfigProviders) {
            permissionConfigProvider.configure(expressionInterceptUrlRegistry);
        }
        otherAnyRequestDispose(expressionInterceptUrlRegistry);
    }

    //默认其余所有请求都需要验证，可覆盖处理其它请求
    @Override
    public void otherAnyRequestDispose(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry expressionInterceptUrlRegistry) {
        expressionInterceptUrlRegistry.anyRequest().authenticated();
    }
}
