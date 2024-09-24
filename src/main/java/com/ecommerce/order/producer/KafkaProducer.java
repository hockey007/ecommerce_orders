package com.ecommerce.order.producer;

import com.ecommerce.order.dto.OrderEvent;
import com.ecommerce.order.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class KafkaProducer {

    @Autowired
    private KafkaTemplate<UUID, OrderEvent> kafkaTemplate;

    @Value("${topic.name.order}")
    String orderTopic;

    public void sendOrder(UUID key, OrderEvent payload) {
        kafkaTemplate.send(orderTopic, key, payload);
    }

}
