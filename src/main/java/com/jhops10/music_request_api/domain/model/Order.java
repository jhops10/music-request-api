package com.jhops10.music_request_api.domain.model;

import com.jhops10.music_request_api.domain.enums.Instrument;
import com.jhops10.music_request_api.domain.enums.OrderStatus;
import com.jhops10.music_request_api.domain.enums.Tone;
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
            throw new IllegalStateException("Cannot update status of a completed order");
        }
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }
}
