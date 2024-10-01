package com.enigma.purba_resto.service;

import com.enigma.purba_resto.dto.request.UpdateAdminRequest;
import com.enigma.purba_resto.dto.response.AdminResponse;
import com.enigma.purba_resto.entity.Admin;

public interface AdminService {
    void createNew(Admin admin); // parameter pakai dto/nggak juga boleh
    AdminResponse update(UpdateAdminRequest request);
}
