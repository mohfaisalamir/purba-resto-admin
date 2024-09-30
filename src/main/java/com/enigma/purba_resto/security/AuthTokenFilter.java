package com.enigma.purba_resto.security;

import com.enigma.purba_resto.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component
@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserService userService;
    @Autowired
    public AuthTokenFilter(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            // Validasi token JWT
            String token = parseJwt(request);
            if (token != null && jwtUtil.verifyJwtToken(token)) {
                // Set authentication ke Spring Security
                setAuthentication(request, token);
            }
            // Lanjutkan filter ke controller/filter lainnya
            // filterChain.doFilter(request, response); // ini jangan ditaruh di dalam try, karena jika error tidak diteruskan di controller.. taruh diluar
        } catch (Exception e) {
            log.error("Cannot set user authentication :{}", e.getMessage());
        }
        //log.info("Masoook pak eko Request : {}", request.getHeader("User-Agent"));
        filterChain.doFilter(request, response);
    }
    private void setAuthentication(HttpServletRequest request, String token) {
        Map<String, String> userInfo = jwtUtil.getUserInfoByToken(token);
        // Mengambil UserDetails berdasarkan userId dari token
        UserDetails user = userService.loadUserByUserName(userInfo.get("userId"));
        // Validasi/authentication by token
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                user,
                null,  // Tidak memerlukan credentials untuk validasi by token
                user.getAuthorities()
        );
        // Menambahkan informasi tambahan berupa alamat IP, Host ke dalam Spring Security
        authenticationToken.setDetails(new WebAuthenticationDetails(request));
        // Menyimpan authentication ke Spring Security context
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}