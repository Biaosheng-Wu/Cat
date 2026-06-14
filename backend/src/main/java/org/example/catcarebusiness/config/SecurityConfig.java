package org.example.catcarebusiness.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // 登录注册相关全部放行
                .requestMatchers("/api/auth/**").permitAll()
                // 文件上传放行
                .requestMatchers("/api/file/**").permitAll()

                // 查询类接口：登录即可访问
                .requestMatchers(HttpMethod.GET, "/api/cat/list", "/api/cat/{id}").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/feed/todayCount/{catId}").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/feed/my").authenticated()

                // 写入类接口：登录即可访问
                .requestMatchers(HttpMethod.POST, "/api/cat/add").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/feed/submit").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/report/submit").authenticated()

                // 管理类接口：仅管理员可访问
                .requestMatchers("/api/cat/tnr/**").hasRole("ADMIN")
                .requestMatchers("/api/cat/delete/**").hasRole("ADMIN")
                .requestMatchers("/api/report/handle/**").hasRole("ADMIN")

                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
