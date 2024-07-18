package com.enigma.purba_resto.repository;

import com.enigma.purba_resto.entity.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TableRepository extends JpaRepository<Table, String> {
    Optional<Table> findByName(String name);
}
