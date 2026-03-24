package com.jhops10.music_request_api.application.mappers;

import com.jhops10.music_request_api.application.dtos.OrderRequestDTO;
import com.jhops10.music_request_api.application.dtos.OrderResponseDTO;
import com.jhops10.music_request_api.domain.model.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public Order toDomain(OrderRequestDTO request) {
        return Order.builder()
                .studentEmail(request.studentEmail())
                .instrument(request.instrument())
                .tone(request.tone())
                .musicName(request.musicName())
                .instructions(request.instructions())
                .build();
    }

    public OrderResponseDTO toResponse(Order order) {
        return new OrderResponseDTO(
                order.getId(),
                order.getStudentEmail(),
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
