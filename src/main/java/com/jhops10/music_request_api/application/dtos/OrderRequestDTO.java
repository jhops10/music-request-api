package com.jhops10.music_request_api.application.dtos;

import com.jhops10.music_request_api.domain.enums.Instrument;
import com.jhops10.music_request_api.domain.enums.Tone;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados para criar um novo pedido")
public record OrderRequestDTO(
        @Schema(description = "Instrumento musical", example = "VIOLAO", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull Instrument instrument,

        @Schema(description = "Tom da música", example = "C", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull Tone tone,

        @Schema(description = "Nome da música", example = "Parabéns pra Você", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank String musicName,

        @Schema(description = "Instruções adicionais (opcional)", example = "Tocar na versão original")
        String instructions
) {
}
