package com.kun.security.core.domain;

import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 * 图片验证码
 * 包含：图片、验证码基类
 */
public class ImageValidateCode extends BaseValidateCode implements Serializable {

    // 验证码图片
    private BufferedImage codePic;

    public ImageValidateCode() {
    }

    public ImageValidateCode(String code, BufferedImage codePic) {
        super(code);
        this.codePic = codePic;
    }


    public BufferedImage getCodePic() {
        return codePic;
    }

    public void setCodePic(BufferedImage codePic) {
        this.codePic = codePic;
    }
}
