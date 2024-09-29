package com.enigma.purba_resto.service.impl;

import com.enigma.purba_resto.constant.ERole;
import com.enigma.purba_resto.dto.request.AuthRequest;
import com.enigma.purba_resto.dto.response.LoginResponse;
import com.enigma.purba_resto.dto.response.RegisterResponse;
import com.enigma.purba_resto.entity.AppUser;
import com.enigma.purba_resto.entity.Customer;
import com.enigma.purba_resto.entity.Role;
import com.enigma.purba_resto.entity.UserCredential;
import com.enigma.purba_resto.repository.UserCredentialRepository;
import com.enigma.purba_resto.security.JwtUtil;
import com.enigma.purba_resto.service.AuthService;
import com.enigma.purba_resto.service.CustomerService;
import com.enigma.purba_resto.service.RoleService;
import com.enigma.purba_resto.util.ValidationUtil;
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
public class AuthServiceImpl implements AuthService {
    private final UserCredentialRepository userCredentialRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomerService customerService;
    private final RoleService roleService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final ValidationUtil validationUtil;

    @Autowired
    public AuthServiceImpl(UserCredentialRepository userCredentialRepository, PasswordEncoder passwordEncoder, CustomerService customerService, RoleService roleService, JwtUtil jwtUtil, AuthenticationManager authenticationManager, ValidationUtil validationUtil) {
        this.userCredentialRepository = userCredentialRepository;
        this.passwordEncoder = passwordEncoder;
        this.customerService = customerService;
        this.roleService = roleService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.validationUtil = validationUtil;
    }

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
                    .username(authRequest.getUsername().toLowerCase())
                    //.password(authRequest.getPassword()) // ini jika tanpa di hashing, bahaya,, bisa bisa di hack akun customer
                    .password(passwordEncoder.encode(authRequest.getPassword()))
                    .role(savedRole)
                    .build();
            // Simpan UserCredential terlebih dahulu
            UserCredential userCredentialSaved = userCredentialRepository.saveAndFlush(userCredential);


            // Set role ke UserCredential yang sudah disimpan
            userCredentialSaved.setRole(savedRole);

            // Update UserCredential setelah role di-set (cukup panggil saveAndFlush satu kali lagi)
            userCredentialRepository.saveAndFlush(userCredentialSaved);

            // Buat customer dengan user credential yang sudah lengkap
            Customer customer = Customer.builder()
                    .userCredential(userCredentialSaved)
                    .build();
            customerService.createNewCustomer(customer);

            // Return response
            return RegisterResponse.builder()
                    .username(userCredentialSaved.getUsername())
                    .role(userCredentialSaved.getRole().getName().toString())
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
