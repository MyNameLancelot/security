package com.kun.security.app.controller;

import com.kun.security.core.response.AuthenticationResponse;
import org.apache.commons.codec.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class AppSecurityController {

    private Logger log = LoggerFactory.getLogger(AppSecurityController.class);

    /**
     * 当发现需要身份认证时跳入到这个请求
     */
    @GetMapping(value = "/authentication/require", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public AuthenticationResponse requireAuthentication(HttpServletRequest request, HttpServletResponse response) {
        response.setCharacterEncoding(CharEncoding.UTF_8);
        log.debug("未进行身份认证，AJAX请求将返回状态码401");
        return new AuthenticationResponse(request.getRequestURI(), "未登陆", HttpStatus.UNAUTHORIZED.value());
    }
}

