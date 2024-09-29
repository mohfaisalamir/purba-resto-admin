package com.enigma.purba_resto.service;

import com.enigma.purba_resto.entity.Role;

public interface RoleService {
    Role getOrSave(Role role);
}
