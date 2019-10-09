package com.kun.security.core.service;

import org.springframework.social.security.SocialUserDetails;

/**
 * 抽象OAth2用户Service
 * @param <IDType> User的ID类型
 * @param <UserType> UserType必须实现SocialUserDetails
 */
public abstract class AbstractOAUth2UserService<IDType, UserType extends SocialUserDetails>
        extends AbstractUserService<IDType, UserType>{

}
