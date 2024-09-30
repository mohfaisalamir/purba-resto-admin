package com.enigma.purba_resto.service.impl;

import com.enigma.purba_resto.entity.AppUser;
import com.enigma.purba_resto.entity.UserCredential;
import com.enigma.purba_resto.repository.UserCredentialRepository;
import com.enigma.purba_resto.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserCredentialRepository userCredentialRepository;

    @Autowired
    public UserServiceImpl(UserCredentialRepository userCredentialRepository) {
        this.userCredentialRepository = userCredentialRepository;
    }

    // verifikasi JWT
    @Override
    public AppUser loadUserByUserName(String name) {
        UserCredential userCredential = userCredentialRepository.findById(name)
                .orElseThrow(() -> new UsernameNotFoundException(name + " invalid credential "));
        return AppUser.builder()
                .id(userCredential.getId())
                .username(userCredential.getUsername())
                .password(userCredential.getPassword())
                .role(userCredential.getRole().getName())
                .build();
    }
    // verifikasi Authentifikasi Login - loadUserByUsername ini dipanggil saat authenticate
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserCredential userCredential = userCredentialRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + " invalid credential "));
        return AppUser.builder()
                .id(userCredential.getId())
                .username(userCredential.getUsername())
                .password(userCredential.getPassword())
                .role(userCredential.getRole().getName())
                .build();
    }
}
