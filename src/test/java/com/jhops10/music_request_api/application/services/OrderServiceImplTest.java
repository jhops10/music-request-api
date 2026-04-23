package com.jhops10.music_request_api.application.services;

import com.jhops10.music_request_api.domain.enums.Instrument;
import com.jhops10.music_request_api.domain.enums.OrderStatus;
import com.jhops10.music_request_api.domain.enums.Tone;
import com.jhops10.music_request_api.domain.exceptions.OrderNotFoundException;
import com.jhops10.music_request_api.domain.model.Order;
import com.jhops10.music_request_api.domain.ports.outgoing.OrderRepositoryPort;
import com.jhops10.music_request_api.infrastructure.messaging.OrderEventPublisher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepositoryPort orderRepositoryPort;

    @Mock
    private OrderEventPublisher eventPublisher;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Nested
    class CreateOrder {

        @Test
        @DisplayName("Should create order with success")
        void shouldCreateOrderWithSuccess() {

            Order order = defaultOrder();

            when(orderRepositoryPort.save(order)).thenReturn(order);

            var sut = orderService.createOrder(order);

            assertNotNull(sut);
            assertSame(order, sut);

            verify(orderRepositoryPort).save(order);
            verifyNoMoreInteractions(orderRepositoryPort);
        }
    }

    @Nested
    class FindByIdOrThrow {

        @Test
        @DisplayName("Should return order when id exists")
        void shouldReturnOrderWhenIdExists() {

            Order order = defaultOrder();

            when(orderRepositoryPort.findById(order.getId())).thenReturn(Optional.of(order));

            var sut = orderService.findByIdOrThrow(order.getId());

            assertNotNull(sut);
            assertSame(order, sut);

            verify(orderRepositoryPort).findById(order.getId());
            verifyNoMoreInteractions(orderRepositoryPort);

        }

        @Test
        @DisplayName("Should throw exception when order not found")
        void shouldThrowExceptionWhenOrderNotFound() {

            UUID unexistingId = UUID.randomUUID();

            when(orderRepositoryPort.findById(unexistingId)).thenReturn(Optional.empty());

            var result = assertThrows(OrderNotFoundException.class, () -> orderService.findByIdOrThrow(unexistingId));

            assertEquals("Order not found for id: " + unexistingId, result.getMessage());
            verify(orderRepositoryPort).findById(unexistingId);
            verifyNoMoreInteractions(orderRepositoryPort);

        }
    }

    @Nested
    class FindAll {

        @Test
        @DisplayName("Should return paginated orders")
        void shouldReturnPaginatedOrders() {

            Pageable pageable = PageRequest.of(0, 10);

            List<Order> orders = List.of(defaultOrder(), defaultOrder());
            Page<Order> page = new PageImpl<>(orders, pageable, orders.size());

            when(orderRepositoryPort.findAll(pageable)).thenReturn(page);

            Page<Order> result = orderService.findAll(pageable);

            assertNotNull(result);
            assertEquals(2, result.getContent().size());
            assertSame(page, result);

            verify(orderRepositoryPort).findAll(pageable);
            verifyNoMoreInteractions(orderRepositoryPort);
        }

        @Test
        @DisplayName("Should return empty page when no orders found")
        void shouldReturnEmptyPage() {

            Pageable pageable = PageRequest.of(0, 10);
            Page<Order> emptyPage = Page.empty(pageable);

            when(orderRepositoryPort.findAll(pageable)).thenReturn(emptyPage);

            Page<Order> result = orderService.findAll(pageable);

            assertNotNull(result);
            assertTrue(result.isEmpty());

            verify(orderRepositoryPort).findAll(pageable);
            verifyNoMoreInteractions(orderRepositoryPort);
        }
    }

    @Nested
    class UpdateStatus {

        @Test
        @DisplayName("Should update order status with success")
        void shouldUpdateOrderStatusWithSuccess() {

            Order order = defaultOrder();

            when(orderRepositoryPort.findById(order.getId())).thenReturn(Optional.of(order));
            when(orderRepositoryPort.save(order)).thenReturn(order);

            var sut = orderService.updateStatus(order.getId(), OrderStatus.PROCESSING);

            assertNotNull(sut);
            assertEquals(OrderStatus.PROCESSING, sut.getStatus());

            verify(orderRepositoryPort).findById(order.getId());
            verify(orderRepositoryPort).save(order);
            verifyNoMoreInteractions(orderRepositoryPort);
        }

        @Test
        @DisplayName("Should throw exception when updating non-existing order")
        void shouldThrowExceptionWhenOrderNotFound() {

            UUID id = UUID.randomUUID();

            when(orderRepositoryPort.findById(id)).thenReturn(Optional.empty());

            assertThrows(OrderNotFoundException.class, () -> orderService.updateStatus(id, OrderStatus.COMPLETED));

            verify(orderRepositoryPort).findById(id);
            verifyNoMoreInteractions(orderRepositoryPort);
        }
    }

    @Nested
    class FindByUserId {

        @Test
        @DisplayName("Should return orders by user id")
        void shouldReturnOrdersByUserId() {

            UUID userId = UUID.randomUUID();
            Pageable pageable = PageRequest.of(0, 10);

            List<Order> orders = List.of(defaultOrder());
            Page<Order> page = new PageImpl<>(orders, pageable, orders.size());

            when(orderRepositoryPort.findByUserId(userId, pageable)).thenReturn(page);

            Page<Order> result = orderService.findByUserId(userId, pageable);

            assertNotNull(result);
            assertSame(page, result);

            verify(orderRepositoryPort).findByUserId(userId, pageable);
            verifyNoMoreInteractions(orderRepositoryPort);
        }
    }


    private Order defaultOrder() {
        return Order.builder()
                .id(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .instrument(Instrument.VIOLA_CAIPIRA)
                .tone(Tone.A)
                .musicName("Test Song")
                .instructions("Test Instructions")
                .createdAt(LocalDateTime.of(2026, 1, 1, 10, 0))
                .build();
    }
}