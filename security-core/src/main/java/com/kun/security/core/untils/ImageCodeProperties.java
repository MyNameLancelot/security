package com.kun.security.core.untils;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "login.verify.image", ignoreUnknownFields = true)
public class ImageCodeProperties {
    private static final int DEFAULT_IMAGE_WIDTH  = 90;
    private static final int DEFAULT_IMAGE_HEIGHT = 90;
    private static final int DEFAULT_CODE_COUNT   = 4;
    private static final int DEFAULT_FONT_HEIGHT  = 18;
    private static final int DEFAULT_IMAGE_OFFSET_X   = 15;
    private static final int DEFAULT_IMAGE_OFFSET_Y   = 16;
    private static final String DEFAULT_FONT_NAME     = "微软雅黑";
    private static final char[] DEFAULT_CODE_SEQUENCE = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M',
            'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '2', '3', '4', '5', '6', '7', '8', '9' };

    private Long expireDate  = null;                     // 过期时间
    private int  imageWidth  = DEFAULT_IMAGE_WIDTH;      // 定义图片的width
    private int  imageHeight = DEFAULT_IMAGE_HEIGHT;     // 定义图片的height
    private int  codeCount   = DEFAULT_CODE_COUNT;       // 定义图片上显示验证码的个数
    private int  fontHeight  = DEFAULT_FONT_HEIGHT;      // 字体高度
    private int  offsetX     = DEFAULT_IMAGE_OFFSET_X;   // 验证码生成X轴间隔
    private int  offsetY     = DEFAULT_IMAGE_OFFSET_Y;   // 验证码生成Y轴间距
    private String  fontName = DEFAULT_FONT_NAME;        // 字体
    private char[] codeSequence = DEFAULT_CODE_SEQUENCE; // 生成序列
    private String accessUrl;                            // 访问图片验证码的URL
    private Boolean enable;

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public int getCodeCount() {
        return codeCount;
    }

    public void setCodeCount(int codeCount) {
        this.codeCount = codeCount;
    }

    public int getFontHeight() {
        return fontHeight;
    }

    public void setFontHeight(int fontHeight) {
        this.fontHeight = fontHeight;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public char[] getCodeSequence() {
        return codeSequence;
    }

    public void setCodeSequence(char[] codeSequence) {
        this.codeSequence = codeSequence;
    }

    public Long getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Long expireDate) {
        this.expireDate = expireDate;
    }

    public String getAccessUrl() {
        return accessUrl;
    }

    public void setAccessUrl(String accessUrl) {
        this.accessUrl = accessUrl;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }
}
