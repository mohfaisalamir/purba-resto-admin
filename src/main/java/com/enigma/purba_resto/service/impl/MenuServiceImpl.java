package com.enigma.purba_resto.service.impl;

import com.enigma.purba_resto.dto.request.NewMenuRequest;
import com.enigma.purba_resto.dto.request.SearchMenuRequest;
import com.enigma.purba_resto.dto.request.UpdateMenuRequest;
import com.enigma.purba_resto.dto.response.FileResponse;
import com.enigma.purba_resto.dto.response.MenuResponse;
import com.enigma.purba_resto.entity.Menu;
import com.enigma.purba_resto.entity.MenuImage;
import com.enigma.purba_resto.repository.MenuRepository;
//import jakarta.transaction.Transactional;
import com.enigma.purba_resto.service.MenuImageService;
import com.enigma.purba_resto.util.ValidationUtil;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements com.enigma.purba_resto.service.MenuService {
    private final MenuRepository menuRepository;
    private final ValidationUtil validationUtil;
    private final MenuImageService menuImageService;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public MenuResponse createMenu(NewMenuRequest request) {
        validationUtil.validate(request);
        // UPLOAD FILE
        MenuImage fileMenuImage = menuImageService.createFile(request.getMultipartFile());

        // create Menu
        Menu menu = Menu.builder()
                .name(request.getName())
                .price(request.getPrice())
                .menuImage(fileMenuImage)
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
        validationUtil.validate(request);
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
        menuImageService.findFileByPath(menu.getMenuImage().getPath());
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
        /*Specification<Menu> specification = new Specification<Menu>() {
            @Override
            public Predicate toPredicate(Root<Menu> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {*/
        Specification<Menu> specification = getMenuSpecification(request);
        Sort.Direction direction = Sort.Direction.fromString(request.getDirection());
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize(), direction, request.getSortBy());
        Page<Menu> menus = menuRepository.findAll(specification, pageable);
        return menus.map(menu -> mapToResponse(menu));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteMenu(String id) {
        Menu menu = findByIdOrThrowNotFound(id);
        menuRepository.delete(menu);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteBulkMenus(List<String> ids) {
        List<Menu> existingMenus = menuRepository.findAllById(ids);
        if (existingMenus.size() != ids.size()) {
            // Tangani ID yang tidak ditemukan, misalnya, lemparkan exception atau buat log
            throw new RuntimeException("Some IDs not found");
        }
        menuRepository.deleteAll(existingMenus);
    }

    @Override
    public Resource getMenuImageById(String id) {
        Menu menu = findByIdOrThrowNotFound(id);
        org.springframework.core.io.Resource fileByPath = menuImageService.findFileByPath(menu.getMenuImage().getPath());
        return fileByPath;
    }

    private Menu findByIdOrThrowNotFound(String id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "menu not found"));
    }

    private MenuResponse mapToResponse(Menu menu) {
        FileResponse fileResponse = FileResponse.builder()
                .fileName(menu.getMenuImage().getName())
                .url("/api/menu/" + menu.getId() + "/image")
                .build();
        return MenuResponse.builder()
                .id(menu.getId())
                .name(menu.getName())
                .price(menu.getPrice())
                .image(fileResponse)
                .build();
    }


    private Specification<Menu> getMenuSpecification(SearchMenuRequest request) {
        return  (root, query, criteriaBuilder ) -> {
            // criteriaBuilder ==> operator (<,>,=,!= ,<=, >=)
            // criteriaQuery  ==> select, where
            // SELECT  m.name, m.price
            // root ==> representasi dari entity(property) ==> menu.name
            List<Predicate> predicates = new ArrayList<>();
            //Search by name (equal)
            if(request.getName()!=null){
                Predicate name = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")), "%" + request.getName().toLowerCase() + "%");
                //equal(root.get("name").as(String.class), request.getName());
                predicates.add(name);
            }
            // search by min price(greatherThan)
            if (request.getMinPrice()!=null){
                Predicate price = criteriaBuilder.greaterThanOrEqualTo(root.get("price"), request.getMinPrice());
                predicates.add(price);
            }
            // search by max price(lesstherThan)
            if (request.getMaxPrice()!=null){
                Predicate price = criteriaBuilder.lessThanOrEqualTo(root.get("price"), request.getMaxPrice());
                predicates.add(price);
            }
            // kenapa pakai if if if?, karena jika pakai else if, maka ada yang gak dapet/ada yang dilewati
            return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
        };
    }
}
