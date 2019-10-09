package com.kun.security.core;

import com.kun.security.core.auth2.ConnectStatusView;
import com.kun.security.core.auth2.ConnectView;
import com.kun.security.core.conf.DefaultSecurityConfigManager;
import com.kun.security.core.conf.PermissionConfigManager;
import com.kun.security.core.service.impl.SecurityUserServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.servlet.view.AbstractView;

@Configuration
@EnableConfigurationProperties(CoreSecurityProperties.class)
public class PermissionAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(PermissionConfigManager.class)
    public PermissionConfigManager permissionConfigManager() {
        return new DefaultSecurityConfigManager();
    }

    @Bean
    @ConditionalOnMissingBean(UserDetailsService.class)
    public SecurityUserServiceImpl securityUserServiceImpl() {
        return new SecurityUserServiceImpl();
    }

    @ConditionalOnMissingBean(name = "connect/status")
    @Bean("connect/status")
    public AbstractView connectStatusView(){
        return new ConnectStatusView();
    }

    @ConditionalOnMissingBean(name = {"connect/qqConnected", "connect/qqConnect"})
    @Bean({"connect/qqConnected", "connect/qqConnect", "connect/wechatConnected", "connect/wechatConnect"})
    public AbstractView connectView(){
        return new ConnectView();
    }
}
