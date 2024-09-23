package com.ecommerce.order.config;

import cart.CartServiceGrpc;
import inventory.InventoryServiceGrpc;
import cart.CartServiceGrpc.CartServiceBlockingStub;
import inventory.InventoryServiceGrpc.InventoryServiceBlockingStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcClientConfig {

    @Bean
    @Qualifier("cartChannel")
    public ManagedChannel cartServiceChannel() {
        return ManagedChannelBuilder.forAddress("localhost", 8086)
                .usePlaintext()
                .build();
    }

    @Bean
    @Qualifier("inventoryChannel")
    public ManagedChannel inventoryServiceChannel() {
        return ManagedChannelBuilder.forAddress("localhost", 8084)
                .usePlaintext()
                .build();
    }

    @Bean
    CartServiceBlockingStub cartServiceBlockingStub(@Qualifier("cartChannel") ManagedChannel channel) {
        return CartServiceGrpc.newBlockingStub(channel);
    }

    @Bean
    InventoryServiceBlockingStub inventoryServiceBlockingStub(@Qualifier("inventoryChannel") ManagedChannel channel) {
        return InventoryServiceGrpc.newBlockingStub(channel);
    }

}
