package org.example.catcarebusiness.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. 关闭 CSRF 防御（由于我们是前后端分离的 API 接口测试，必须关闭，否则 POST 请求会被拒绝）
                .csrf(csrf -> csrf.disable())
                // 2. 配置接口访问控制
                .authorizeHttpRequests(auth -> auth
                        // 允许所有用户无条件访问以 /api/ 开头的任意网址
                        .requestMatchers("/api/**").permitAll()
                        // 其余任何请求需要认证（保留默认安全规则）
                        .anyRequest().authenticated()
                );
        return http.build();
    }
}