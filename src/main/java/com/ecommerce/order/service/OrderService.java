package com.ecommerce.order.service;

import com.ecommerce.common.CartProduct;
import com.ecommerce.common.CartServiceGrpc.CartServiceBlockingStub;
import com.ecommerce.common.GetCartRequest;
import com.ecommerce.common.GetCartResponse;
import com.ecommerce.common.InventoryServiceGrpc.InventoryServiceBlockingStub;
import com.ecommerce.order.dto.OrderEvent;
import com.ecommerce.order.model.Order;
import com.ecommerce.order.model.OrderItem;
import com.ecommerce.order.model.OrderStatus;
import com.ecommerce.order.repository.OrderItemRepository;
import com.ecommerce.order.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.UUID.fromString;

@Service
public class OrderService {

    @Autowired
    InventoryServiceBlockingStub inventoryService;

    @Autowired
    CartServiceBlockingStub cartService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private KafkaTemplate<String, OrderEvent> kafkaTemplate;

    private static final String ORDER_CREATED_TOPIC = "order-created";

    @Transactional
    public void placeOrder(String userId) {

        OrderEvent orderEvent = new OrderEvent();

        Order order = new Order();
        order.setUserId(fromString(userId));
        order.setOrderStatus(OrderStatus.PROCESSING);

        Order savedOrder = orderRepository.save(order);
        orderEvent.setOrder(savedOrder);

        GetCartRequest cartRequest = GetCartRequest.newBuilder()
                .setUserId(userId)
                .build();

        List<OrderItem> orderItems = new ArrayList<>();
        GetCartResponse cartProducts = cartService.getCartItems(cartRequest);
        for(CartProduct cartProduct: cartProducts.getCartItemsList()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setQuantity(cartProduct.getQuantity());
            orderItem.setProductId(fromString(cartProduct.getProductId()));
            orderItem.setVariantId(fromString(cartProduct.getVariantId()));

            orderItems.add(orderItem);
        }

        List<OrderItem> savedOrderItems = orderItemRepository.saveAll(orderItems);
        orderEvent.setOrderItems(savedOrderItems);
        orderEvent.setOrderStatus(OrderStatus.PROCESSING);

        kafkaTemplate.send(ORDER_CREATED_TOPIC, orderEvent);
    }

}
