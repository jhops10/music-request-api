package com.jhops10.music_request_api.domain.ports.outgoing;

import com.jhops10.music_request_api.domain.model.Order;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepositoryPort {

    Order save(Order order);

    Optional<Order> findById(UUID id);

    List<Order> findAll();

    void deleteById(UUID id);

    boolean existsById(UUID id);
}
