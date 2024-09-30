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
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createMenu(@RequestBody NewMenuRequest request) {
        //System.out.println("JIANCCCCOOKKKKKK" +request);
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
            @RequestParam(required = false, defaultValue = "name") String sortBy,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long minPrice,
            @RequestParam(required = false) Long maxPrice

    ) {
        page = PagingUtils.validatePage(page);
        size = PagingUtils.validateSize(size);
        direction = PagingUtils.validateDirection(direction);

        SearchMenuRequest request = SearchMenuRequest.builder()
                .page(page)
                .size(size)
                .direction(direction)
                .name(name)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
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
    @DeleteMapping("/bulk")
    public ResponseEntity<?> deleteBulk(@RequestParam List<String> ids) {
        try {
            // Panggil service untuk memvalidasi dan menghapus menu secara bulk
            menuService.deleteBulkMenus(ids);

            // Jika berhasil dihapus, kembalikan response sukses
            CommonResponse<?> response = CommonResponse.builder()
                    .message("Successfully deleted menus")
                    .statusCode(HttpStatus.OK.value())
                    .data("OK")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (ResponseStatusException e) {
            // Jika salah satu ID tidak ditemukan, kembalikan response 404
            CommonResponse<?> response = CommonResponse.builder()
                    .message(e.getMessage())
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (IllegalStateException e) {
            // Jika ada validasi yang gagal, kembalikan response 400
            CommonResponse<?> response = CommonResponse.builder()
                    .message(e.getMessage())
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            // Untuk error lain yang tidak terduga, kembalikan response 500
            CommonResponse<?> response = CommonResponse.builder()
                    .message("An error occurred while deleting the menus")
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}