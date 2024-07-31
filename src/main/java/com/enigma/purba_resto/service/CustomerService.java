package com.enigma.purba_resto.service;

import com.enigma.purba_resto.dto.request.SearchCustomerRequest;
import com.enigma.purba_resto.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomerService {
    Customer createCustomer(Customer customer);
    Customer updateCustomer(Customer customer);
    Customer getCustomerById(String id);
    Page<Customer> getAllCustomers(SearchCustomerRequest request);
    void deleteCustomer(String id);
}
