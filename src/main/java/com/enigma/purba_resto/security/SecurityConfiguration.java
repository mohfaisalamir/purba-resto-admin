package com.enigma.purba_resto.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final AuthTokenFilter authTokenFilter;
    @Autowired
    public SecurityConfiguration(AuthTokenFilter authTokenFilter) {
        this.authTokenFilter = authTokenFilter;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        /*return http.httpBasic().and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/auth/**").permitAll()
                .anyRequest().authenticated()
                .and().addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();*/
        // atau

        http
                .csrf(csrf -> csrf.disable()) // Menonaktifkan CSRF
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll() // Memperbolehkan akses ke /api/auth/**
                        .anyRequest().authenticated() // Memastikan semua request lainnya harus diautentikasi
                )
                .httpBasic(withDefaults()) // Mengaktifkan otentikasi HTTP Basic
                .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class); // Menambahkan filter custom

        return http.build(); // Mengembalikan SecurityFilterChain

    }
}
