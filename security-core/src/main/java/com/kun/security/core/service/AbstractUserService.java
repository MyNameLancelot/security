package com.kun.security.core.service;

import com.kun.security.core.exception.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 抽象用户Service
 * @param <IDType> User的ID类型
 * @param <UserType> UserType必须实现UserDetails
 */
public abstract class AbstractUserService<IDType,UserType extends UserDetails> {

    public abstract UserType getUserByUserName(String userName);

    public UserType getUserByMobile(String mobile) {
        throw new AuthenticationException("用户不支持手机号码查找");
    }

    public abstract UserType getUserByUserId(IDType userId);

    public abstract UserType saveUser(UserType user);
}
