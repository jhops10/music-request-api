package com.jhops10.music_request_api.domain.ports.incoming;

import com.jhops10.music_request_api.domain.enums.OrderStatus;
import com.jhops10.music_request_api.domain.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface OrderServicePort {

    Order createOrder(Order order);

    Optional<Order> findById(UUID id);

    Order findByIdOrThrow(UUID id);

    Page<Order> findAll(Pageable pageable);

    Page<Order> findByUserId(UUID userId, Pageable pageable);

    Order updateStatus(UUID id, OrderStatus newStatus);

}
