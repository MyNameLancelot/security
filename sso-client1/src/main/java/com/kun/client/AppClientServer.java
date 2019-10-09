package com.kun.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
@Controller
@EnableOAuth2Sso
public class AppClientServer {

    @RequestMapping("/user")
    @ResponseBody
    public Authentication user(Authentication authentication) {
        return authentication;
    }

    public static void main(String[] args) {
        SpringApplication.run(AppClientServer.class, args);
    }
}
