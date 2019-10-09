package com.kun.service.impl;

import com.kun.domain.User;
import com.kun.security.core.service.AbstractOAUth2UserService;
import com.kun.security.core.service.AbstractUserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl extends AbstractOAUth2UserService<String, User> {

    Map<String, User> userNameMap = new HashMap<>();
    Map<String, User> userMobileMap = new HashMap<>();
    Map<Integer, User> userIdMap = new HashMap<>();

    public UserServiceImpl() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        User user = new User();
        user.setUsername("张三");
        user.setNickName("巨无霸");
        user.setId(1);
        user.setMobile("15712904378");
        // 真正数据库的密码应该是加密的，不应该在此加密
        user.setPassword(passwordEncoder.encode("123"));
        userNameMap.put("张三", user);
        userMobileMap.put("15712904378", user);
        userIdMap.put(1, user);
    }


    @Override
    public User getUserByUserName(String userName) {
        return userNameMap.get(userName);
    }

    @Override
    public User getUserByMobile(String mobile) {
        return userMobileMap.get(mobile);
    }

    @Override
    public User getUserByUserId(String userId) {
        return userIdMap.get(Integer.valueOf(userId));
    }

    @Override
    public User saveUser(User user) {
        userNameMap.put(user.getUsername(), user);
        userMobileMap.put(user.getMobile(), user);
        user.setId(100);
        userIdMap.put(100, user);
        return user;
    }
}


