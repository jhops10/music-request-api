package com.jhops10.music_request_api.application.services;

import com.jhops10.music_request_api.domain.enums.OrderStatus;
import com.jhops10.music_request_api.domain.exceptions.OrderNotFoundException;
import com.jhops10.music_request_api.domain.model.Order;
import com.jhops10.music_request_api.domain.ports.incoming.OrderServicePort;
import com.jhops10.music_request_api.domain.ports.outgoing.OrderRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrderService implements OrderServicePort {

    private final OrderRepositoryPort orderRepositoryPort;

    public OrderService(OrderRepositoryPort orderRepositoryPort) {
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
    public List<Order> findAll() {
        return orderRepositoryPort.findAll();
    }

    @Override
    public Order updateStatus(UUID id, OrderStatus newStatus) {
       Order order = orderRepositoryPort.findById(id)
               .orElseThrow(() -> new OrderNotFoundException("Order not found for id: " + id));

       order.updateStatus(newStatus);
        return orderRepositoryPort.save(order);
    }
}
