package com.enigma.purba_resto.controller;

import com.enigma.purba_resto.dto.request.NewMenuRequest;
import com.enigma.purba_resto.dto.request.SearchMenuRequest;
import com.enigma.purba_resto.dto.request.UpdateMenuRequest;
import com.enigma.purba_resto.dto.response.CommonResponse;
import com.enigma.purba_resto.dto.response.MenuResponse;
import com.enigma.purba_resto.dto.response.PagingResponse;
import com.enigma.purba_resto.entity.Menu;
import com.enigma.purba_resto.repository.MenuRepository;
import com.enigma.purba_resto.service.MenuService;
import com.enigma.purba_resto.util.PagingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class MenuController {
    private final MenuService menuService;
    @Autowired
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }



    @PostMapping
    public ResponseEntity<?> createMenu(@RequestBody NewMenuRequest request) {
        MenuResponse menuResponse = menuService.createMenu(request);
        CommonResponse<MenuResponse> response = CommonResponse.<MenuResponse>builder()
                .message("successfully create new menu")
                .statusCode(HttpStatus.CREATED.value())
                .data(menuResponse).build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/bulk")
    public ResponseEntity<?> bulk(@RequestBody List<NewMenuRequest> menus) {
        List<MenuResponse> menuResponses = menuService.createMenusBulk(menus);
        CommonResponse<List<MenuResponse>> response = CommonResponse.<List<MenuResponse>>builder()
                .message("successfully create new menus")
                .statusCode(HttpStatus.CREATED.value())
                .data(menuResponses)
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<?> findAll(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false, defaultValue = "asc") String direction,
            @RequestParam(required = false, defaultValue = "name") String sortBy

    ) {
        page = PagingUtils.validatePage(page);
        size = PagingUtils.validateSize(size);
        direction = PagingUtils.validateDirection(direction);
        SearchMenuRequest request = SearchMenuRequest.builder()
                .page(page)
                .size(size)
                .direction(direction)
                .sortBy(sortBy).build();
        Page<MenuResponse> menuResponses = menuService.getAllMenus(request);
        PagingResponse pagingResponse = PagingResponse.builder()
                .count(menuResponses.getTotalElements())
                .totalPages(menuResponses.getTotalPages())
                .page(page)
                .size(size)
                .build();
        CommonResponse<List<MenuResponse>> response = CommonResponse.<List<MenuResponse>>builder()
                .message("Succesfully get all menu")
                .statusCode(HttpStatus.OK.value())
                .data(menuResponses.getContent())
                .paging(pagingResponse).build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findMenuById(@PathVariable String id) {
        MenuResponse menuResponse = menuService.getOne(id);
        CommonResponse<MenuResponse> response = CommonResponse.<MenuResponse>builder()
                .message("successfully get menu by id")
                .statusCode(HttpStatus.OK.value())
                .data(menuResponse).build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody UpdateMenuRequest request) {
        MenuResponse menuResponse = menuService.updateMenu(request);
        CommonResponse<MenuResponse> response = CommonResponse.<MenuResponse>builder()
                .message("successfully update menu")
                .statusCode(HttpStatus.OK.value())
                .data(menuResponse).build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        menuService.deleteMenu(id);
        CommonResponse<?> response = CommonResponse.builder()
                .message("successfully delete menu")
                .statusCode(HttpStatus.OK.value())
                .data("OK")
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

}