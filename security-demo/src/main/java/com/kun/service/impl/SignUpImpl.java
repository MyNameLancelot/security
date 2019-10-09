package com.kun.service.impl;

import com.kun.domain.User;
import com.kun.security.core.service.AbstractUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.stereotype.Component;

import java.util.UUID;

//@Component
public class SignUpImpl implements ConnectionSignUp {
    @Autowired
    private AbstractUserService abstractUserService;

    @Override
    public String execute(Connection<?> connection) {
        User user = new User();
        user.setUsername(UUID.randomUUID().toString());
        abstractUserService.saveUser(user);
        return user.getUserId();
    }
}
