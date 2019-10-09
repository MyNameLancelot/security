package com.kun.security.app.oauth2.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kun.security.app.controller.SmsCodeController;
import com.kun.security.core.response.SimpleResponse;
import org.apache.commons.codec.CharEncoding;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.ProviderSignInAttempt;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.social.security.SocialAuthenticationRedirectException;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AppSocialAuthenticationFailureHandler implements AuthenticationFailureHandler {

	public static final String REDIS_SOCIAL_CONNECT_INFO = "SOCIAL_CONNECT:";

	private static Logger log = LoggerFactory.getLogger(SmsCodeController.class);

	private AuthenticationFailureHandler delegate;

	private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

	private ObjectMapper objectMapper = new ObjectMapper();

	private RedisTemplate redisTemplate;

	public AppSocialAuthenticationFailureHandler(RedisTemplate redisTemplate, AuthenticationFailureHandler delegate) {
		this.redisTemplate = redisTemplate;
		this.delegate = delegate;
	}

	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
		if (failed instanceof SocialAuthenticationRedirectException) {
			String deviceId = request.getParameter("device");
			if (deviceId == null) {
				log.error("device为空");
				response.setCharacterEncoding(CharEncoding.UTF_8);
				response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
				response.setStatus(HttpStatus.SC_BAD_REQUEST);
				SimpleResponse result = new SimpleResponse(String.valueOf(HttpStatus.SC_BAD_REQUEST), "[device]" + "不能为空");
				response.getWriter().write(objectMapper.writeValueAsString(result));
				return;
			}
			ProviderSignInAttempt providerSignInAttempt = (ProviderSignInAttempt) sessionStrategy.getAttribute(new ServletWebRequest(request), ProviderSignInAttempt.SESSION_ATTRIBUTE);
			redisTemplate.boundValueOps(REDIS_SOCIAL_CONNECT_INFO + deviceId).set(providerSignInAttempt);
			sessionStrategy.removeAttribute(new ServletWebRequest(request), ProviderSignInAttempt.SESSION_ATTRIBUTE);
			response.sendRedirect(((SocialAuthenticationRedirectException) failed).getRedirectUrl());
			return;
		}
		delegate.onAuthenticationFailure(request, response, failed);
	}
}