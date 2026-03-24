package com.jhops10.music_request_api.domain.ports.outgoing;

import com.jhops10.music_request_api.domain.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepositoryPort {

    Order save(Order order);

    Optional<Order> findById(UUID id);

    Page<Order> findAll(Pageable pageable);

    void deleteById(UUID id);

    boolean existsById(UUID id);
}
