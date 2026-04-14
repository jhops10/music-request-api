package com.jhops10.music_request_api.domain.model;

import com.jhops10.music_request_api.domain.enums.OrderStatus;
import com.jhops10.music_request_api.domain.exceptions.InvalidOrderStatusTransitionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


class OrderTest {

    @Nested
    class UpdateStatus {

        @Test
        @DisplayName("Should update status from pending to processing")
        void shouldUpdateStatusFromPendingToProcessing() {

            Order order = defaultOrder();

            order.updateStatus(OrderStatus.PROCESSING);

            assertEquals(OrderStatus.PROCESSING, order.getStatus());
            assertNotNull(order.getUpdatedAt());
        }

        @Test
        @DisplayName("Should update status from processing to completed")
        void shouldUpdateStatusFromProcessingToCompleted() {

            Order order = orderWithStatus(OrderStatus.PROCESSING);

            order.updateStatus(OrderStatus.COMPLETED);

            assertEquals(OrderStatus.COMPLETED, order.getStatus());
            assertNotNull(order.getUpdatedAt());
        }

        @Test
        @DisplayName("Should throw exception when update status of completed order")
        void shouldThrowExceptionWhenUpdateStatusOfCompletedOrder() {

            Order order = orderWithStatus(OrderStatus.COMPLETED);

            var sut = assertThrows(InvalidOrderStatusTransitionException.class, () -> order.updateStatus(OrderStatus.PROCESSING));

            assertEquals("Cannot update status of completed order. Current status: COMPLETED", sut.getMessage());
            assertNull(order.getUpdatedAt());
        }

        @Test
        @DisplayName("Should throw exception when update status from pending to completed")
        void shouldThrowExceptionWhenUpdateStatusFromPendingToCompleted() {

            Order order = defaultOrder();

            var sut = assertThrows(InvalidOrderStatusTransitionException.class, () -> order.updateStatus(OrderStatus.COMPLETED));

            assertEquals("Cannot change order status from PENDING to COMPLETED", sut.getMessage());
            assertNull(order.getUpdatedAt());
        }

        @Test
        @DisplayName("Should do nothing when update status from pending to pending")
        void shouldDoNothingWhenUpdateStatusFromPendingToPending() {

            Order order = defaultOrder();

            LocalDateTime before = order.getUpdatedAt();
            order.updateStatus(OrderStatus.PENDING);

            assertEquals(OrderStatus.PENDING, order.getStatus());
            assertEquals(before, order.getUpdatedAt());
        }

        @Test
        @DisplayName("Should do nothing when update status from processing to processing")
        void shouldDoNothingWhenUpdateStatusFromProcessingToProcessing() {

            Order order = orderWithStatus(OrderStatus.PROCESSING);

            LocalDateTime before = order.getUpdatedAt();
            order.updateStatus(OrderStatus.PROCESSING);

            assertEquals(OrderStatus.PROCESSING, order.getStatus());
            assertEquals(before, order.getUpdatedAt());
        }
    }

    private Order defaultOrder() {
        return Order.builder()
                .id(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .musicName("Test Song")
                .createdAt(LocalDateTime.now())
                .build();
    }

    private Order orderWithStatus(OrderStatus status) {
        return Order.builder()
                .id(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .musicName("Test Song")
                .status(status)
                .createdAt(LocalDateTime.now())
                .build();
    }
}