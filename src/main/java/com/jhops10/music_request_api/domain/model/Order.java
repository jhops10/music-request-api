package com.jhops10.music_request_api.domain.model;

import com.jhops10.music_request_api.domain.enums.Instrument;
import com.jhops10.music_request_api.domain.enums.OrderStatus;
import com.jhops10.music_request_api.domain.enums.Tone;
import com.jhops10.music_request_api.domain.exceptions.InvalidOrderStatusTransitionException;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
public class Order {

    private UUID id;
    private UUID userId;
    private Instrument instrument;
    private Tone tone;
    private String musicName;
    private String instructions;

    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public void updateStatus(OrderStatus status) {

        if (this.status == OrderStatus.COMPLETED) {
            throw new InvalidOrderStatusTransitionException("Cannot update status of completed order. Current status: " + this.status);
        }

        if (this.status == status) {
            return;
        }

        boolean isValidTransition = isValidTransition(this.status, status);

        if (!isValidTransition) {
            throw new InvalidOrderStatusTransitionException("Cannot change order status from " + this.status + " to " + status);
        }

        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    private boolean isValidTransition(OrderStatus current, OrderStatus next) {
        return switch (current) {
            case PENDING -> next == OrderStatus.PROCESSING;
            case PROCESSING -> next == OrderStatus.COMPLETED;
            case COMPLETED -> false;
        };
    }
}
