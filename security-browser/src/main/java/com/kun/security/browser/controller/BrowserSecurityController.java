package com.kun.security.browser.controller;

import com.kun.security.core.CoreSecurityProperties;
import com.kun.security.core.response.AuthenticationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class BrowserSecurityController {

    private static Logger log = LoggerFactory.getLogger(BrowserSecurityController.class);

    @Autowired
    private CoreSecurityProperties coreSecurityProperties;

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    /**
     * 当发现需要身份认证时跳入到这个请求
     */
    @GetMapping("/authentication/require")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public AuthenticationResponse requireAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String requestedWith = request.getHeader("X-Requested-With");
        if (!"XMLHttpRequest".equals(requestedWith)) {
            log.debug("未进行身份认证，游览器请求将跳转至登陆页面");
            redirectStrategy.sendRedirect(request, response, coreSecurityProperties.getLoginHtmlUrl());
            return null;
        }else {
            log.debug("未进行身份认证，AJAX请求将返回状态码401");
            return new AuthenticationResponse(request.getRequestURI(), "未登陆", HttpStatus.UNAUTHORIZED.value());
        }
    }

    @GetMapping("/session/invalid")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public AuthenticationResponse invalidSession(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String requestedWith = request.getHeader("X-Requested-With");
        if (!"XMLHttpRequest".equals(requestedWith)) {
            log.debug("会话过期，游览器请求将跳转至登陆页面，引发跳转的路径{0}",request.getRequestURL());
            redirectStrategy.sendRedirect(request, response, coreSecurityProperties.getLoginHtmlUrl());
        }
        log.debug("未进行身份认证，AJAX请求将返回状态码401");
        return new AuthenticationResponse(request.getRequestURI(), "会话超时", HttpStatus.UNAUTHORIZED.value());
    }
}
