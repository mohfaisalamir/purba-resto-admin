package com.enigma.purba_resto.service;

import com.enigma.purba_resto.dto.request.NewTableRequest;
import com.enigma.purba_resto.dto.request.UpdateTableRequest;
import com.enigma.purba_resto.dto.response.TableResponse;
import com.enigma.purba_resto.entity.Table;

import java.util.List;

public interface TableService {
    TableResponse createTable(NewTableRequest request);
    TableResponse updateTable(UpdateTableRequest request);
    Table getTableByName(String name);
    TableResponse getOneTableByName(String name);
    List<TableResponse> getAllTables();
    void deleteTable(String id);
}
