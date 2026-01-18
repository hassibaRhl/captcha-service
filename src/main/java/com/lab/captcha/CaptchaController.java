package com.lab.captcha;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

@CrossOrigin(origins = "*") 
@RestController
@RequestMapping("/api/captcha")
public class CaptchaController {

    @Autowired
    private DefaultKaptcha captchaProducer;

    private static String lastText = ""; // تخزين الكود للتحقق

    @GetMapping("/render")
    public void render(HttpServletResponse response) throws IOException {
        response.setContentType("image/jpeg");
        response.setHeader("Cache-Control", "no-store, no-cache");
        
        lastText = captchaProducer.createText();
        BufferedImage bi = captchaProducer.createImage(lastText);
        ImageIO.write(bi, "jpg", response.getOutputStream());
    }

    @GetMapping("/verify")
    public boolean verify(@RequestParam String code) {
        return lastText != null && lastText.equalsIgnoreCase(code);
    }

    @GetMapping("/status")
    public String status() {
        return "Captcha Service is Running!";
    }
}
