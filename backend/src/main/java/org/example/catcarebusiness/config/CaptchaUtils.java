package org.example.catcarebusiness.config;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Random;

/**
 * 图形验证码生成工具
 * 生成 4 位数字+字母的验证码图片，返回 base64 PNG
 */
public class CaptchaUtils {

    private static final int WIDTH = 120;
    private static final int HEIGHT = 44;
    private static final int FONT_SIZE = 26;
    private static final String CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final Random RANDOM = new Random();

    /** 生成验证码，返回 {code, base64Image} */
    public static CaptchaResult generate() {
        StringBuilder code = new StringBuilder();

        // 1. 创建画布
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        // 2. 随机背景色
        g.setColor(randomLightColor());
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // 3. 绘制干扰线
        g.setColor(randomColor());
        for (int i = 0; i < 5; i++) {
            int x1 = RANDOM.nextInt(WIDTH);
            int y1 = RANDOM.nextInt(HEIGHT);
            int x2 = RANDOM.nextInt(WIDTH);
            int y2 = RANDOM.nextInt(HEIGHT);
            g.drawLine(x1, y1, x2, y2);
        }

        // 4. 绘制干扰点
        for (int i = 0; i < 30; i++) {
            g.setColor(randomColor());
            int x = RANDOM.nextInt(WIDTH);
            int y = RANDOM.nextInt(HEIGHT);
            g.drawOval(x, y, 1, 1);
        }

        // 5. 绘制验证码文字
        g.setFont(new Font("Arial", Font.BOLD, FONT_SIZE));
        for (int i = 0; i < 4; i++) {
            String ch = String.valueOf(CHARS.charAt(RANDOM.nextInt(CHARS.length())));
            code.append(ch);
            g.setColor(randomDarkColor());
            // 每个字符略微旋转
            double angle = (RANDOM.nextDouble() - 0.5) * 0.4;
            g.rotate(angle, 20 + i * 26, 30);
            g.drawString(ch, 14 + i * 26, 32);
            g.rotate(-angle, 20 + i * 26, 30);
        }

        g.dispose();

        // 6. 转 base64
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            String base64 = Base64.getEncoder().encodeToString(baos.toByteArray());
            return new CaptchaResult(code.toString(), "data:image/png;base64," + base64);
        } catch (Exception e) {
            throw new RuntimeException("验证码生成失败", e);
        }
    }

    private static Color randomLightColor() {
        return new Color(240 + RANDOM.nextInt(16), 240 + RANDOM.nextInt(16), 240 + RANDOM.nextInt(16));
    }

    private static Color randomColor() {
        return new Color(RANDOM.nextInt(200), RANDOM.nextInt(200), RANDOM.nextInt(200));
    }

    private static Color randomDarkColor() {
        return new Color(20 + RANDOM.nextInt(80), 20 + RANDOM.nextInt(80), 20 + RANDOM.nextInt(80));
    }

    public static class CaptchaResult {
        private final String code;
        private final String image;

        public CaptchaResult(String code, String image) {
            this.code = code;
            this.image = image;
        }

        public String getCode() { return code; }
        public String getImage() { return image; }
    }
}
