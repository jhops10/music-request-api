package com.jhops10.music_request_api.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepositoryJpa extends JpaRepository<OrderEntity, UUID> {
}
