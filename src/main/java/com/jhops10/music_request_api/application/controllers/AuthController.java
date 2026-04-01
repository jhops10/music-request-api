package com.jhops10.music_request_api.application.controllers;

import com.jhops10.music_request_api.application.dtos.AuthResponseDTO;
import com.jhops10.music_request_api.application.dtos.LoginRequestDTO;
import com.jhops10.music_request_api.application.dtos.RegisterRequestDTO;
import com.jhops10.music_request_api.domain.model.User;
import com.jhops10.music_request_api.domain.ports.outgoing.UserRepositoryPort;
import com.jhops10.music_request_api.infrastructure.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(UserRepositoryPort userRepositoryPort,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager,
                          JwtService jwtService) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {

        // 1. Verificar se email já existe
        if (userRepositoryPort.existsByEmail(request.email())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        // 2. Criar usuário
        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))  // Codifica senha
                .role(request.role())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // 3. Salvar
        User savedUser = userRepositoryPort.save(user);

        // 4. Gerar token JWT
        String token = jwtService.generateToken(savedUser);

        // 5. Retornar resposta
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AuthResponseDTO(token, "Bearer", savedUser.getEmail(), savedUser.getRole()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {

        // 1. Autenticar usuário
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        // 2. Buscar usuário autenticado
        User user = userRepositoryPort.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 3. Gerar token JWT
        String token = jwtService.generateToken(user);

        // 4. Retornar resposta
        return ResponseEntity.ok(new AuthResponseDTO(token, "Bearer", user.getEmail(), user.getRole()));
    }
}