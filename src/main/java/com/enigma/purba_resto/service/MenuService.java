package com.enigma.purba_resto.service;

import com.enigma.purba_resto.entity.Customer;
import com.enigma.purba_resto.entity.Menu;
import org.springframework.stereotype.Service;

import java.util.List;

public interface MenuService {
    Menu createMenu(Menu customer);
    List<Menu> createMenusBulk(List<Menu> menus);
    Menu updateMenu(Menu customer);
    Menu getMenuById(String id);
    List<Menu> getAllMenus(String name, Long min, Long max);
    void deleteMenu(String id);
}
