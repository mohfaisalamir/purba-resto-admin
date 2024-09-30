package com.enigma.purba_resto.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true
)
public class SecurityConfiguration {
    private final AuthTokenFilter authTokenFilter;
    private final AuthEntryPoint authEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    @Autowired
    public SecurityConfiguration(AuthTokenFilter authTokenFilter, AuthEntryPoint authEntryPoint, CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.authTokenFilter = authTokenFilter;
        this.authEntryPoint = authEntryPoint;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }
    // Authentication Manager -> digunakan untuk cek Authenticaton (password,username) / token
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Menonaktifkan CSRF
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception ->exception
                        .authenticationEntryPoint(authEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)) // Tambahkan CustomAccessDeniedHandler)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll() // Memperbolehkan akses ke /api/auth/**
                        .anyRequest().authenticated() // Memastikan semua request lainnya harus diautentikasi
                )
                .httpBasic(withDefaults()) // Mengaktifkan otentikasi HTTP Basic
                .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class); // Menambahkan filter custom

        return http.build(); // Mengembalikan SecurityFilterChain
    }
}
