package com.enigma.purba_resto.controller;

import com.enigma.purba_resto.dto.request.NewTableRequest;
import com.enigma.purba_resto.dto.request.UpdateTableRequest;
import com.enigma.purba_resto.dto.response.CommonResponse;
import com.enigma.purba_resto.dto.response.TableResponse;
import com.enigma.purba_resto.entity.Table;
import com.enigma.purba_resto.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tables")
public class TableController {
    private final TableService tableService;

    @Autowired
    public TableController(TableService tableService) {
        this.tableService = tableService;
    }
    @PostMapping
    public ResponseEntity<?> createNewTable(@RequestBody NewTableRequest request) {
        TableResponse tableResponse = tableService.createTable(request);
        CommonResponse<TableResponse> response = CommonResponse.<TableResponse>builder()
                .message("successfully create new table")
                .statusCode(HttpStatus.CREATED.value())
                .data(tableResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{name}")
    public ResponseEntity<?> getTableByName(@PathVariable String name) {
        TableResponse tableResponse = tableService.getOneTableByName(name);
        CommonResponse<TableResponse> response = CommonResponse.<TableResponse>builder()
                .message("successfully get table")
                .statusCode(HttpStatus.OK.value())
                .data(tableResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<?> getAllTable() {
        List<TableResponse> tableResponses = tableService.getAllTables();
        CommonResponse<List<TableResponse>> response = CommonResponse.<List<TableResponse>>builder()
                .message("successfully get table")
                .statusCode(HttpStatus.OK.value())
                .data(tableResponses)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @PutMapping
    public ResponseEntity<?> updateTable(@RequestBody UpdateTableRequest request) {
        TableResponse tableResponse = tableService.updateTable(request);
        CommonResponse<TableResponse> response = CommonResponse.<TableResponse>builder()
                .message("successfully get table")
                .statusCode(HttpStatus.OK.value())
                .data(tableResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTableById(@PathVariable String id) {
        tableService.deleteTable(id);
        CommonResponse<String> response = CommonResponse.<String>builder()
                .message("successfully get table")
                .statusCode(HttpStatus.OK.value())
                .data("OK")
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

}
