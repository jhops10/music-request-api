package com.jhops10.music_request_api.application.dtos;

import com.jhops10.music_request_api.domain.enums.Instrument;
import com.jhops10.music_request_api.domain.enums.Tone;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrderRequestDTO(
        @Email @NotBlank String studentEmail,
        @NotNull Instrument instrument,
        @NotNull Tone tone,
        @NotBlank String musicName,
        String instructions
) {
}
