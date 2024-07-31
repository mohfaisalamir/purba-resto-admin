package com.enigma.purba_resto.service;

import com.enigma.purba_resto.dto.request.OrderRequest;
import com.enigma.purba_resto.dto.response.OrderResponse;
import com.enigma.purba_resto.entity.Order;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface OrderService {
    OrderResponse createNewOrder(OrderRequest request);
    List<OrderResponse> getAllOrders();
    OrderResponse getOrderById(String id);
}
