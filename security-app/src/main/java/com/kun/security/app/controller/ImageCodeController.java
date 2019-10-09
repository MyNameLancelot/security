package com.kun.security.app.controller;


import com.kun.security.core.controller.SecurityBrowserConstants;
import com.kun.security.core.domain.ImageValidateCode;
import com.kun.security.core.untils.ImageCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@ConditionalOnProperty(prefix = "login.verify.image", value = "enable", havingValue = "true")
@Controller
public class ImageCodeController {

    @Autowired
    private ImageCodeUtil imageCodeUtil;

    public static final String REDIS_IMAGE_VERIFY_CODE = "VERIFY_CODE:IMAGE_CODE:";

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping(SecurityBrowserConstants.VALIDATE_IMAGE_URL)
    @ResponseBody
    public void getVerifyCode( @RequestParam("device") String device, HttpServletResponse response) throws IOException {
        ImageValidateCode imageCode = imageCodeUtil.generateCodeAndPic();
        ImageValidateCode redisInCode = new ImageValidateCode();
        redisInCode.setCode(imageCode.getCode());
        redisInCode.setExpireDate(imageCode.getExpireDate());
        // 设置响应的类型格式为图片格式
        response.setContentType(MimeTypeUtils.IMAGE_JPEG_VALUE);
        // 禁止图像缓存。
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        // 写入图片
        ImageIO.write(imageCode.getCodePic(), "jpeg", response.getOutputStream());
        response.getOutputStream().flush();
        if (imageCode.getExpireDate() == null) {
            redisTemplate.boundValueOps(REDIS_IMAGE_VERIFY_CODE + device).set(redisInCode);
        }else {
            redisTemplate.boundValueOps(REDIS_IMAGE_VERIFY_CODE + device).set(redisInCode, imageCode.getExpireDate(), TimeUnit.MILLISECONDS);
        }
    }
}