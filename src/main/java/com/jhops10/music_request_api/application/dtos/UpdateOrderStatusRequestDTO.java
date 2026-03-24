package com.jhops10.music_request_api.application.dtos;

import com.jhops10.music_request_api.domain.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateOrderStatusRequestDTO(
        @NotNull OrderStatus status
) {
}
