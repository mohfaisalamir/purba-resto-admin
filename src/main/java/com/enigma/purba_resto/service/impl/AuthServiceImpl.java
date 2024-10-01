package com.enigma.purba_resto.service.impl;

import com.enigma.purba_resto.constant.ERole;
import com.enigma.purba_resto.dto.request.AuthRequest;
import com.enigma.purba_resto.dto.request.NewCustomerRequest;
import com.enigma.purba_resto.dto.response.LoginResponse;
import com.enigma.purba_resto.dto.response.RegisterResponse;
import com.enigma.purba_resto.entity.*;
import com.enigma.purba_resto.repository.UserCredentialRepository;
import com.enigma.purba_resto.security.JwtUtil;
import com.enigma.purba_resto.service.AdminService;
import com.enigma.purba_resto.service.AuthService;
import com.enigma.purba_resto.service.CustomerService;
import com.enigma.purba_resto.service.RoleService;
import com.enigma.purba_resto.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserCredentialRepository userCredentialRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomerService customerService;
    private final RoleService roleService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final ValidationUtil validationUtil;
    private final AdminService adminService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public RegisterResponse registerCustomer(AuthRequest authRequest) {
        try {
            validationUtil.validate(authRequest);
            // Ambil atau simpan role
            Role role = Role.builder()
                    .name(ERole.ROLE_CUSTOMER).build();
            Role savedRole = roleService.getOrSave(role);
            // Buat user credential
            UserCredential userCredential = UserCredential.builder()
                    .username(authRequest.getUsername())
                    //.password(authRequest.getPassword()) // ini jika tanpa di hashing, bahaya,, bisa bisa di hack akun customer
                    .password(passwordEncoder.encode(authRequest.getPassword()))
                    .role(savedRole)
                    .build();
            // Simpan UserCredential terlebih dahulu
            userCredentialRepository.saveAndFlush(userCredential);
/*
            // Set role ke UserCredential yang sudah disimpan
            userCredentialSaved.setRole(savedRole);
            // Update UserCredential setelah role di-set (cukup panggil saveAndFlush satu kali lagi)
            userCredentialRepository.saveAndFlush(userCredentialSaved);
            // Buat customer dengan user credential yang sudah lengkap
*/
            // CUSTOMER
            NewCustomerRequest customer = NewCustomerRequest.builder()
                    .userCredentialId(userCredential.getId())
                    .build();
            customerService.createNewCustomer(customer);

            // Return response
            return RegisterResponse.builder()
                    .username(userCredential.getUsername())
                    .role(userCredential.getRole().getName().toString())
                    .build();

        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "username already exists");
        }
    }

    @Override
    public RegisterResponse registerAdmin(AuthRequest authRequest) {
        try {
            validationUtil.validate(authRequest);
            // Ambil atau simpan role
            Role role = Role.builder()
                    .name(ERole.ROLE_ADMIN).build();
            Role savedRole = roleService.getOrSave(role);
            // Buat user credential
            UserCredential userCredential = UserCredential.builder()
                    .username(authRequest.getUsername())
                    //.password(authRequest.getPassword()) // ini jika tanpa di hashing, bahaya,, bisa bisa di hack akun customer
                    .password(passwordEncoder.encode(authRequest.getPassword()))
                    .role(savedRole)
                    .build();
            // Simpan UserCredential terlebih dahulu
            userCredentialRepository.saveAndFlush(userCredential);
            //ADMIN
            Admin admin= Admin.builder()
                    .userCredential(userCredential)
                    .build();
            adminService.createNew(admin);

            // Return response
            return RegisterResponse.builder()
                    .username(userCredential.getUsername())
                    .role(userCredential.getRole().getName().toString())
                    .build();

        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "username already exists");
        }
    }

    @Override
    public LoginResponse login(AuthRequest request) {
        // ini daalah tempat logika LOGIN
        validationUtil.validate(request);
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername().toLowerCase(),
                request.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        // object AppUser
        AppUser appUser = (AppUser) authenticate.getPrincipal(); // Object casting to AppUser, ini menngandung id, jika casting ke Object, ini tidak ada id
        String token = jwtUtil.generateToken(appUser);

        return LoginResponse.builder()
                .role(appUser.getRole().name())
                .token(token)
                .build();
    }

}
