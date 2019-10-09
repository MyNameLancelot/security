package com.kun.security.browser.config;

import org.apache.commons.codec.CharEncoding;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.util.MimeTypeUtils;

import javax.servlet.ServletException;
import java.io.IOException;

public class BrowserSessionInformationExpiredStrategy implements SessionInformationExpiredStrategy {

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
        event.getResponse().setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
        event.getResponse().setCharacterEncoding(CharEncoding.UTF_8);
        event.getResponse().getWriter().write("账号在别处登陆");
    }
}
