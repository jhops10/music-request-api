package com.jhops10.music_request_api.application.services;

import com.jhops10.music_request_api.domain.enums.OrderStatus;
import com.jhops10.music_request_api.domain.exceptions.OrderNotFoundException;
import com.jhops10.music_request_api.domain.model.Order;
import com.jhops10.music_request_api.domain.ports.incoming.OrderServicePort;
import com.jhops10.music_request_api.domain.ports.outgoing.OrderRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderServicePort {

    private final OrderRepositoryPort orderRepositoryPort;

    public OrderServiceImpl(OrderRepositoryPort orderRepositoryPort) {
        this.orderRepositoryPort = orderRepositoryPort;
    }


    @Override
    public Order createOrder(Order order) {
        return orderRepositoryPort.save(order);
    }

    @Override
    public Optional<Order> findById(UUID id) {
        return orderRepositoryPort.findById(id);
    }

    @Override
    public Order findByIdOrThrow(UUID id) {
        return orderRepositoryPort.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(
                        "Order not found for id: " + id
                ));
    }

    @Override
    public Page<Order> findAll(Pageable pageable) {
        return orderRepositoryPort.findAll(pageable);
    }

    @Override
    public Order updateStatus(UUID id, OrderStatus newStatus) {
       Order order = findByIdOrThrow(id);

       order.updateStatus(newStatus);
        return orderRepositoryPort.save(order);
    }

    @Override
    public Page<Order> findByUserId(UUID userId, Pageable pageable) {
        return orderRepositoryPort.findByUserId(userId, pageable);
    }
}
