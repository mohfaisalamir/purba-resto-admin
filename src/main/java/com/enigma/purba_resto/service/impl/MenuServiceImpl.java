package com.enigma.purba_resto.service.impl;

import com.enigma.purba_resto.entity.Menu;
import com.enigma.purba_resto.repository.MenuRepository;
import com.enigma.purba_resto.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MenuServiceImpl implements MenuService {
    private final MenuRepository menuRepository;
    @Autowired
    public MenuServiceImpl(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    @Override
    public Menu createMenu(Menu menu) {
        try {
            Menu saved = menuRepository.save(menu);
            return saved;
        }catch (DataIntegrityViolationException e){
            throw new DataIntegrityViolationException("Menu data already exists");
        }
    }

    @Override
    public List<Menu> createMenusBulk(List<Menu> menus) {
        return menuRepository.saveAll(menus);
    }

    @Override
    public Menu updateMenu(Menu menu) {
        findByIdOrThrowNotFound(menu.getId());
        return menuRepository.save(menu);
    }

    @Override
    public Menu getMenuById(String id) {
        return findByIdOrThrowNotFound(id);
    }

    @Override
    public List<Menu> getAllMenus(String name, Long min, Long max) {
        /*if(name == null || min == null || max == null){
           return menuRepository.findAll();
        }*/

        return menuRepository.getMenuByNameIgnoreCaseContainingAndPriceBetween(name,min,max);
    }

    @Override
    public void deleteMenu(String id) {
        Menu byIdOrThrowNotFound = findByIdOrThrowNotFound(id);
        menuRepository.delete(byIdOrThrowNotFound);
    }

    private Menu findByIdOrThrowNotFound(String id) {
        Optional<Menu> byId = menuRepository.findById(id);
        return byId.orElseThrow(() -> new RuntimeException("Menu not found"));
    }
}
