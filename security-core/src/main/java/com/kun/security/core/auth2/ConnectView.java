package com.kun.security.core.auth2;

import org.apache.commons.codec.CharEncoding;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


public class ConnectView extends AbstractView {

    protected static final String DUPLICATE_CONNECTION_ATTRIBUTE = "social_addConnection_duplicate";

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setCharacterEncoding(CharEncoding.UTF_8);
        response.setContentType(MimeTypeUtils.TEXT_HTML_VALUE);
        if(model.get(DUPLICATE_CONNECTION_ATTRIBUTE) != null){
            response.getWriter().write("<h3>此社交账户已被其它用户绑定</h3>");
            return;
        }

        if(model.get("connections") == null) {
            response.getWriter().write("<h3>解绑成功</h3>");
        }else {
            response.getWriter().write("<h3>绑定成功</h3>");
        }
    }
}
