package com.enigma.purba_resto.service.impl;

import com.enigma.purba_resto.dto.request.NewMenuRequest;
import com.enigma.purba_resto.dto.request.SearchMenuRequest;
import com.enigma.purba_resto.dto.request.UpdateMenuRequest;
import com.enigma.purba_resto.dto.response.MenuResponse;
import com.enigma.purba_resto.entity.Menu;
import com.enigma.purba_resto.repository.MenuRepository;
//import jakarta.transaction.Transactional;
import com.enigma.purba_resto.util.ValidationUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl implements com.enigma.purba_resto.service.MenuService {
    private final MenuRepository menuRepository;
    private final ValidationUtil validationUtil;
    @Autowired
    public MenuServiceImpl(MenuRepository menuRepository, ValidationUtil validationUtil) {
        this.menuRepository = menuRepository;
        this.validationUtil = validationUtil;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public MenuResponse createMenu(NewMenuRequest request) {
        validationUtil.validate(request);
        Menu menu = Menu.builder()
                .name(request.getName())
                .price(request.getPrice())
                .build();
        menuRepository.saveAndFlush(menu);
        return mapToResponse(menu);
    }

    @Override
    public List<MenuResponse> createMenusBulk(List<NewMenuRequest> requests) {
        List<Menu> menus = requests.stream().map(request -> Menu.builder()
                .name(request.getName())
                .price(request.getPrice())
                .build()).collect(Collectors.toList());
        menuRepository.saveAllAndFlush(menus);
        return menus.stream().map(menu -> mapToResponse(menu)).collect(Collectors.toList());
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public MenuResponse updateMenu(UpdateMenuRequest request) {
        Menu menu = findByIdOrThrowNotFound(request.getId());
        menu.setName(request.getName());
        menu.setPrice(request.getPrice());
        menuRepository.saveAndFlush(menu);
        return mapToResponse(menu);
    }
    @Transactional(readOnly = true)
    @Override
    public Menu getMenuById(String id) {
        return findByIdOrThrowNotFound(id);
    }
    @Transactional(readOnly = true)
    @Override
    public MenuResponse getOne(String id) {
        Menu menu = findByIdOrThrowNotFound(id);
        return mapToResponse(menu);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<MenuResponse> getAllMenus(SearchMenuRequest request) {
        /*if(name == null && min == null && max == null){
           return menuRepository.findAll().stream().map(menu -> mapToResponse(menu)).collect(Collectors.toList());
        }
         if (minPrice == null) {
        minPrice = 0L;
    }

        if (maxPrice == null) {
        maxPrice = Long.MAX_VALUE;
    }
    List<Menu> menus = menuRepository.findAllByNameLikeIgnoreCaseOrPriceBetween("%" + name + "%", minPrice, maxPrice);
         */
        Sort.Direction direction = Sort.Direction.fromString(request.getDirection());
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(), direction, request.getSortBy());
        Page<Menu> menus = menuRepository.findAll(pageable);

        System.out.println(menus);

        return menus.map(menu -> mapToResponse(menu));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteMenu(String id) {
        Menu menu = findByIdOrThrowNotFound(id);
        menuRepository.delete(menu);
    }
    private Menu findByIdOrThrowNotFound(String id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "menu not found"));
    }

    private MenuResponse mapToResponse(Menu menu) {
        return MenuResponse.builder()
                .id(menu.getId())
                .name(menu.getName())
                .price(menu.getPrice())
                .build();
    }

}
