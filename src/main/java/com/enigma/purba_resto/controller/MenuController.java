package com.enigma.purba_resto.controller;

import com.enigma.purba_resto.entity.Menu;
import com.enigma.purba_resto.repository.MenuRepository;
import com.enigma.purba_resto.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/menu")
public class MenuController {
    private final MenuService menuService;

    @Autowired
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping
    public Menu createNew(@RequestBody Menu menu) {
        return menuService.createMenu(menu);
    }
    @PostMapping("/bulk")
    public List<Menu> bulk(@RequestBody List<Menu> menuList) {
        return menuService.createMenusBulk(menuList);
    }

    @GetMapping
    public List<Menu> findAll(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long min,
            @RequestParam(required = false) Long max
    ) {
        return menuService.getAllMenus(name,min,max);
    }

    @GetMapping("/{id}")
    public Menu findById(@PathVariable String id) {
        return menuService.getMenuById(id);

    }

    @PutMapping("/{id}")
    public Menu update(@RequestBody Menu menu) {
        return menuService.updateMenu(menu);
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        menuService.deleteMenu(id);
    }
}
