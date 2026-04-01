package com.jhops10.music_request_api.application.mappers;

import com.jhops10.music_request_api.application.dtos.OrderRequestDTO;
import com.jhops10.music_request_api.application.dtos.OrderResponseDTO;
import com.jhops10.music_request_api.domain.model.Order;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderMapper {

    public Order toDomain(OrderRequestDTO request, UUID userId) {
        return Order.builder()
                .userId(userId)
                .instrument(request.instrument())
                .tone(request.tone())
                .musicName(request.musicName())
                .instructions(request.instructions())
                .build();
    }

    public OrderResponseDTO toResponse(Order order) {
        return new OrderResponseDTO(
                order.getId(),
                order.getUserId(),
                order.getInstrument(),
                order.getTone(),
                order.getMusicName(),
                order.getInstructions(),
                order.getStatus(),
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
}
