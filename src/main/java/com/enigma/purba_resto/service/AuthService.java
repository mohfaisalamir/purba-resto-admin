package com.enigma.purba_resto.service;

import com.enigma.purba_resto.dto.request.AuthRequest;
import com.enigma.purba_resto.dto.response.LoginResponse;
import com.enigma.purba_resto.dto.response.RegisterResponse;

public interface AuthService {
    RegisterResponse registerCustomer(AuthRequest request);
    RegisterResponse registerAdmin(AuthRequest request);
    LoginResponse login(AuthRequest request);
}
