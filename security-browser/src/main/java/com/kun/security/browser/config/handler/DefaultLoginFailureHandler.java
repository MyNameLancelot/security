package com.kun.security.browser.config.handler;

import com.kun.security.core.response.SimpleResponse;
import org.apache.commons.codec.CharEncoding;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.util.MimeTypeUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 默认的登陆失败处理器
 */
public class DefaultLoginFailureHandler implements AuthenticationFailureHandler {

    private static Logger log = LoggerFactory.getLogger(DefaultLoginFailureHandler.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.info("登录失败");
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(CharEncoding.UTF_8);
        SimpleResponse simpleResponse = new SimpleResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), exception.getMessage());
        response.getWriter().write(objectMapper.writeValueAsString(simpleResponse));
    }
}
