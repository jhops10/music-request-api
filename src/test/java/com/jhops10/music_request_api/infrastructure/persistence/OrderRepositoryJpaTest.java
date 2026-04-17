package com.jhops10.music_request_api.infrastructure.persistence;

import com.jhops10.music_request_api.domain.enums.Instrument;
import com.jhops10.music_request_api.domain.enums.OrderStatus;
import com.jhops10.music_request_api.domain.enums.Tone;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class OrderRepositoryJpaTest {

    @Autowired
    private OrderRepositoryJpa orderRepositoryJpa;

    @Test
    @DisplayName("Should save and find order by id")
    void shouldSaveAndFindOrderById() {

        UUID userId = UUID.randomUUID();
        OrderEntity order = createOrderEntity(userId);

        OrderEntity saved = orderRepositoryJpa.save(order);
        var found = orderRepositoryJpa.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getUserId()).isEqualTo(userId);
        assertThat(found.get().getMusicName()).isEqualTo("Test Music");
    }

    @Test
    @DisplayName("Should find orders by user id with pagination")
    void shouldFindOrdersByUserId() {

        UUID userId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);

        orderRepositoryJpa.save(createOrderEntity(userId));
        orderRepositoryJpa.save(createOrderEntity(userId));
        orderRepositoryJpa.save(createOrderEntity(UUID.randomUUID())); // outro usuário


        Page<OrderEntity> result = orderRepositoryJpa.findByUserId(userId, pageable);


        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getUserId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("Should return empty page when user has no orders")
    void shouldReturnEmptyPageWhenUserHasNoOrders() {

        UUID userId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);


        Page<OrderEntity> result = orderRepositoryJpa.findByUserId(userId, pageable);


        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isZero();
    }

    @Test
    @DisplayName("Should check if order exists by id")
    void shouldCheckIfOrderExistsById() {

        OrderEntity saved = orderRepositoryJpa.save(createOrderEntity(UUID.randomUUID()));


        assertThat(orderRepositoryJpa.existsById(saved.getId())).isTrue();
        assertThat(orderRepositoryJpa.existsById(UUID.randomUUID())).isFalse();
    }

    private OrderEntity createOrderEntity(UUID userId) {
        return OrderEntity.builder()
                .userId(userId)
                .instrument(Instrument.VIOLAO)
                .tone(Tone.C)
                .musicName("Test Music")
                .instructions("Test Instructions")
                .status(OrderStatus.PENDING)
                .build();
    }
}