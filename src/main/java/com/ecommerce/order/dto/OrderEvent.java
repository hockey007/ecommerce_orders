package com.ecommerce.order.dto;

import com.ecommerce.order.model.PaymentStatus;
import com.ecommerce.order.model.InventoryStatus;
import com.ecommerce.order.model.Order;
import com.ecommerce.order.model.OrderItem;
import com.ecommerce.order.model.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderEvent {

    private Order order;
    private List<OrderItem> orderItems;
    private Double total;
    private OrderStatus orderStatus = OrderStatus.CREATED;
    private InventoryStatus inventoryStatus = InventoryStatus.CREATED;
    private PaymentStatus paymentStatus = PaymentStatus.CREATED;
    private String orderStatusMessage;
    private String paymentStatusMessage;


}
