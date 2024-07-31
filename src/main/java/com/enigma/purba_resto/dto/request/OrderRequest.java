package com.enigma.purba_resto.dto.request;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Data // getter, setter, equal, and hascode
//@AllArgsConstructor // constructor full parameter
@NoArgsConstructor // construcktor non parameter
@Builder
public class OrderRequest {
    private String customerId;
    private String tableName;
    private List<OrderDetailRequest> orderDetails;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getTableName() {
        return tableName;
    }

    public void settableName(String tableName) {
        this.tableName = tableName;
    }

    public List<OrderDetailRequest> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetailRequest> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public OrderRequest(String customerId, String tableName, List<OrderDetailRequest> orderDetails) {
        this.customerId = customerId;
        this.tableName = tableName;
        this.orderDetails = orderDetails;
    }
}

