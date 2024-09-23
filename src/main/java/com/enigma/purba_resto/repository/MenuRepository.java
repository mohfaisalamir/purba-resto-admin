package com.enigma.purba_resto.repository;

import com.enigma.purba_resto.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
//findByNameIgnoreCaseContainingAndPriceBetween
public interface MenuRepository extends JpaRepository<Menu, String>, JpaSpecificationExecutor<Menu> {
    List<Menu> getMenuByNameIgnoreCaseContainingAndPriceBetween(String name, Long minPrice, Long maxPrice);
}
