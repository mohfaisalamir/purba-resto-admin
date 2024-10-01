package com.enigma.purba_resto.service.impl;

import com.enigma.purba_resto.dto.request.UpdateAdminRequest;
import com.enigma.purba_resto.dto.response.AdminResponse;
import com.enigma.purba_resto.entity.Admin;
import com.enigma.purba_resto.repository.AdminRepository;
import com.enigma.purba_resto.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor // ==> pakai ini udah gak perlu constructor, ini dugunakan unruk final property/atribute yang diwajibkan untuk membuat contructor.
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;

    @Override
    public void createNew(Admin admin) {
        try {
            adminRepository.saveAndFlush(admin);
        }catch (DataIntegrityViolationException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Admin already exists");
        }
    }

    @Override
    public AdminResponse update(UpdateAdminRequest request) {
        try {
            Admin admin = findByIdOrThrowNotFound(request.getId());
            admin.setName(request.getName());
            adminRepository.saveAndFlush(admin);
            return AdminResponse.builder()
                    .adminId(request.getId())
                    .name(request.getName())
                    .userCredentialId(admin.getUserCredential().getId())
                    .build();
        }catch (DataIntegrityViolationException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Admin does not exist");
        }
    }

    private Admin findByIdOrThrowNotFound(String id) {
        return adminRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin not found"));
    }
}
