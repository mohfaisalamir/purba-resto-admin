package com.enigma.purba_resto.service;

import com.enigma.purba_resto.entity.AppUser;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    AppUser loadUserByUserName(String name);
}
