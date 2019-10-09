package com.kun.security.core.auth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.CharEncoding;
import org.springframework.social.connect.Connection;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConnectStatusView extends AbstractView {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, List<Connection<?>>> connectionMap = (Map<String, List<Connection<?>>>)model.get("connectionMap");
        Map<String, Boolean> connections = new HashMap<>();
        for (Map.Entry<String, List<Connection<?>>> entry : connectionMap.entrySet()) {
            connections.put(entry.getKey(), !CollectionUtils.isEmpty(entry.getValue()));
        }
        response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(CharEncoding.UTF_8);
        response.getWriter().write(objectMapper.writeValueAsString(connections));
    }
}
