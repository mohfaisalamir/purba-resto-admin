package com.enigma.purba_resto.service;

import com.enigma.purba_resto.entity.Customer;

import java.util.List;

public interface CustomerService {
    Customer createCustomer(Customer customer);
    Customer updateCustomer(Customer customer);
    Customer getCustomerById(String id);
    List<Customer> getAllCustomers();
    void deleteCustomer(String id);
}
