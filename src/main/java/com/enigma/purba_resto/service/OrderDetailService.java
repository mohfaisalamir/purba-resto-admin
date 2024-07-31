package com.enigma.purba_resto.service;

import com.enigma.purba_resto.dto.request.OrderRequest;
import com.enigma.purba_resto.dto.response.OrderDetailResponse;
import com.enigma.purba_resto.dto.response.OrderResponse;
import com.enigma.purba_resto.entity.Order;
import com.enigma.purba_resto.entity.OrderDetail;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderDetailService {
        List<OrderDetail> createBulk(List<OrderDetail> orderDetails);
        OrderDetailResponse getById(String id);

}
