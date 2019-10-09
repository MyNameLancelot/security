package com.kun.security.browser.config.handler;

import com.kun.security.core.response.SimpleResponse;
import org.apache.commons.codec.CharEncoding;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.MimeTypeUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 默认登陆成功处理器
 */
public class DefaultLoginSuccessHandler implements AuthenticationSuccessHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    private static Logger log = LoggerFactory.getLogger(DefaultLoginSuccessHandler.class);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.debug("用户{}登陆成功", authentication.getPrincipal());
        SimpleResponse simpleResponse = new SimpleResponse(String.valueOf(HttpStatus.OK.value()), "登陆成功");
        response.setCharacterEncoding(CharEncoding.UTF_8);
        response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(simpleResponse));
        response.flushBuffer();
    }
}
