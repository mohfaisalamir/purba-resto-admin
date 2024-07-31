package com.enigma.purba_resto.service;

import com.enigma.purba_resto.entity.Table;

import java.util.List;

public interface TableService {
    Table createTable(Table table);
    List<Table> createTablesBulk(List<Table> tables);
    Table updateTable(Table table);
    Table getTableByName(String name);
    List<Table> getAllTables();
    void deleteTable(String id);
}
