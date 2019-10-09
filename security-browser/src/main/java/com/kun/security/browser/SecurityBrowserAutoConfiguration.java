package com.kun.security.browser;

import com.kun.security.browser.config.BrowserSessionInformationExpiredStrategy;
import com.kun.security.browser.config.handler.DefaultLoginFailureHandler;
import com.kun.security.browser.config.handler.DefaultLoginSuccessHandler;
import com.kun.security.browser.config.properties.BrowserSecurityLogoutProperties;
import com.kun.security.browser.config.properties.BrowserSessionManagerProperties;
import com.kun.security.browser.config.provider.BrowserPermissionConfigProvider;
import com.kun.security.browser.filter.ImageValidateCodeFilter;
import com.kun.security.browser.filter.SmsValidateCodeFilter;
import com.kun.security.core.CoreSecurityProperties;
import com.kun.security.core.conf.PermissionConfigProvider;
import com.kun.security.core.conf.SmsSecurityConfig;
import com.kun.security.core.provider.SmsCodeAuthenticationProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.security.SpringSocialConfigurer;

import javax.sql.DataSource;

@Configuration
public class SecurityBrowserAutoConfiguration {

    /**
     * 游览器端权限配置
     */
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Bean
    public PermissionConfigProvider browserPermissionConfigProvider(ApplicationContext applicationContext,
                                                                    CoreSecurityProperties coreSecurityProperties,
                                                                    BrowserSecurityLogoutProperties browserSecurityLogoutProperties,
                                                                    BrowserSessionManagerProperties browserSessionManagerProperties) {
        return new BrowserPermissionConfigProvider(applicationContext, coreSecurityProperties, browserSecurityLogoutProperties, browserSessionManagerProperties);
    }

    /**
     * 登陆失败处理器
     */
    @ConditionalOnMissingBean(AuthenticationFailureHandler.class)
    @Bean
    public AuthenticationFailureHandler defaultLoginFailureHandler() {
        return new DefaultLoginFailureHandler();
    }

    /**
     * 登陆成功处理器
     */
    @ConditionalOnMissingBean(AuthenticationSuccessHandler.class)
    @Bean
    public AuthenticationSuccessHandler defaultLoginSuccessHandler() {
        return new DefaultLoginSuccessHandler();
    }

    /**
     * 持久化-JDBC记住我功能，建表语句查看JdbcTokenRepositoryImpl.CREATE_TABLE_SQL
     */
    @Bean
    @ConditionalOnProperty(prefix = "browser.security", value = "rememberMe", havingValue = "true")
    public PersistentTokenRepository persistentTokenRepository(DataSource dataSource) {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }

    /**
     * 用户多地登陆
     */
    @Bean
    @ConditionalOnProperty(prefix="browser.session.manager", name = {"maximum-sessions", "max-sessions-prevents-login"})
    @ConditionalOnMissingBean(SessionInformationExpiredStrategy.class)
    SessionInformationExpiredStrategy browserSessionInformationExpiredStrategy() {
        return new BrowserSessionInformationExpiredStrategy();
    }

    /**
     * 获取社交账户信息
     */
    @Import(ConnectController.class)
    @ConditionalOnBean(SpringSocialConfigurer.class)
    public class SocialAutoConfigurer {
        @Bean
        public ProviderSignInUtils providerSignInUtils(ConnectionFactoryLocator connectionFactoryLocator, UsersConnectionRepository connectionRepository){
            return new ProviderSignInUtils(connectionFactoryLocator, connectionRepository);
        }
    }

    @Configuration
    @ConditionalOnProperty(prefix = "login.verify.image", value = "enable", havingValue = "true")
    public class ImageCodeAutoConfiguration{

        @Bean
        public ImageValidateCodeFilter imageValidateCodeFilter(AuthenticationFailureHandler authenticationFailureHandler) {
            return new ImageValidateCodeFilter(authenticationFailureHandler);
        }
    }

    @Configuration
    @ConditionalOnProperty(prefix="login.verify.sms", name = "enable", havingValue = "true")
    public class SmsCodeAutoConfiguration{

        @Bean
        public SmsValidateCodeFilter smsValidateCodeFilter(AuthenticationFailureHandler authenticationFailureHandler) {
            return new SmsValidateCodeFilter(authenticationFailureHandler);
        }

        @Bean
        public SmsCodeAuthenticationProvider smsCodeAuthenticationProvider(){
            return new SmsCodeAuthenticationProvider();
        }

        @Bean
        public SmsSecurityConfig smsSecurityConfig() {
            return new SmsSecurityConfig();
        }
    }
}
