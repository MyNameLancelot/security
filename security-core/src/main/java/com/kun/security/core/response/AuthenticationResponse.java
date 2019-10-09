package com.kun.security.core.response;

/**
 * 登陆认证回复对象
 * 包含：redirectURI【成功跳转到的URI】，content【内容】，code【状态码：200-OK,500-ERROR】
 */
public class AuthenticationResponse {
    private String  redirectURI;
    private String  content;
    private Integer code;

    public AuthenticationResponse(String redirectURI, String content, Integer code) {
        this.redirectURI = redirectURI;
        this.content = content;
        this.code = code;
    }

    public String getRedirectURI() {
        return redirectURI;
    }

    public void setRedirectURI(String redirectURI) {
        this.redirectURI = redirectURI;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
