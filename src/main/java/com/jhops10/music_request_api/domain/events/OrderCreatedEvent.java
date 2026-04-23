package com.jhops10.music_request_api.domain.events;

import com.jhops10.music_request_api.domain.enums.Instrument;
import com.jhops10.music_request_api.domain.enums.OrderStatus;
import com.jhops10.music_request_api.domain.enums.Tone;
import com.jhops10.music_request_api.domain.model.Order;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;


public record OrderCreatedEvent(
        UUID orderId,
        UUID userId,
        String userEmail,
        String musicName,
        Instrument instrument,
        Tone tone,
        String instructions,
        OrderStatus status,
        LocalDateTime createdAt,
        LocalDateTime eventTimestamp
) implements Serializable {

    public OrderCreatedEvent {
        if (eventTimestamp == null) {
            eventTimestamp = LocalDateTime.now();
        }
    }

    public static OrderCreatedEvent from(Order order, String userEmail) {
        return new OrderCreatedEvent(
                order.getId(),
                order.getUserId(),
                userEmail,
                order.getMusicName(),
                order.getInstrument(),
                order.getTone(),
                order.getInstructions(),
                order.getStatus(),
                order.getCreatedAt(),
                LocalDateTime.now()
        );
    }
}