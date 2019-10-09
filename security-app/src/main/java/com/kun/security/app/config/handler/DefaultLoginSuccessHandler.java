package com.kun.security.app.config.handler;

import com.kun.security.core.exception.AuthenticationException;
import org.apache.commons.codec.CharEncoding;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.MimeTypeUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.Collections;

/**
 * APP登陆成功处理器
 */
public class DefaultLoginSuccessHandler implements AuthenticationSuccessHandler {

    private ObjectMapper objectMapper = new ObjectMapper();

    private static Logger log = LoggerFactory.getLogger(DefaultLoginSuccessHandler.class);

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Autowired
    private AuthorizationServerTokenServices authorizationServerTokenServices;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setCharacterEncoding(CharEncoding.UTF_8);
        response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
        //用Basic 认证传入clientId和secret
        String header = request.getHeader("Authorization");
        if (header == null || !header.toLowerCase().startsWith("basic ")) {
            throw new AuthenticationException("没有OAuth2需要的的认证信息");
        }
        String[] tokens = extractAndDecodeHeader(header, request);
        String clientId = tokens[0];
        String secret   = tokens[1];

        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);
        if(clientDetails == null) {
            response.getWriter().write(objectMapper.writeValueAsString(new AuthenticationException("clientId不正确")));
            return;
        }

        if(!passwordEncoder.matches(secret, clientDetails.getClientSecret())) {
            response.getWriter().write(objectMapper.writeValueAsString(new AuthenticationException("secret不正确")));
            return;
        }

        TokenRequest tokenRequest = new TokenRequest(Collections.EMPTY_MAP, clientId, clientDetails.getScope(), "custom_login");

        OAuth2Request oauth2Request = tokenRequest.createOAuth2Request(clientDetails);

        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oauth2Request, authentication);

        OAuth2AccessToken accessToken = authorizationServerTokenServices.createAccessToken(oAuth2Authentication);

        log.debug("用户{}登陆成功", authentication.getPrincipal());

        response.getWriter().write(objectMapper.writeValueAsString(accessToken));
    }


    private String[] extractAndDecodeHeader(String header, HttpServletRequest request)
            throws IOException {

        byte[] base64Token = header.substring(6).getBytes("UTF-8");
        byte[] decoded;
        try {
            decoded = Base64.getDecoder().decode(base64Token);
        }
        catch (IllegalArgumentException e) {
            throw new BadCredentialsException(
                    "Failed to decode basic authentication token");
        }

        String token = new String(decoded, CharEncoding.UTF_8);

        int delimiter = token.indexOf(":");

        if (delimiter == -1) {
            throw new BadCredentialsException("Invalid basic authentication token");
        }
        return new String[] { token.substring(0, delimiter), token.substring(delimiter + 1) };
    }
}
