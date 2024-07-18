package com.enigma.purba_resto.controller;

import com.enigma.purba_resto.entity.Customer;
import com.enigma.purba_resto.repository.CustomerRepository;
import com.enigma.purba_resto.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public Customer createNew(@RequestBody Customer customer) {
        return customerService.createCustomer(customer);
    }

    @GetMapping
    public List<Customer> findAll() {
       return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public Customer findById(@PathVariable String id) {
       return customerService.getCustomerById(id);
        /*Optional<Customer> byId = customerRepository.findById(id);
        return byId.orElseThrow(() -> new RuntimeException("Customer not found"));*/
        //atau
        /*if (byId.isEmpty()) {
            throw new RuntimeException("Customer not found");
        }
        return byId.get();*/
        // atau
        /*if (byId.isPresent()) {
            return byId.get();
        }else {
            throw new RuntimeException("Customer not found");
        }*/
    }

    @PutMapping("/{id}")
    public Customer update(@RequestBody Customer customer) {
        return customerService.updateCustomer(customer);
    }
    /*public Customer updateCustomer(@PathVariable String id, @RequestBody Customer customer) {
        Optional<Customer> byId = customerRepository.findById(id);
        if (byId.isPresent()) {
            Customer existingCustomer = byId.get();
            customer.setId(existingCustomer.getId());
            return customerRepository.save(customer);
        } else {
            throw new RuntimeException("Customer not found with id: " + id);
        }
    }*/ // atau
    /*public Customer updateCustomer(@RequestBody Customer customer) {
        Optional<Customer> byId = customerRepository.findById(customer.getId());
        if (byId.isPresent()) {
            return customerRepository.save(customer);
        }else {
            throw new RuntimeException("Customer not found");
        }
    }*/
    // atau
    /*public Customer updateCustomer(@RequestBody Customer customer) {
        Optional<Customer> byId = customerRepository.findById(customer.getId());
        if (byId.isEmpty()) {
            throw new RuntimeException("Customer not found");
        }
        return customerRepository.save(customer);
    }*/
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        customerService.deleteCustomer(id);
    }
}
