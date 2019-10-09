package com.kun.security.core.response;

/**
 * 简单的回复对象
 * 包含：code【状态码】，content【内容】
 */
public class SimpleResponse {
    String code;
    String content;

    public SimpleResponse(String code, String content) {
        this.code = code;
        this.content = content;
    }

    public SimpleResponse() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
