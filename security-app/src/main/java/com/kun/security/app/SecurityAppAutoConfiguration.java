package com.kun.security.app;

import com.kun.security.app.config.handler.DefaultLoginFailureHandler;
import com.kun.security.app.config.handler.DefaultLoginSuccessHandler;
import com.kun.security.app.config.properties.AppOAuth2Properties;
import com.kun.security.app.config.provider.AppPermissionConfigProvider;
import com.kun.security.app.filter.ImageValidateCodeFilter;
import com.kun.security.app.filter.SmsValidateCodeFilter;
import com.kun.security.app.oauth2.AppProviderSignInUtils;
import com.kun.security.app.oauth2.OpenIdSecurityConfig;
import com.kun.security.app.oauth2.processor.SocialAuthenticationPostProcessorImpl;
import com.kun.security.core.CoreSecurityProperties;
import com.kun.security.core.auth2.ConnectStatusView;
import com.kun.security.core.auth2.ConnectView;
import com.kun.security.core.auth2.SocialAuthenticationPostProcessor;
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
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.security.SpringSocialConfigurer;
import org.springframework.web.servlet.view.AbstractView;

@Configuration
@EnableAuthorizationServer  //开启OAuth2认证服务器
@EnableResourceServer       //开启OAuth2资源服务器
public class SecurityAppAutoConfiguration {

    /**
     * 注入密码加密器，WebSecurityConfigurerAdapter会扫描Context中的所有在PasswordEncoder，进行使用
     */
    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 权限管理器
     */
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @Bean
    public PermissionConfigProvider appPermissionConfigProvider(ApplicationContext applicationContext, CoreSecurityProperties coreSecurityProperties){
        return new AppPermissionConfigProvider(applicationContext, coreSecurityProperties);
    }

    /**
     * 第三方登陆用于注册账户时提供帮助
     */
    @Bean
    @ConditionalOnBean(value = {RedisTemplate.class, ConnectionFactoryLocator.class, UsersConnectionRepository.class})
    AppProviderSignInUtils appProviderSignInUtils(RedisTemplate redisTemplate, ConnectionFactoryLocator connectionFactoryLocator, UsersConnectionRepository connectionRepository) {
        return new AppProviderSignInUtils(redisTemplate, connectionFactoryLocator, connectionRepository);
    }

    /**
     * 登陆成功处理器
     */
    @Bean
    @ConditionalOnMissingBean(AuthenticationSuccessHandler.class)
    public AuthenticationSuccessHandler defaultLoginSuccessHandler(){
        return new DefaultLoginSuccessHandler();
    }

    /**
     * 登陆失败处理器
     */
    @Bean
    @ConditionalOnMissingBean(AuthenticationFailureHandler.class)
    public AuthenticationFailureHandler defaultLoginFailureHandler(){
        return new DefaultLoginFailureHandler();
    }

    @Bean
    @ConditionalOnProperty(prefix = "app.oauth2", name = "store-type", havingValue = "redis", matchIfMissing = true)
    public TokenStore tokenStore(RedisConnectionFactory redisConnectionFactory){
        return new RedisTokenStore(redisConnectionFactory);
    }

    @Configuration
    @ConditionalOnProperty(prefix = "app.oauth2", name = "store-type", havingValue = "jwt")
    public class JwtTokenConfig {

        @Bean
        public JwtTokenStore jwtTokenStore(JwtAccessTokenConverter jwtTokenEnhancer) {
            return new JwtTokenStore(jwtTokenEnhancer);
        }

        @Bean
        public JwtAccessTokenConverter jwtAccessTokenConverter(AppOAuth2Properties appOAuth2Properties) {
            JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
            jwtAccessTokenConverter.setSigningKey(appOAuth2Properties.getJwtSigningKey());
            return jwtAccessTokenConverter;
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

    @Configuration
    @ConditionalOnProperty(prefix = "login.verify.image", value = "enable", havingValue = "true")
    public class ImageCodeAutoConfiguration{

        @Bean
        public ImageValidateCodeFilter imageValidateCodeFilter(AuthenticationFailureHandler authenticationFailureHandler) {
            return new ImageValidateCodeFilter(authenticationFailureHandler);
        }
    }

    /**
     * 获取社交账户信息
     */
    @Import(ConnectController.class)
    @ConditionalOnBean(SpringSocialConfigurer.class)
    public class SocialAutoConfigurer {
        /**
         * 第三方登陆使用简化模式登陆时开启
         */
        @ConditionalOnProperty(prefix = "app.oauth2", name = "open-id-login", havingValue = "true")
        @Bean
        public OpenIdSecurityConfig openIdSecurityConfig(){
            return new OpenIdSecurityConfig();
        }

        /**
         * 第三方登陆使用授权码模式登陆时开启
         */
        @ConditionalOnProperty(prefix = "app.oauth2", name = "code-login", havingValue = "true")
        @Bean
        public SocialAuthenticationPostProcessor socialAuthenticationPostProcessor(){
            return new SocialAuthenticationPostProcessorImpl();
        }

        /**
         * 查看第三方账户绑定情况的视图
         */
        @ConditionalOnMissingBean(name = "connect/status")
        @Bean("connect/status")
        public AbstractView connectStatusView(){
            return new ConnectStatusView();
        }

        /**
         * 绑定&解绑第三方账户情况的视图
         */
        @ConditionalOnMissingBean(name = {"connect/qqConnected", "connect/qqConnect"})
        @Bean({"connect/qqConnected", "connect/qqConnect", "connect/wechatConnected", "connect/wechatConnect"})
        public AbstractView connectView(){
            return new ConnectView();
        }
    }
}
