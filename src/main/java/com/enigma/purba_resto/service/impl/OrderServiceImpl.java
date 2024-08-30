package com.enigma.purba_resto.service.impl;

import com.enigma.purba_resto.dto.request.OrderDetailRequest;
import com.enigma.purba_resto.dto.request.OrderRequest;
import com.enigma.purba_resto.dto.request.SearchOrderRequest;
import com.enigma.purba_resto.dto.response.OrderDetailResponse;
import com.enigma.purba_resto.dto.response.OrderResponse;
import com.enigma.purba_resto.entity.*;
import com.enigma.purba_resto.repository.CustomerRepository;
import com.enigma.purba_resto.repository.MenuRepository;
import com.enigma.purba_resto.repository.OrderRepository;
import com.enigma.purba_resto.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service ("orderService")
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerService customerService;
    private final MenuService menuService;
    private final TableService tableService;
    private final OrderDetailService orderDetailService;
    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, CustomerService customerService, MenuService menuService, TableService tableService, OrderDetailService orderDetailService) {
        this.orderRepository = orderRepository;
        this.customerService = customerService;
        this.menuService = menuService;
        this.tableService = tableService;
        this.orderDetailService = orderDetailService;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrderResponse createNewOrder(@RequestBody OrderRequest request) {
        // CARA 3 : MENCOBA DENGAN DTO
        Customer customer = customerService.getCustomerById(request.getCustomerId());
        Table tableByName = tableService.getTableByName(request.getTableName());


        //buat entity object ==> table order
        Order order = Order.builder() //new Order(); // sebenernya kita bisa bikin Instance tanpa membuat new, tapi pakai builder (ini bisa lebih singkat)
                .customer(customer)
                .table(tableByName)
                .transDate(LocalDateTime.now())
                .build();
        //customer
        //order.setCustomer(customerById);

        //tabel
        //order.setTable(tableByName);
        //order.setTransDate(LocalDateTime.now());
        orderRepository.saveAndFlush(order);

        List<OrderDetail> orderDetails = new ArrayList<>();
        for (OrderDetailRequest orderDetailRequest : request.getOrderDetails()) {
            Menu menu = menuService.getMenuById(orderDetailRequest.getMenuId());

            //simpan order detail
            OrderDetail orderDetail = OrderDetail.builder()//new OrderDetail();
                    .order(order)
                    .menu(menu)
                    .price(menu.getPrice())
                    .quantity(orderDetailRequest.getQuantity())
                    .build();
            //order yang sudah disimpan
            //orderDetail.setOrder(order);
            //menu
            //orderDetail.setMenu(menu);
            //orderDetail.setQuantity(orderDetailRequest.getQuantity());
            //orderDetail.setPrice(menu.getPrice());
            // ini yang bikin order details kosong, karna belum disimpan, yak berikut adalah orderddetail yang dismpan ke orderDetails
            orderDetails.add(orderDetail);
        }
        orderDetailService.createBulk(orderDetails);
        order.setOrderDetails(orderDetails);

        return mapToOrderResponse(order);
    }

    @Override
    public Page<OrderResponse> getAllOrders(SearchOrderRequest request) {
        Pageable pageable = PageRequest.of(
                request.getPage()-1,
                request.getSize());
        Page<Order> orders = orderRepository.findAll(pageable);
        return orders.map(order -> mapToOrderResponse(order))
        // cara 1
        /*List<OrderResponse> orderResponses = new ArrayList<>();
        for (Order order : orders) {
            OrderResponse orderResponse = mapToOrderResponse(order);
            orderResponses.add(orderResponse);
        }*/
        //cara 2
        //List<OrderResponse> orderResponses1 = orders.stream().map(order -> mapToOrderResponse(order)).collect(Collectors.toList());
        ;
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(String id) {
        Order order = orderRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"order not found"));
        return mapToOrderResponse(order);
    }

    private static OrderResponse mapToOrderResponse(Order order) {
        List<OrderDetailResponse> orderDetailResponses = order.getOrderDetails().stream().map(orderDetail -> {
            return OrderDetailResponse.builder()
                    .orderDetailId(orderDetail.getId())
                    .orderId(orderDetail.getOrder().getId())
                    .menuId(orderDetail.getMenu().getId())
                    .menuName(orderDetail.getMenu().getName())
                    .price(orderDetail.getPrice())
                    .quantity(orderDetail.getQuantity())
                    .build();
        }).collect(Collectors.toList());

        OrderResponse orderResponse = OrderResponse.builder()
                .orderId(order.getId())
                .customerId(order.getCustomer().getId())
                .tableName(order.getTable().getName())
                .orderDetails(orderDetailResponses)
                .orderDate(order.getTransDate())

                .customerName(order.getCustomer().getName())
                .build();

        return orderResponse;
    }
}

// CARA 2 : MENCOBA DENGAN DTO
/*
Order order = new Order();

        Customer customer = new Customer();
        customer.setId(request.getCustomerId());
        order.setCustomer(customer);

        Table table = new Table();
        table.setId(request.getTableId());

        order.setTable(table);
        order.setTransDate(LocalDateTime.now());

        List<OrderDetail> orderDetails = new ArrayList<>();
        for (OrderDetailRequest orderDetailRequest : request.getOrderDetails()) {
            OrderDetail orderDetail = new OrderDetail();
            Menu menu = menuService.getById(orderDetailRequest.getMenuId());
            orderDetail.setMenu(menu);
            orderDetail.setPrice(menu.getPrice());
            orderDetail.setQuantity(orderDetailRequest.getQuantity());
            orderDetail.getMenuName();
            orderDetails.add(orderDetail);
            //menu.setPrice(menu.getPrice());

        }
        order.setOrderDetails(orderDetails);

        orderDetailService.createBulk(order.getOrderDetails());
        orderRepository.saveAndFlush(order);
        for (OrderDetail orderDetail : order.getOrderDetails()) {
            orderDetail.setOrder(order);
        }
        return order;
*/

// sekarang parameter pakai DTO (request / response)
// CARA 1 CARA2 PRIMITIF
// Simpan order terlebih dahulu
// cara 1 (pakai entiry biasa sebgai parameter)
/*        order.setTransDate(LocalDateTime.now());
        Order savedOrder = orderRepository.saveAndFlush(order);

        // Update order detail dengan referensi order dan harga
        for (OrderDetail orderDetail : savedOrder.getOrderDetails()) {
            Menu menu = menuService.getById(orderDetail.getMenu().getId());
            orderDetail.setOrder(savedOrder);
            orderDetail.setPrice(menu.getPrice());
        }

        // Simpan order detail yang telah diupdate
        orderDetailService.createBulk(savedOrder.getOrderDetails());

        return savedOrder;*/
// 1. save order detail
// Cara pertama dari trainer
        /*orderDetailService.createBulk(order.getOrderDetails());
        order.setTransDate(LocalDateTime.now());
        orderRepository.saveAndFlush(order);

        for (OrderDetail orderDetail : order.getOrderDetails()) {
            Menu menu = menuService.getById(orderDetail.getMenu().getId());
            orderDetail.setOrder(order);
            orderDetail.setPrice(menu.getPrice());
        }
        return order;*/
// dari AI 1
        /*for (OrderDetail orderDetail : order.getOrderDetails()) {
            Optional<Menu> byId = menuService.findById(orderDetail.getMenu().getId());
            orderDetail.setPrice(byId.get().getPrice());
            orderDetail.setOrder(order);
        }
        Order saveOrder = orderRepository.saveAndFlush(order);
        for (OrderDetail orderDetail : saveOrder.getOrderDetails()) {
            orderDetail.setOrder(saveOrder);
        }
        order.setTransDate(LocalDateTime.now());
        orderDetailService.createBulk(saveOrder.getOrderDetails());*/
// dari AI 2
       /* if (order == null) {
            throw new IllegalArgumentException("Order must not be null");
        }
        if (order.getCustomer() == null || order.getTable() == null || order.getOrderDetails() == null || order.getOrderDetails().isEmpty()) {
            throw new IllegalArgumentException("Customer, Table, and Order Details must not be null");
        }

        for (OrderDetail detail : order.getOrderDetails()) {
            if (detail == null || detail.getMenu() == null) {
                throw new IllegalArgumentException("OrderDetail and its Menu must not be null");
            }
            detail.setOrder(order);
        }

        order.setTransDate(LocalDateTime.now());

        // Save Order and OrderDetails
        orderRepository.saveAndFlush(order);*/
// return order;