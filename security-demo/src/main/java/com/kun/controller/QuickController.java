package com.kun.controller;

import com.kun.domain.User;
//import com.kun.security.app.oauth2.AppProviderSignInUtils;
import com.kun.security.core.service.AbstractUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@RestController
public class QuickController {

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private AbstractUserService userService;

    @Autowired(required = false)
    private ProviderSignInUtils providerSignInUtils;
//
//    @Autowired(required = false)
//    private AppProviderSignInUtils appProviderSignInUtils;

    @RequestMapping(value = "/me", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Authentication me(Authentication authentication) {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @PostMapping("/user/register")
    public String register(User user, HttpServletRequest request) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.saveUser(user);
        if(providerSignInUtils != null) {
            providerSignInUtils.doPostSignUp(user.getUserId(), new ServletRequestAttributes(request));
        }
        return "ok";
    }

//    @PostMapping("/user/app/register")
//    public String registerApp(User user, HttpServletRequest request) {
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        userService.saveUser(user);
//        if(appProviderSignInUtils != null) {
//            appProviderSignInUtils.doPostSignUp(user.getUserId(), request);
//        }
//        return "ok";
//    }

    @GetMapping("/user/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String userAdd() {
        return "用户添加界面";
    }

//    @Autowired
//    private AppOAuth2Properties appOAuth2Properties;
//
//    @GetMapping("/jwt/info")
//    @PreAuthorize("hasRole('ADMIN')")
//    public String jwt(@RequestHeader("Authorization") String authorization) throws UnsupportedEncodingException {
//        String jwtToken = authorization.substring(7);
//        Claims claims = Jwts.parser().setSigningKey(appOAuth2Properties.getJwtSigningKey().getBytes(CharEncoding.UTF_8)).parseClaimsJws(jwtToken).getBody();
//        return (String) claims.get("company");
//    }

}
