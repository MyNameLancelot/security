package com.kun.security.core.auth2.wechat;

import com.kun.security.core.auth2.wechat.connect.WeChatConnectFactory;
import com.kun.security.core.service.AbstractUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;

import javax.sql.DataSource;


@EnableConfigurationProperties(WeChatSocialProperties.class)
@Configuration
@ConditionalOnProperty(prefix = "wechat.social", name = "enable")
public class WeChatSocialConfig extends SocialConfigurerAdapter {

    private static Logger log = LoggerFactory.getLogger(WeChatSocialConfig.class);

    private DataSource dataSource;

    private WeChatSocialProperties weChatSocialProperties;

    @Autowired
    private ConfigurableApplicationContext configurableApplicationContext;

    @Autowired(required = false)
    private ConnectionSignUp connectionSignUp;

    public WeChatSocialConfig(WeChatSocialProperties weChatSocialProperties, DataSource dataSource, AbstractUserService userService) {
        this.dataSource = dataSource;
        this.weChatSocialProperties = weChatSocialProperties;
    }


    @Override
    public UserIdSource getUserIdSource() {
        try {
            return weChatSocialProperties.getUserIdSourceType().newInstance();
        } catch (InstantiationException e) {
            log.error("得到UserIdSource时发生异常【{}】,Spring 容器即将关闭", e.getMessage());
        } catch (IllegalAccessException e) {
            log.error("得到UserIdSource时发生异常【{}】,Spring 容器即将关闭", e.getMessage());
        }
        configurableApplicationContext.registerShutdownHook();
        return null;
    }

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        JdbcUsersConnectionRepository jdbcUsersConnectionRepository = new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
        jdbcUsersConnectionRepository.setConnectionSignUp(connectionSignUp);
        return jdbcUsersConnectionRepository;
    }

    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer connectionFactoryConfigurer, Environment environment) {
        connectionFactoryConfigurer.addConnectionFactory(
                new WeChatConnectFactory(weChatSocialProperties.getProviderId(), weChatSocialProperties.getAppId(), weChatSocialProperties.getAppKey()));
    }
}
