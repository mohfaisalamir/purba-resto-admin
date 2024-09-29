package com.enigma.purba_resto.service.impl;

import com.enigma.purba_resto.entity.Role;
import com.enigma.purba_resto.repository.RoleRepository;
import com.enigma.purba_resto.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {
    final RoleRepository roleRepository;
    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role getOrSave(Role role) {
        // Jika ada kita get dari DB
        Optional<Role> optionalRole = roleRepository.findByName(role.getName());
        if (!optionalRole.isEmpty()) {
            return optionalRole.get();
        }
        // Jika tidak ada kita create baru
        return roleRepository.save(role);
    }

}
