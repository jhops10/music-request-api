package com.jhops10.music_request_api.application.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jhops10.music_request_api.domain.enums.Instrument;
import com.jhops10.music_request_api.domain.enums.OrderStatus;
import com.jhops10.music_request_api.domain.enums.Tone;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderResponseDTO(
        UUID id,
        String studentEmail,
        Instrument instrument,
        Tone tone,
        String musicName,
        String instructions,
        OrderStatus status,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime createdAt,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime updatedAt
) {
}
