package com.enigma.purba_resto.service.impl;

import com.enigma.purba_resto.dto.response.OrderDetailResponse;
import com.enigma.purba_resto.entity.OrderDetail;
import com.enigma.purba_resto.repository.OrderDetailRepository;
import com.enigma.purba_resto.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service ("orderDetailService")
public class OrderDetailServiceImpl implements OrderDetailService {
    public final OrderDetailRepository orderDetailRepository;

    @Autowired
    public OrderDetailServiceImpl(OrderDetailRepository orderDetailRepository) {
        this.orderDetailRepository = orderDetailRepository;
    }

    @Override
    public List<OrderDetail> createBulk(List<OrderDetail> orderDetails) {
        return orderDetailRepository.saveAllAndFlush(orderDetails);
    }

    @Override
    public OrderDetailResponse getById(String id) {
        OrderDetail orderDetail = orderDetailRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"order detail not found"));
        return OrderDetailResponse.builder()
                .orderDetailId(orderDetail.getId())
                .orderId(orderDetail.getOrder().getId())
                .menuId(orderDetail.getMenu().getId())
                .menuName(orderDetail.getMenu().getName())
                .price(orderDetail.getPrice())
                .quantity(orderDetail.getQuantity())
                .build();
    }
}
