package com.jhops10.music_request_api.domain.ports.incoming;

import com.jhops10.music_request_api.domain.enums.OrderStatus;
import com.jhops10.music_request_api.domain.model.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderServicePort {

    Order createOrder(Order order);

    Optional<Order> findById(UUID id);

    List<Order> findAll();

    Order updateStatus(UUID id, OrderStatus newStatus);


}
