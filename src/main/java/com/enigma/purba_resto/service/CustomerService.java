package com.enigma.purba_resto.service;

import com.enigma.purba_resto.dto.request.NewCustomerRequest;
import com.enigma.purba_resto.dto.request.SearchCustomerRequest;
import com.enigma.purba_resto.dto.request.UpdateCustomerRequest;
import com.enigma.purba_resto.dto.response.CustomerResponse;
import com.enigma.purba_resto.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerService {
    CustomerResponse createNewCustomer(NewCustomerRequest request);
    CustomerResponse updateCustomer(UpdateCustomerRequest request);
    Customer getCustomerById(String id);
    CustomerResponse getOne(String id);
    Page<CustomerResponse> getAllCustomers(SearchCustomerRequest request);
    void deleteCustomerById(String id);
}
