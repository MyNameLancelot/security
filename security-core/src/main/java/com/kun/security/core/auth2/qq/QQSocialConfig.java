package com.kun.security.core.auth2.qq;

import com.kun.security.core.auth2.qq.connect.QQConnectFactory;
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


@EnableConfigurationProperties(QQSocialProperties.class)
@Configuration
@ConditionalOnProperty(prefix = "qq.social", name = "enable")
public class QQSocialConfig extends SocialConfigurerAdapter {

    private static Logger log = LoggerFactory.getLogger(QQSocialConfig.class);

    private DataSource dataSource;

    private QQSocialProperties qqSocialProperties;

    @Autowired
    private ConfigurableApplicationContext configurableApplicationContext;

    @Autowired(required = false)
    private ConnectionSignUp connectionSignUp;

    public QQSocialConfig(QQSocialProperties qqSocialProperties, DataSource dataSource, AbstractUserService userService) {
        this.dataSource = dataSource;
        this.qqSocialProperties = qqSocialProperties;

    }


    @Override
    public UserIdSource getUserIdSource() {
        try {
            return qqSocialProperties.getUserIdSourceType().newInstance();
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
                new QQConnectFactory(qqSocialProperties.getProviderId(), qqSocialProperties.getAppId(), qqSocialProperties.getAppKey()));
    }
}
