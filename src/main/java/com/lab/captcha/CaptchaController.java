package com.lab.captcha;

import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api/captcha")
@CrossOrigin(origins = "*") // السماح لجميع المواقع بالاتصال بالسيرفر
public class CaptchaController {

    @Autowired
    private Producer captchaProducer;

    // متغير لتخزين الكود بشكل مؤقت (بديل للسيشن لضمان العمل على Render المجاني)
    private String lastGeneratedCode = "";

    @GetMapping("/render")
    public void render(HttpServletResponse response) throws IOException {
        response.setContentType("image/jpeg");
        lastGeneratedCode = captchaProducer.createText();
        BufferedImage bi = captchaProducer.createImage(lastGeneratedCode);
        ImageIO.write(bi, "jpg", response.getOutputStream());
    }

    @GetMapping("/verify") // تم تغييرها إلى Get لتطابق طلب الـ HTML
    public boolean verify(@RequestParam String code) {
        if (lastGeneratedCode != null && lastGeneratedCode.equalsIgnoreCase(code)) {
            return true;
        }
        return false;
    }

    @GetMapping("/status")
    public String status() {
        return "Captcha Service is Running!";
    }
}
