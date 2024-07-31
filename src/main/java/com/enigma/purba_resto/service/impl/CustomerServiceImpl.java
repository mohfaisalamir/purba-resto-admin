package com.enigma.purba_resto.service.impl;

import com.enigma.purba_resto.dto.request.SearchCustomerRequest;
import com.enigma.purba_resto.entity.Customer;
import com.enigma.purba_resto.repository.CustomerRepository;
import com.enigma.purba_resto.service.CustomerService;
import org.hibernate.annotations.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;
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
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Customer data already exists");
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
    public Page<Customer> getAllCustomers(SearchCustomerRequest request) {
        /*if(request.getPage() <= 0 || request.getSize() <= 0) {
            request.setPage(1);
            request.setSize(10);
        }*/ // pindahin ke CustomerController

        // java reflection (ketika kita (typo) salah input kategori,
        // misal name jadi namex maka akan dikembalikan menjadi yang kita inginkan misal kembali  ke 'name')
        // tapi ini kelemahannya smua akan dikembalikan ke 'name'
        for (Field declaredField : Customer.class.getDeclaredFields()) {
            if(!declaredField.getName().equals(request.getSortBy())) {
                request.setSortBy("name");
            }
        }
        Sort.Direction direction = Sort.Direction.fromString(request.getDirection());
        Pageable pageable = PageRequest.of(
                request.getPage()-1,
                request.getSize(),
                direction,
                request.getSortBy());
        return customerRepository.findAll(pageable);
    }

    @Override
    public void deleteCustomer(String id) {
        Customer byIdOrThrowNotFound = findByIdOrThrowNotFound(id);
        customerRepository.delete(byIdOrThrowNotFound);
    }

    private Customer findByIdOrThrowNotFound(String id) {
        Optional<Customer> byId = customerRepository.findById(id);
        return byId.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Customer not found"));
    }
}
