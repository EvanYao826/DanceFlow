package com.danceflow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.danceflow.security.JwtAuthenticationFilter;

/**
 * 安全配置（骨架阶段）：无状态 + 全部放行，保证前后端可以先行联调。
 * 阶段 1 将替换为 JWT 认证过滤器 + 基于角色的接口权限规则。
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/register", "/api/auth/login", "/api/health", "/uploads/**", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/api/activities/my").authenticated()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/activities", "/api/activities/**").permitAll()
                        .requestMatchers("/api/courses/my").authenticated()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/courses", "/api/courses/*").permitAll()
                        .requestMatchers("/api/works/mine").authenticated()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/works", "/api/works/**").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/community/posts", "/api/community/posts/**", "/api/notices", "/api/notices/**").permitAll()
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/courses/*/lessons/*").authenticated()
                        .requestMatchers("/api/courses/*/lessons/*/progress").authenticated()
                        .requestMatchers("/api/admin/**").hasAnyRole("ADMIN", "SUPER_ADMIN")
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
