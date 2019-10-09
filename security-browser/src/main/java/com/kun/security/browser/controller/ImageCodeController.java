package com.kun.security.browser.controller;


import com.kun.security.core.controller.SecurityBrowserConstants;
import com.kun.security.core.domain.ImageValidateCode;
import com.kun.security.core.untils.ImageCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Controller;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ConditionalOnProperty(prefix = "login.verify.image", value = "enable", havingValue = "true")
@Controller
public class ImageCodeController {

    // spring工具类用于操作session
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    @Autowired
    private ImageCodeUtil imageCodeUtil;

    public static final String SESSION_IMAGE_VERIFY_CODE = "SESSION_IMAGE_VERIFY_CODE";

    @GetMapping(SecurityBrowserConstants.VALIDATE_IMAGE_URL)
    @ResponseBody
    public void getVerifyCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ImageValidateCode imageCode = imageCodeUtil.generateCodeAndPic();
        ImageValidateCode sessionInCode = new ImageValidateCode();
        sessionInCode.setCode(imageCode.getCode());
        sessionInCode.setExpireDate(imageCode.getExpireDate());
        sessionStrategy.setAttribute(new ServletWebRequest(request), SESSION_IMAGE_VERIFY_CODE, sessionInCode);
        // 设置响应的类型格式为图片格式
        response.setContentType(MimeTypeUtils.IMAGE_JPEG_VALUE);
        // 禁止图像缓存。
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        // 写入图片
        ImageIO.write(imageCode.getCodePic(), "jpeg", response.getOutputStream());
        // code放入session
        response.getOutputStream().flush();
    }
}