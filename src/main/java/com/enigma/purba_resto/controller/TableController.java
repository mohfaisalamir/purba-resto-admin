package com.enigma.purba_resto.controller;

import com.enigma.purba_resto.entity.Table;
import com.enigma.purba_resto.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/table")
public class TableController {
    private final TableService tableService;

    @Autowired
    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    @PostMapping
    public Table createNew(@RequestBody Table table) {
        return tableService.createTable(table);
    }
    @PostMapping("/bulk")
    public List<Table> bulk(@RequestBody List<Table> tableList) {
        return tableService.createTablesBulk(tableList);
    }

    @GetMapping
    public List<Table> findAll() {
        return tableService.getAllTables();
    }

    @GetMapping("/{name}")
    public Table findByName(@PathVariable String name) {
        return tableService.getTableByName(name);
        /*Optional<Table> byId = tableRepository.findById(id);
        return byId.orElseThrow(() -> new RuntimeException("Table not found"));*/
        //atau
        /*if (byId.isEmpty()) {
            throw new RuntimeException("Table not found");
        }
        return byId.get();*/
        // atau
        /*if (byId.isPresent()) {
            return byId.get();
        }else {
            throw new RuntimeException("Table not found");
        }*/
    }

    @PutMapping
    public Table update(@RequestBody Table table) {
        return tableService.updateTable(table);
    }
    /*public Table updateTable(@PathVariable String id, @RequestBody Table table) {
        Optional<Table> byId = tableRepository.findById(id);
        if (byId.isPresent()) {
            Table existingTable = byId.get();
            table.setId(existingTable.getId());
            return tableRepository.save(table);
        } else {
            throw new RuntimeException("Table not found with id: " + id);
        }
    }*/ // atau
    /*public Table updateTable(@RequestBody Table table) {
        Optional<Table> byId = tableRepository.findById(table.getId());
        if (byId.isPresent()) {
            return tableRepository.save(table);
        }else {
            throw new RuntimeException("Table not found");
        }
    }*/
    // atau
    /*public Table updateTable(@RequestBody Table table) {
        Optional<Table> byId = tableRepository.findById(table.getId());
        if (byId.isEmpty()) {
            throw new RuntimeException("Table not found");
        }
        return tableRepository.save(table);
    }*/
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        tableService.deleteTable(id);
    }
}
