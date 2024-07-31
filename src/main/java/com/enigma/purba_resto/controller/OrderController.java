package com.enigma.purba_resto.controller;

import com.enigma.purba_resto.dto.request.OrderRequest;
import com.enigma.purba_resto.dto.response.CommonResponse;
import com.enigma.purba_resto.dto.response.OrderDetailResponse;
import com.enigma.purba_resto.dto.response.OrderResponse;
import com.enigma.purba_resto.entity.Order;
import com.enigma.purba_resto.entity.OrderDetail;
import com.enigma.purba_resto.repository.OrderDetailRepository;
import com.enigma.purba_resto.repository.OrderRepository;
import com.enigma.purba_resto.service.OrderDetailService;
import com.enigma.purba_resto.service.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class OrderController {
    private final OrderService orderService;
    private final OrderDetailService orderDetailService;

    @Autowired
    public OrderController(
            @Qualifier("orderService") OrderService orderService,
            @Qualifier("orderDetailService") OrderDetailService orderDetailService) {
        this.orderService = orderService;
        this.orderDetailService = orderDetailService;
    }
    @PostMapping
    public ResponseEntity<CommonResponse<OrderResponse>> createNewTransaction(@RequestBody OrderRequest order) {
//        ResponseEntity<> ini memudahkan anda untuk mengotak atik status codenya 201, 200, 202,500 dll.
        OrderResponse orderResponse = orderService.createNewOrder(order);
        CommonResponse<OrderResponse> response = CommonResponse.<OrderResponse>builder()
                .message("Successfully created new transaction")
                .statusCode(HttpStatus
                        .CREATED
                        .value())
                .data(orderResponse)
                .build();
        return ResponseEntity
                .status(HttpStatus
                        .CREATED
                        .value())//status(201) // bisa langsung angka atau bisa juga ENUM
                .body(response);
    }
    @GetMapping
    public ResponseEntity<CommonResponse<List<OrderResponse>>> getAllOrder() {
        List<OrderResponse> allOrders = orderService.getAllOrders();
        CommonResponse<List<OrderResponse>> response = CommonResponse.<List<OrderResponse>>builder()
                .message("Successfully get All transaction")
                .statusCode(HttpStatus
                        .OK
                        .value())
                .data(allOrders)
                .build();

        return ResponseEntity
                .status(HttpStatus
                        .OK
                        .value())
                .body(response);

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable String id) {//CommonResponse<OrderResponse> diganti '?' agar lebih fleksible
        OrderResponse orderById = orderService.getOrderById(id);
        CommonResponse<OrderResponse> response = CommonResponse.<OrderResponse>builder()
                .message("Successfully get All transaction")
                .statusCode(HttpStatus
                        .OK
                        .value())
                .data(orderById)
                .build();
        return ResponseEntity
                .status(HttpStatus
                        .OK
                        .value())
                .body(response);
    }
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getOrderDetailById(@PathVariable String id) {
        OrderDetailResponse byId = orderDetailService.getById(id);
        CommonResponse<OrderDetailResponse> response = CommonResponse.<OrderDetailResponse>builder()
                .message("Successfully get All transaction")
                .statusCode(HttpStatus
                        .OK
                        .value())
                .data(byId)
                .build();
        return ResponseEntity
                .status(HttpStatus
                        .OK
                        .value())
                .body(response);
    }
}
