package com.enigma.purba_resto.controller;

import com.enigma.purba_resto.dto.request.AuthRequest;
import com.enigma.purba_resto.dto.response.CommonResponse;
import com.enigma.purba_resto.dto.response.LoginResponse;
import com.enigma.purba_resto.dto.response.RegisterResponse;
import com.enigma.purba_resto.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    public final AuthService authService;
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping("/register/customer")
    public ResponseEntity<?> registerCustomer(@RequestBody AuthRequest authRequest) {
        RegisterResponse registerResponse = authService.registerCustomer(authRequest);
        CommonResponse<RegisterResponse> response = CommonResponse.<RegisterResponse>builder()
                .message("Customer registered successfully")
                .statusCode(HttpStatus.CREATED.value())
                .data(registerResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    @PostMapping("/register/admin")
    // @PreAuthorize("hasRole('ADMIN_ROLE')")
    public ResponseEntity<?> registerAdmin(@RequestBody AuthRequest authRequest) {
        RegisterResponse registerResponse = authService.registerAdmin(authRequest);
        CommonResponse<RegisterResponse> response = CommonResponse.<RegisterResponse>builder()
                .message("Admin registered successfully")
                .statusCode(HttpStatus.CREATED.value())
                .data(registerResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        LoginResponse login = authService.login(authRequest);
        CommonResponse<LoginResponse> response = CommonResponse.<LoginResponse>builder()
                .message("Customer login successfully")
                .statusCode(HttpStatus.OK.value())
                .data(login)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);

    }
}
