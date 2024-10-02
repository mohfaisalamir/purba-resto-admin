package com.enigma.purba_resto.repository;

import com.enigma.purba_resto.entity.MenuImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuImageRepository extends JpaRepository<MenuImage, Long> {
}
