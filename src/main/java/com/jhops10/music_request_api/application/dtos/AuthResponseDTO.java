package com.jhops10.music_request_api.application.dtos;

import com.jhops10.music_request_api.domain.enums.UserRole;

public record AuthResponseDTO(
        String token,
        String type,
        String email,
        UserRole role
) {
}
