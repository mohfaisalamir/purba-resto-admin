package com.enigma.purba_resto.security;

import com.enigma.purba_resto.dto.response.CommonResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jmx.support.ObjectNameManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
//mengubah response jika terjadi error authenticationException
public class AuthEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;
    @Autowired
    public AuthEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        CommonResponse<String> commonResponse = CommonResponse.<String>builder()
                .message(authException.getMessage())
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .build();
        // mengubah object menjadi jsonString
        String commonResponseString = objectMapper.writeValueAsString(commonResponse);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(commonResponseString);
    }
}
