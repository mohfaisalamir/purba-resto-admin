package com.enigma.purba_resto.service.impl;

import com.enigma.purba_resto.entity.Customer;
import com.enigma.purba_resto.repository.CustomerRepository;
import com.enigma.purba_resto.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer createCustomer(Customer customer) {
        try {
            Customer saved = customerRepository.save(customer);
            return saved;
        }catch (DataIntegrityViolationException e){
            throw new DataIntegrityViolationException("Customer data already exists");
        }
        // save mirip persist jika di JPA Hibernate

    }

    @Override
    public Customer updateCustomer(Customer customer) {
       findByIdOrThrowNotFound(customer.getId());
       return customerRepository.save(customer);
    }

    @Override
    public Customer getCustomerById(String id) {
        return findByIdOrThrowNotFound(id);
    }

    @Override
    public List<Customer> getAllCustomers() {
        List<Customer> all = customerRepository.findAll();
        return all;
    }

    @Override
    public void deleteCustomer(String id) {
        Customer byIdOrThrowNotFound = findByIdOrThrowNotFound(id);
        customerRepository.delete(byIdOrThrowNotFound);
    }

    private Customer findByIdOrThrowNotFound(String id) {
        Optional<Customer> byId = customerRepository.findById(id);
        return byId.orElseThrow(() -> new RuntimeException("Customer not found"));
    }
}
