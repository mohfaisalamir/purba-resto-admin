package com.enigma.purba_resto.service.impl;

import com.enigma.purba_resto.dto.request.NewTableRequest;
import com.enigma.purba_resto.dto.request.UpdateTableRequest;
import com.enigma.purba_resto.dto.response.TableResponse;
import com.enigma.purba_resto.entity.Table;
import com.enigma.purba_resto.repository.TableRepository;
import com.enigma.purba_resto.service.TableService;
import com.enigma.purba_resto.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TableServiceImpl implements TableService {
    private final ValidationUtil validationUtil;
    private final TableRepository tableRepository;
    @Autowired
    public TableServiceImpl(ValidationUtil validationUtil, TableRepository tableRepository) {
        this.validationUtil = validationUtil;
        this.tableRepository = tableRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TableResponse createTable(NewTableRequest request) {
        try {
            validationUtil.validate(request);
            validationUtil.validate(request);
            Table table = Table.builder()
                    .name(request.getName())
                    .build();
            tableRepository.saveAndFlush(table);
            return mapToResponse(table);
        }catch (DataIntegrityViolationException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Table data already exists");
        }
        // save mirip persist jika di JPA Hibernate
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public TableResponse updateTable(UpdateTableRequest request) {
        try {
            validationUtil.validate(request);
            Table table = findByIdOrThrowNotFound(request.getId());
            table.setName(request.getName());
            tableRepository.saveAndFlush(table);
            return mapToResponse(table);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Table data already exists");
        }
    }
    @Transactional(readOnly = true)
    @Override
    public Table getTableByName(String name) {
        return tableRepository.findByName(name).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Table not found su"));
    }

    @Override
    public TableResponse getOneTableByName(String name) {
        Table table = tableRepository.findByName(name)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Table not found su"));
        return mapToResponse(table);
    }


    @Override
    public List<TableResponse> getAllTables() {
        List<TableResponse> collect = tableRepository.findAll().stream().map(table -> mapToResponse(table)).collect(Collectors.toList());
        return collect;

    }

    @Override
    public void deleteTable(String id) {
        Table table = findByIdOrThrowNotFound(id);
        tableRepository.delete(table);
    }

    private Table findByIdOrThrowNotFound(String id) {
        return tableRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "table not found, su"));
    }

    private TableResponse mapToResponse(Table table) {
        return TableResponse.builder()
                .id(table.getId())
                .name(table.getName())
                .build();
    }

}
