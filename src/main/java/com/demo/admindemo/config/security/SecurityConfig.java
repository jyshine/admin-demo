package com.demo.admindemo.config.security;

import com.demo.admindemo.config.jwt.JwtAuthenticationFilter;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter.XFrameOptionsMode;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    @Value("${springdoc.api-docs.path}")
    private String restApiDocPath;
    @Value("${springdoc.swagger-ui.path}")
    private String swaggerPath;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .headers(
                        h -> h.addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsMode.SAMEORIGIN))
                ) .authorizeHttpRequests(it ->
                        it.requestMatchers(
                                new AntPathRequestMatcher("/auth/**"),
                                new AntPathRequestMatcher("/api/v1/guest"),
                                new AntPathRequestMatcher("/h2-console/**" ),
                                new AntPathRequestMatcher(restApiDocPath + "/**"),
                                new AntPathRequestMatcher(swaggerPath+"/**")
                        ).permitAll().anyRequest().authenticated()

                ).sessionManagement(
                        httpSecuritySessionManagementConfigurer ->
                                httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        ;

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        // CorsConfiguration 객체 생성
        CorsConfiguration configuration = new CorsConfiguration();
        // 자격 증명(크로스 도메인 요청에서 쿠키 및 HTTP 인증과 같은 자격 증명이 포함되도록 허용)
        configuration.setAllowCredentials(true);

        // 허용할 오리진(도메인) 목록 설정
        // 실제 운영 환경에서는 보안상의 이유로 가능한한 특정 도메인만을 명시
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080", "http://localhost:5173"));

        // 허용할 HTTP 메서드 목록 설정 (여기서는 GET과 POST만 허용)
        configuration.setAllowedMethods(Arrays.asList("GET", "POST"));

        // UrlBasedCorsConfigurationSource 객체 생성
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // 모든 경로에 대해 위에서 설정한 CorsConfiguration을 적용
        source.registerCorsConfiguration("/**", configuration);

        // 설정이 적용된 CorsConfigurationSource 반환
        return source;
    }
}
