package com.enigma.purba_resto.service;

import com.enigma.purba_resto.dto.request.NewMenuRequest;
import com.enigma.purba_resto.dto.request.SearchMenuRequest;
import com.enigma.purba_resto.dto.request.UpdateMenuRequest;
import com.enigma.purba_resto.dto.response.MenuResponse;
import com.enigma.purba_resto.entity.Menu;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MenuService {
    MenuResponse createMenu(NewMenuRequest customer);
    List<MenuResponse> createMenusBulk(List<NewMenuRequest> menus);
    MenuResponse updateMenu(UpdateMenuRequest request);
    Menu getMenuById(String id); // untuk internal perusahaan karena Menu memuat lengkap, sedangkan MenuResponse untuk client
    MenuResponse getOne(String id);
    Page<MenuResponse> getAllMenus(SearchMenuRequest request);
    void deleteMenu(String id);
    void deleteBulkMenus(List<String> ids);
}
