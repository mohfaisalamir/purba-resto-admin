package com.enigma.purba_resto.service.impl;

import com.enigma.purba_resto.dto.request.NewCustomerRequest;
import com.enigma.purba_resto.dto.request.SearchCustomerRequest;
import com.enigma.purba_resto.dto.request.UpdateCustomerRequest;
import com.enigma.purba_resto.dto.response.CustomerResponse;
import com.enigma.purba_resto.entity.Customer;
import com.enigma.purba_resto.repository.CustomerRepository;
import com.enigma.purba_resto.service.CustomerService;
import com.enigma.purba_resto.util.ValidationUtil;
import org.hibernate.annotations.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final ValidationUtil validationUtil;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, ValidationUtil validationUtil) {
        this.customerRepository = customerRepository;
        this.validationUtil = validationUtil;
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public CustomerResponse createNewCustomer(NewCustomerRequest request) {
        try {
            validationUtil.validate(request);
            Customer customer = Customer.builder()
                    .name(request.getName())
                    .phone(request.getPhone())
                    .email(request.getEmail())
                    .build();
            customerRepository.saveAndFlush(customer);
            return mapToResponse(customer);
        }catch (DataIntegrityViolationException e){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Customer data already exists");
        }
        // save mirip persist jika di JPA Hibernate
    }

    @Override
    public CustomerResponse updateCustomer(UpdateCustomerRequest request) {
       try {
           validationUtil.validate(request);
           Customer currrentCustomer = findByIdOrThrowNotFound(request.getId());
           currrentCustomer.setName(request.getName());
           currrentCustomer.setPhone(request.getPhone());
           currrentCustomer.setEmail(request.getEmail());
           customerRepository.saveAndFlush(currrentCustomer);
           return mapToResponse(currrentCustomer);
       }catch (DataIntegrityViolationException e){
           throw new ResponseStatusException(HttpStatus.CONFLICT,"Customer data already exists");
       }
    }

    @Override
    public Customer getCustomerById(String id) {
        return findByIdOrThrowNotFound(id);
    }

    @Override
    public CustomerResponse getOne(String id) {
        Customer customer = findByIdOrThrowNotFound(id);
        return mapToResponse(customer);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<CustomerResponse> getAllCustomers(SearchCustomerRequest request) {
        /*if(request.getPage() <= 0 || request.getSize() <= 0) {
            request.setPage(1);
            request.setSize(10);
        }*/
        // pindahin ke CustomerController
        // java reflection (ketika kita (typo) salah input kategori,
        // misal name jadi namex maka akan dikembalikan menjadi yang kita inginkan misal kembali  ke 'name')
        // tapi ini kelemahannya smua akan dikembalikan ke 'name'
        // java reflection - kurang bagus untuk performa
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
                request.getSortBy()
        );
        Page<Customer> customers = customerRepository.findAll(pageable);
        return customers.map(customer -> mapToResponse(customer));
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteCustomerById(String id) {
        Customer customer = findByIdOrThrowNotFound(id);
        customerRepository.delete(customer);
    }

    private Customer findByIdOrThrowNotFound(String id) {
        Optional<Customer> customer = customerRepository.findById(id);
        return customer.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Bruh, Customer not found"));
    }
    private CustomerResponse mapToResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .name(customer.getName())
                .phone(customer.getPhone())
                .isMember(customer.isMember())
                .build();
    }

}
