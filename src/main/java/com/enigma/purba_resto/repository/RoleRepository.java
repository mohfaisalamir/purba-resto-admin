package com.enigma.purba_resto.repository;

import com.enigma.purba_resto.constant.ERole;
import com.enigma.purba_resto.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByName(ERole name);
}
