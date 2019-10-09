package com.kun.security.app.authentication.filter;

import com.kun.security.app.authentication.token.OpenIdCodeAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *  OpenId认证登陆处理拦截生成令牌用于校验
 */
public class OpenIdAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private boolean postOnly = true;

    private String openIdParameter = "openId";

    private String providerIdParameter = "providerId";

    public OpenIdAuthenticationFilter() {
        super(new AntPathRequestMatcher("/authentication/openid", "POST"));
    }

    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        if (postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }

        String openId     = obtainOpenId(request);
        String providerId = obtainProviderId(request);

        OpenIdCodeAuthenticationToken authRequest = new OpenIdCodeAuthenticationToken(openId, providerId);

        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

    protected String obtainOpenId(HttpServletRequest request) {
        return request.getParameter(openIdParameter);
    }

    protected String obtainProviderId(HttpServletRequest request) {
        return request.getParameter(providerIdParameter);
    }


    protected void setDetails(HttpServletRequest request,
                              OpenIdCodeAuthenticationToken authRequest) {
        authRequest.setDetails(authenticationDetailsSource.buildDetails(request));
    }

    public void setOpenIdParameter(String openIdParameter) {
        Assert.hasText(openIdParameter, "openIdParameter parameter must not be empty or null");
        this.openIdParameter = openIdParameter;
    }

    public void setProviderIdParameter(String providerIdParameter) {
        Assert.hasText(providerIdParameter, "providerIdParameter parameter must not be empty or null");
        this.providerIdParameter = providerIdParameter;
    }
}
