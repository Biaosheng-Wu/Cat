package org.example.catcarebusiness.controller;

import org.example.catcarebusiness.config.CaptchaService;
import org.example.catcarebusiness.config.JwtUtils;
import org.example.catcarebusiness.entity.User;
import org.example.catcarebusiness.mapper.UserMapper;
import org.example.catcarebusiness.mapper.UserRoleMapper;
import org.example.catcarebusiness.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CaptchaService captchaService;

    /** 获取图形验证码 */
    @GetMapping("/captcha")
    public Map<String, String> getCaptcha() {
        return captchaService.generate();
    }

    /** 获取当前登录用户信息（含角色） */
    @GetMapping("/me")
    public Map<String, Object> getCurrentUser(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new CaptchaException("未登录");
        }
        String token = header.substring(7);
        if (!jwtUtils.validateToken(token)) {
            throw new CaptchaException("Token无效");
        }

        String username = jwtUtils.getUsernameFromToken(token);
        Long userId = jwtUtils.getUserIdFromToken(token);
        User user = userService.getByUsername(username);

        if (user == null) {
            throw new CaptchaException("用户不存在");
        }

        boolean isAdmin = userMapper.selectRoleCodesByUserId(userId)
                .contains("ROLE_ADMIN");

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("data", Map.of(
            "username", user.getUsername(),
            "nickname", user.getNickname() != null ? user.getNickname() : user.getUsername(),
            "phone", user.getPhone() != null ? user.getPhone() : "",
            "isAdmin", isAdmin
        ));
        return result;
    }

    /** 登录 */
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");

        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            throw new CaptchaException("用户名和密码不能为空");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userService.getByUsername(username);
        String token = jwtUtils.generateToken(username, user.getId());
        boolean isAdmin = userMapper.selectRoleCodesByUserId(user.getId())
                .contains("ROLE_ADMIN");

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "登录成功");
        result.put("data", Map.of(
            "token", token,
            "username", user.getUsername(),
            "nickname", user.getNickname() != null ? user.getNickname() : user.getUsername(),
            "phone", user.getPhone() != null ? user.getPhone() : "",
            "isAdmin", isAdmin
        ));
        return result;
    }

    /** 注册（需验证码） */
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Map<String, String> registerData) {
        String username = registerData.get("username");
        String password = registerData.get("password");
        String captchaKey = registerData.get("captchaKey");
        String captchaCode = registerData.get("captchaCode");

        if (username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) {
            throw new CaptchaException("用户名和密码不能为空");
        }

        if (username.trim().length() < 3 || username.trim().length() > 20) {
            throw new CaptchaException("用户名长度需在3-20个字符之间");
        }

        if (password.trim().length() < 6) {
            throw new CaptchaException("密码长度不能少于6位");
        }

        // 验证验证码
        if (captchaKey == null || captchaCode == null || captchaCode.trim().isEmpty()) {
            throw new CaptchaException("请输入验证码");
        }
        if (!captchaService.validate(captchaKey, captchaCode)) {
            throw new CaptchaException("验证码错误或已过期");
        }

        // 检查用户名唯一性
        User exist = userService.getByUsername(username.trim());
        if (exist != null) {
            throw new CaptchaException("用户名已存在");
        }

        User user = new User();
        user.setUsername(username.trim());
        user.setPassword(passwordEncoder.encode(password));
        user.setNickname(username.trim());
        user.setEnabled(1);
        user.setCreateTime(LocalDateTime.now());
        userMapper.insert(user);

        // 分配志愿者角色
        var volunRole = userRoleMapper.selectOne(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<org.example.catcarebusiness.entity.UserRole>()
                .eq(org.example.catcarebusiness.entity.UserRole::getRoleCode, "ROLE_VOLUNTEER"));
        if (volunRole != null) {
            userRoleMapper.insertUserRole(user.getId(), volunRole.getId());
        }

        String token = jwtUtils.generateToken(username, user.getId());

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "注册成功");
        result.put("data", Map.of(
            "token", token,
            "username", user.getUsername(),
            "nickname", user.getNickname(),
            "phone", "",
            "isAdmin", false
        ));
        return result;
    }

    /** 业务异常（返回 400 + JSON） */
    @ExceptionHandler(CaptchaException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleCaptchaException(CaptchaException e) {
        Map<String, Object> error = new HashMap<>();
        error.put("code", 400);
        error.put("message", e.getMessage());
        error.put("data", null);
        return error;
    }

    /** 自定义业务异常 */
    public static class CaptchaException extends RuntimeException {
        public CaptchaException(String message) {
            super(message);
        }
    }
}
