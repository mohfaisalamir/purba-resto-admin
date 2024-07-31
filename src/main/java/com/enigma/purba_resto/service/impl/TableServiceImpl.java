package com.enigma.purba_resto.service.impl;

import com.enigma.purba_resto.entity.Table;
import com.enigma.purba_resto.repository.TableRepository;
import com.enigma.purba_resto.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class TableServiceImpl implements TableService {
    private final TableRepository tableRepository;
    @Autowired
    public TableServiceImpl(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    @Override
    public Table createTable(Table table) {
        try {
            Table saved = tableRepository.save(table);
            return saved;
        }catch (DataIntegrityViolationException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Table data already exists");
        }
        // save mirip persist jika di JPA Hibernate
    }
    @Override
    public List<Table> createTablesBulk(List<Table> tables) {
        return tableRepository.saveAll(tables);
    }

    @Override
    public Table updateTable(Table table) {
        try {
            findByIdOrThrowNotFound(table.getId());
            return tableRepository.save(table);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Table data already exists");
        }
    }

    @Override
    public Table getTableByName(String name) {
        return tableRepository.findByName(name).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Table not found su"));
    }

    @Override
    public List<Table> getAllTables() {
        List<Table> all = tableRepository.findAll();
        return all;
    }

    @Override
    public void deleteTable(String id) {
        Table byIdOrThrowNotFound = findByIdOrThrowNotFound(id);
        tableRepository.delete(byIdOrThrowNotFound);
    }

    private Table findByIdOrThrowNotFound(String id) {
        Optional<Table> byId = tableRepository.findById(id);
        return byId.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Table not found su"));
    }
}
