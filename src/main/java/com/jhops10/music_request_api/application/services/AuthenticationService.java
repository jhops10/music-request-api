package com.jhops10.music_request_api.application.services;

import com.jhops10.music_request_api.domain.enums.UserRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AuthenticationService {

    /**
     * Extrai o ID do usuário do objeto Authentication
     */
    public UUID getUserId(Authentication authentication) {
        // O ID do usuário está armazenado no 'name' do principal
        // Ou podemos criar um campo customizado no token
        return UUID.fromString(authentication.getName());
    }

    /**
     * Extrai a role do usuário do objeto Authentication
     */
    public UserRole getUserRole(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(role -> role.startsWith("ROLE_"))
                .findFirst()
                .map(role -> role.replace("ROLE_", ""))
                .map(UserRole::valueOf)
                .orElseThrow(() -> new IllegalStateException("User has no role"));
    }
}