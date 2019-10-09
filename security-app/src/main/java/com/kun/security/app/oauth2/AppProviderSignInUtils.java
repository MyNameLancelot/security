/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kun.security.app.oauth2;

import com.kun.security.app.oauth2.processor.AppSocialAuthenticationFailureHandler;
import com.kun.security.core.exception.AuthenticationException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInAttempt;

import javax.servlet.http.HttpServletRequest;

/**
 * 将connect信息保存到redis
 */
public class AppProviderSignInUtils {

	private ConnectionFactoryLocator connectionFactoryLocator;
	private UsersConnectionRepository connectionRepository;
	private RedisTemplate redisTemplate;

	public AppProviderSignInUtils(RedisTemplate redisTemplate, ConnectionFactoryLocator connectionFactoryLocator, UsersConnectionRepository connectionRepository) {
		this.redisTemplate = redisTemplate;
		this.connectionFactoryLocator = connectionFactoryLocator;
		this.connectionRepository = connectionRepository;
	}
	
	public void doPostSignUp(String userId, HttpServletRequest request) {
		ProviderSignInAttempt signInAttempt = (ProviderSignInAttempt) getProviderUserSignInAttempt(request);
		if (signInAttempt != null) {
			connectionRepository.createConnectionRepository(userId).addConnection(signInAttempt.getConnection(connectionFactoryLocator));
			String deviceId = request.getParameter("device");
			redisTemplate.delete(AppSocialAuthenticationFailureHandler.REDIS_SOCIAL_CONNECT_INFO + deviceId);
		}
	}

	private ProviderSignInAttempt getProviderUserSignInAttempt(HttpServletRequest request) {
		String deviceId = request.getParameter("device");
		if (deviceId == null) {
			throw new AuthenticationException("【device】不能为空");
		}
		return (ProviderSignInAttempt)redisTemplate.boundValueOps(AppSocialAuthenticationFailureHandler.REDIS_SOCIAL_CONNECT_INFO + deviceId).get();
	}
	
}
