package com.lab.captcha;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@RestController
@RequestMapping("/api/captcha")
public class CaptchaController {

    @Autowired
    private DefaultKaptcha captchaProducer;

    @GetMapping("/render")
    public void render(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("image/jpeg");
        String capText = captchaProducer.createText();
        request.getSession().setAttribute("captcha_key", capText);
        BufferedImage bi = captchaProducer.createImage(capText);
        ImageIO.write(bi, "jpg", response.getOutputStream());
    }

    @PostMapping("/verify")
    public String verify(@RequestParam String code, HttpServletRequest request) {
        String sessionCode = (String) request.getSession().getAttribute("captcha_key");
        if (sessionCode != null && sessionCode.equalsIgnoreCase(code)) {
            return "✅ Correct!";
        }
        return "❌ Incorrect!";
    }

    @GetMapping("/status")
    public String status() {
        return "Captcha Service is Running!";
    }
}