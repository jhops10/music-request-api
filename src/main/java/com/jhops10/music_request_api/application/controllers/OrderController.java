package com.jhops10.music_request_api.application.controllers;

import com.jhops10.music_request_api.application.dtos.OrderRequestDTO;
import com.jhops10.music_request_api.application.dtos.OrderResponseDTO;
import com.jhops10.music_request_api.application.dtos.UpdateOrderStatusRequestDTO;
import com.jhops10.music_request_api.application.mappers.OrderMapper;
import com.jhops10.music_request_api.application.services.AuthenticationService;
import com.jhops10.music_request_api.domain.enums.UserRole;
import com.jhops10.music_request_api.domain.exceptions.UnauthorizedAccessException;
import com.jhops10.music_request_api.domain.model.Order;
import com.jhops10.music_request_api.domain.ports.incoming.OrderServicePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@Tag(name = "Pedidos", description = "Endpoints para gerenciar pedidos de músicas")
@SecurityRequirement(name = "Bearer Authentication")
public class OrderController {

    private final OrderServicePort orderServicePort;
    private final OrderMapper orderMapper;
    private final AuthenticationService authenticationService;

    public OrderController(OrderServicePort orderServicePort, OrderMapper orderMapper, AuthenticationService authenticationService) {
        this.orderServicePort = orderServicePort;
        this.orderMapper = orderMapper;
        this.authenticationService = authenticationService;
    }


    @PostMapping
    @Operation(summary = "Criar pedido", description = "Cria um novo pedido de música para o usuário autenticado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody @Valid OrderRequestDTO request,
                                                        Authentication authentication) {

        UUID userId = authenticationService.getUserId(authentication);

        Order order = orderMapper.toDomain(request, userId);
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
    @Operation(summary = "Listar pedidos", description = "Retorna pedidos paginados. STUDENT vê apenas seus pedidos, TEACHER vê todos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<Page<OrderResponseDTO>> getAllOrders(
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            Authentication authentication) {

        UUID userId = authenticationService.getUserId(authentication);
        UserRole role = authenticationService.getUserRole(authentication);

        Page<Order> ordersPage;

        if (role == UserRole.STUDENT) {
            ordersPage = orderServicePort.findByUserId(userId, pageable);
        } else {
            ordersPage = orderServicePort.findAll(pageable);
        }

        Page<OrderResponseDTO> response = ordersPage.map(orderMapper::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pedido por ID", description = "Retorna um pedido específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado (STUDENT vendo pedido de outro)")
    })
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable("id") UUID id,
                                                         Authentication authentication) {

        UUID userId = authenticationService.getUserId(authentication);
        UserRole role = authenticationService.getUserRole(authentication);

        Order order = orderServicePort.findByIdOrThrow(id);


        if (role == UserRole.STUDENT && !order.getUserId().equals(userId)) {
            throw new UnauthorizedAccessException("Student cannot view order belonging to another user. Order ID: " + id);
        }

        OrderResponseDTO response = orderMapper.toResponse(order);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status do pedido", description = "Apenas TEACHER pode atualizar status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status atualizado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado (apenas TEACHER)")
    })
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(@PathVariable("id") UUID id,
                                                              @RequestBody @Valid UpdateOrderStatusRequestDTO request,
                                                              Authentication authentication) {

        UserRole role = authenticationService.getUserRole(authentication);

        if (role != UserRole.TEACHER) {
            throw new UnauthorizedAccessException("User with role " + role + " cannot update order status. Only TEACHER can.");
        }

        Order updated = orderServicePort.updateStatus(id, request.status());
        OrderResponseDTO response = orderMapper.toResponse(updated);

        return ResponseEntity.ok(response);
    }
}
