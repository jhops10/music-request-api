package com.jhops10.music_request_api.application.controllers;

import com.jhops10.music_request_api.application.dtos.OrderRequestDTO;
import com.jhops10.music_request_api.application.dtos.OrderResponseDTO;
import com.jhops10.music_request_api.application.dtos.UpdateOrderStatusRequestDTO;
import com.jhops10.music_request_api.application.mappers.OrderMapper;
import com.jhops10.music_request_api.domain.model.Order;
import com.jhops10.music_request_api.domain.ports.incoming.OrderServicePort;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderServicePort orderServicePort;
    private final OrderMapper orderMapper;

    public OrderController(OrderServicePort orderServicePort, OrderMapper orderMapper) {
        this.orderServicePort = orderServicePort;
        this.orderMapper = orderMapper;
    }


    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody @Valid OrderRequestDTO request) {
        Order order = orderMapper.toDomain(request);
        Order created = orderServicePort.createOrder(order);
        OrderResponseDTO response = orderMapper.toResponse(created);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<OrderResponseDTO>> getAllOrders(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable) {

        Page<Order> ordersPage = orderServicePort.findAll(pageable);
        Page<OrderResponseDTO> response = ordersPage.map(orderMapper::toResponse);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable("id") UUID id) {
        Order order = orderServicePort.findByIdOrThrow(id);

        OrderResponseDTO response = orderMapper.toResponse(order);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(@PathVariable("id") UUID id,
                                                              @RequestBody @Valid UpdateOrderStatusRequestDTO request) {

        Order updated = orderServicePort.updateStatus(id, request.status());
        OrderResponseDTO response = orderMapper.toResponse(updated);

        return ResponseEntity.ok(response);
    }
}
