package com.kun.security.app.config;

import com.kun.security.app.config.properties.AppOAuth2Properties;
import com.kun.security.app.config.properties.OAuth2ClientProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.ClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableConfigurationProperties(AppOAuth2Properties.class)
public class AppAuthorizationServerConfigurer extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    @Qualifier("securityUserServiceImpl")
    private UserDetailsService userDetailsService;

    @Autowired
    private AppOAuth2Properties appOAuth2Properties;

    @Autowired(required = false)
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired(required = false)
    private TokenEnhancer jwtTokenEnhancer;

    /**
     * 配置OAuth2认证的信息
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        InMemoryClientDetailsServiceBuilder inMemoryClientDetailsServiceBuilder = clients.inMemory();
        for (OAuth2ClientProperties oauth2ClientProperty : appOAuth2Properties.getClient()) {
            // client_id
            ClientDetailsServiceBuilder<InMemoryClientDetailsServiceBuilder>.ClientBuilder clientBuilder = inMemoryClientDetailsServiceBuilder.withClient(oauth2ClientProperty.getClientId());
            // client_secret
            clientBuilder.secret(passwordEncoder.encode(oauth2ClientProperty.getSecret()));
            // 允许的授权范围
            clientBuilder.scopes(oauth2ClientProperty.getScope());
            // token过期时间（秒）
            if( oauth2ClientProperty.getTokenValiditySeconds() != null){
                clientBuilder.accessTokenValiditySeconds(oauth2ClientProperty.getTokenValiditySeconds());
            } else {
                clientBuilder.accessTokenValiditySeconds(-1);
            }
            // 重定向地址
            clientBuilder.redirectUris(oauth2ClientProperty.getRedirectUrl().split(";"));
            // 允许的授权类型
            clientBuilder.authorizedGrantTypes("password", "authorization_code", "refresh_token");
        }
    }

    /**
     * 配置OAuth2认证Controller
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager);
        endpoints.setClientDetailsService(clientDetailsService);
        endpoints.userDetailsService(userDetailsService);
        endpoints.tokenStore(tokenStore);
        if( jwtAccessTokenConverter != null && jwtTokenEnhancer != null) {
            TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
            List<TokenEnhancer> tokenEnhancers = new ArrayList<>();
            if(jwtTokenEnhancer != null) {
                tokenEnhancers.add(jwtTokenEnhancer);
            }
            if(jwtAccessTokenConverter != null) {
                tokenEnhancers.add(jwtAccessTokenConverter);
            }
            tokenEnhancerChain.setTokenEnhancers(tokenEnhancers);
            endpoints.tokenEnhancer(tokenEnhancerChain);
        }
    }
}
