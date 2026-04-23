package com.jhops10.music_request_api.application.services;

import com.jhops10.music_request_api.domain.enums.OrderStatus;
import com.jhops10.music_request_api.domain.exceptions.OrderNotFoundException;
import com.jhops10.music_request_api.domain.model.Order;
import com.jhops10.music_request_api.domain.ports.incoming.OrderServicePort;
import com.jhops10.music_request_api.domain.ports.outgoing.OrderRepositoryPort;
import com.jhops10.music_request_api.infrastructure.messaging.OrderEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderServicePort {

    private final OrderRepositoryPort orderRepositoryPort;
    private final OrderEventPublisher eventPublisher;

    public OrderServiceImpl(OrderRepositoryPort orderRepositoryPort, OrderEventPublisher eventPublisher) {
        this.orderRepositoryPort = orderRepositoryPort;
        this.eventPublisher = eventPublisher;
    }


    @Override
    public Order createOrder(Order order) {
        Order savedOrder = orderRepositoryPort.save(order);

        eventPublisher.publishOrderCreatedEvent(savedOrder);
        return savedOrder;
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
