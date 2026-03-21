package com.jhops10.music_request_api.infrastructure.persistence;

import com.jhops10.music_request_api.domain.model.Order;
import com.jhops10.music_request_api.domain.ports.outgoing.OrderRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class OrderRepositoryAdapter implements OrderRepositoryPort {

    private final OrderRepositoryJpa orderRepositoryJpa;

    public OrderRepositoryAdapter(OrderRepositoryJpa orderRepositoryJpa) {
        this.orderRepositoryJpa = orderRepositoryJpa;
    }

    @Override
    public Order save(Order order) {
        OrderEntity entity = toEntity(order);
        OrderEntity saved = orderRepositoryJpa.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Order> findById(UUID id) {
        return orderRepositoryJpa.findById(id)
                .map(this::toDomain);
    }

    @Override
    public List<Order> findAll() {
       return orderRepositoryJpa.findAll()
               .stream()
               .map(this::toDomain)
               .toList();
    }

    @Override
    public void deleteById(UUID id) {
        orderRepositoryJpa.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return orderRepositoryJpa.existsById(id);
    }

    private OrderEntity toEntity(Order order) {
        return OrderEntity.builder()
                .id(order.getId())
                .studentEmail(order.getStudentEmail())
                .instrument(order.getInstrument())
                .tone(order.getTone())
                .musicName(order.getMusicName())
                .instructions(order.getInstructions())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    private Order toDomain(OrderEntity orderEntity) {
        return Order.builder()
                .id(orderEntity.getId())
                .studentEmail(orderEntity.getStudentEmail())
                .instrument(orderEntity.getInstrument())
                .tone(orderEntity.getTone())
                .musicName(orderEntity.getMusicName())
                .instructions(orderEntity.getInstructions())
                .status(orderEntity.getStatus())
                .createdAt(orderEntity.getCreatedAt())
                .updatedAt(orderEntity.getUpdatedAt())
                .build();
    }
}
