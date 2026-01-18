package com.lab.captcha;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/captcha")
public class CaptchaController {

    @Autowired
    private DefaultKaptcha captchaProducer;

    // تخزين الكود في الذاكرة (لأغراض المختبر) لضمان عدم حدوث خطأ 500
    private static String currentCaptchaText = "";

    @GetMapping(value = "/render", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] render() throws IOException {
        // 1. إنشاء نص الكابتشا
        currentCaptchaText = captchaProducer.createText();
        
        // 2. إنشاء الصورة
        BufferedImage bi = captchaProducer.createImage(currentCaptchaText);
        
        // 3. تحويل الصورة إلى مصفوفة bytes لإرسالها للمتصفح مباشرة
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi, "jpg", baos);
        return baos.toByteArray();
    }

    @GetMapping("/verify")
    public boolean verify(@RequestParam String code) {
        return currentCaptchaText != null && currentCaptchaText.equalsIgnoreCase(code);
    }

    @GetMapping("/status")
    public String status() {
        return "السيرفر يعمل بنجاح! 🚀";
    }
}
