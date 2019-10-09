package com.kun.security.core.untils;

import com.kun.security.core.domain.ImageValidateCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.Instant;
import java.util.Random;

/**
 * 验证码生成工具类
 */
@Configuration
@ConditionalOnProperty(prefix = "login.verify.image", name = "enable")
@EnableConfigurationProperties(ImageCodeProperties.class)
public class ImageCodeUtil {

    private Logger log = LoggerFactory.getLogger(ImageCodeUtil.class);

    private ImageCodeProperties imageCodeProperties;

    public ImageCodeUtil(ImageCodeProperties imageCodeProperties) {
        this.imageCodeProperties = imageCodeProperties;
    }

    /**
     * code为生成的验证码
     * codePic为生成的验证码BufferedImage对象
     */
    public ImageValidateCode generateCodeAndPic() {
        Long expireDate     = imageCodeProperties.getExpireDate();      // 过期时间
        int  imageWidth     = imageCodeProperties.getImageWidth();      // 定义图片的width
        int  imageHeight    = imageCodeProperties.getFontHeight();      // 定义图片的height
        int  codeCount      = imageCodeProperties.getCodeCount();       // 定义图片上显示验证码的个数
        int  fontHeight     = imageCodeProperties.getFontHeight();      // 字体高度
        int  offsetX        = imageCodeProperties.getOffsetX();         // 验证码生成X轴间隔
        int  offsetY        = imageCodeProperties.getOffsetY();         // 验证码生成Y轴间距
        String fontName     = imageCodeProperties.getFontName();        // 字体
        char[] codeSequence = imageCodeProperties.getCodeSequence();    // 生成序列

        // 定义图像buffer
        BufferedImage buffImg = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = buffImg.getGraphics();
        // 创建一个随机数生成器类
        Random random = new Random();
        // 将图像填充为白色
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, imageWidth, imageHeight);
        // 创建字体，字体的大小应该根据图片的高度来定。
        Font font = new Font(fontName, Font.BOLD, fontHeight);
        // 设置字体。
        graphics.setFont(font);
        // 画边框。
        graphics.setColor(Color.BLACK);
        graphics.drawRect(0, 0, imageWidth - 1, imageHeight - 1);
        // 随机产生30条干扰线，使图象中的认证码不易被其它程序探测到。
        graphics.setColor(Color.BLACK);
        for (int i = 0; i < 0; i++) {
            int x1 = random.nextInt(imageWidth);
            int y1 = random.nextInt(imageHeight);
            int x2 = random.nextInt(imageWidth);
            int y2 = random.nextInt(imageHeight);
            graphics.drawLine(x1, y1, x1 + x2, y1 + y2);
        }
        // randomCode用于保存随机产生的验证码，以便用户登录后进行验证。
        StringBuffer randomCode = new StringBuffer();
        int red   = 0;
        int green = 0;
        int blue  = 0;
        // 随机产生codeCount数字的验证码。
        for (int i = 0; i < codeCount; i++) {
            // 得到随机产生的验证码数字。
            String code = String.valueOf(codeSequence[random.nextInt(codeSequence.length)]);
            // 产生随机的颜色分量来构造颜色值，这样输出的每位数字的颜色值都将不同。设置为230避免使用纯白色
            red = random.nextInt(230);
            green = random.nextInt(230);
            blue = random.nextInt(230);
            // 用随机产生的颜色将验证码绘制到图像中。
            graphics.setColor(new Color(red, green, blue));
            graphics.drawString(code, (i + 1) * offsetX, offsetY);
            // 将产生的四个随机数组合在一起。
            randomCode.append(code);
        }
        ImageValidateCode imageCode = new ImageValidateCode(randomCode.toString(), buffImg);
        if (expireDate != null){
            imageCode.setExpireDate(Instant.now().getEpochSecond() + expireDate);
        }
        log.debug("生成图片验证码{}", imageCode.getCode());
        return imageCode ;
    }
}