package com.jhops10.music_request_api.infrastructure.security;

import com.jhops10.music_request_api.domain.model.User;
import com.jhops10.music_request_api.domain.ports.outgoing.UserRepositoryPort;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepositoryPort userRepositoryPort;

    public JwtAuthenticationFilter(JwtService jwtService, UserRepositoryPort userRepositoryPort) {
        this.jwtService = jwtService;
        this.userRepositoryPort = userRepositoryPort;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Extrair o token do header Authorization
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Extrair o token (remover "Bearer ")
        final String token = authHeader.substring(7);

        try {
            // 3. Extrair o userId do token
            UUID userId = jwtService.extractUserId(token);

            // 4. Buscar o usuário no banco
            User user = userRepositoryPort.findById(userId)
                    .orElse(null);

            // 5. Se usuário existe e ainda não está autenticado
            if (user != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // 6. Validar o token
                if (jwtService.isTokenValid(token, user)) {

                    // 7. Criar as authorities (permissões) baseado na role
                    List<SimpleGrantedAuthority> authorities = List.of(
                            new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
                    );

                    // 8. Criar o objeto de autenticação do Spring Security
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            user.getId().toString(),  // principal (username)
                            null,                     // credentials
                            authorities                // authorities
                    );

                    // 9. Adicionar detalhes da requisição
                    authToken.setDetails(request);

                    // 10. Colocar no contexto de segurança
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Token inválido - não autentica
            logger.error("Invalid JWT token: " + e.getMessage());
        }

        // 11. Continuar a cadeia de filtros
        filterChain.doFilter(request, response);
    }
}