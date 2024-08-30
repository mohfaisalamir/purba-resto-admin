package com.enigma.purba_resto.controller;

import com.enigma.purba_resto.dto.request.NewCustomerRequest;
import com.enigma.purba_resto.dto.request.SearchCustomerRequest;
import com.enigma.purba_resto.dto.request.UpdateCustomerRequest;
import com.enigma.purba_resto.dto.response.CommonResponse;
import com.enigma.purba_resto.dto.response.CustomerResponse;
import com.enigma.purba_resto.dto.response.PagingResponse;
import com.enigma.purba_resto.entity.Customer;
import com.enigma.purba_resto.service.CustomerService;
import com.enigma.purba_resto.util.PagingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity createNew(@RequestBody NewCustomerRequest request) {
        CustomerResponse customerResponse = customerService.createNewCustomer(request);
        CommonResponse<CustomerResponse> commonResponse = CommonResponse.<CustomerResponse>builder()
                .message("Succesfully create new customer")
                .statusCode(HttpStatus.CREATED.value())
                .data(customerResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commonResponse);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable String id) {
        CustomerResponse customerResponse = customerService.getOne(id);
        CommonResponse<CustomerResponse> response = CommonResponse.<CustomerResponse> builder()
                .message("Succesfully get customer by id")
                .statusCode(HttpStatus.OK.value())
                .data(customerResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<?> findAll(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false,defaultValue = "10") Integer size,
            @RequestParam(required = false,defaultValue = "asc") String direction,
            @RequestParam(required = false, defaultValue = "name") String sortBy
    ) {
        page = PagingUtils.validatePage(page);
        size = PagingUtils.validateSize(size);
        direction = PagingUtils.validateDirection(direction);
        /*if(page <= 0 || size <= 0) {
            page = 1;
            size = 10;
        }*/
        SearchCustomerRequest searchCustomerRequest = SearchCustomerRequest.builder()
                .page(page)
                .size(size)
                .direction(direction)
                .sortBy(sortBy)
                .build();
        Page<CustomerResponse> allCustomers = customerService.getAllCustomers(searchCustomerRequest);
        PagingResponse pagingResponse = PagingResponse.builder()
                .page(page)
                .size(size)
                .count(allCustomers.getTotalElements())
                .totalPages(allCustomers.getTotalPages())
                .build();
        CommonResponse<List<CustomerResponse>> response = CommonResponse.<List<CustomerResponse>>builder()
                .message("successfuly get all customer")
                .statusCode(HttpStatus.OK.value())
                .data(allCustomers.getContent())
                .paging(pagingResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }


    @PutMapping
    public ResponseEntity<?> update(@RequestBody UpdateCustomerRequest request) {
        CustomerResponse customerResponse = customerService.updateCustomer(request);
        CommonResponse<CustomerResponse> commonResponse = CommonResponse.<CustomerResponse>builder()
                .message("Succesfully updete customer")
                .statusCode(HttpStatus.OK.value())
                .data(customerResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commonResponse);
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
    public ResponseEntity<?> delete(@PathVariable String id) {
        customerService.deleteCustomerById(id);
        CommonResponse<?> commonResponse = CommonResponse.builder()
                .message("Succesfully delete customer")
                .statusCode(HttpStatus.OK.value())
                .data("OK Lur")
                .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(commonResponse);
    }
}
