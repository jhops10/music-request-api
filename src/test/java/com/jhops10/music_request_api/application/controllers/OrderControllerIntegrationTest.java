package com.jhops10.music_request_api.application.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.jhops10.music_request_api.application.dtos.OrderRequestDTO;
import com.jhops10.music_request_api.application.dtos.UpdateOrderStatusRequestDTO;
import com.jhops10.music_request_api.domain.enums.Instrument;
import com.jhops10.music_request_api.domain.enums.OrderStatus;
import com.jhops10.music_request_api.domain.enums.Tone;
import com.jhops10.music_request_api.domain.enums.UserRole;
import com.jhops10.music_request_api.domain.model.User;
import com.jhops10.music_request_api.infrastructure.persistence.OrderEntity;
import com.jhops10.music_request_api.infrastructure.persistence.OrderRepositoryJpa;
import com.jhops10.music_request_api.infrastructure.persistence.UserEntity;
import com.jhops10.music_request_api.infrastructure.persistence.UserRepositoryJpa;
import com.jhops10.music_request_api.infrastructure.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderRepositoryJpa orderRepositoryJpa;

    @Autowired
    private UserRepositoryJpa userRepositoryJpa;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    private String studentToken;
    private String teacherToken;
    private UUID studentId;
    private UUID teacherId;

    @BeforeEach
    void setUp() {
        orderRepositoryJpa.deleteAll();
        userRepositoryJpa.deleteAll();

        UserEntity student = UserEntity.builder()
                .email("student@test.com")
                .passwordHash(passwordEncoder.encode("password123"))
                .role(UserRole.STUDENT)
                .build();
        UserEntity savedStudent = userRepositoryJpa.save(student);
        studentId = savedStudent.getId();
        studentToken = generateTokenForUser(savedStudent);


        UserEntity teacher = UserEntity.builder()
                .email("teacher@test.com")
                .passwordHash(passwordEncoder.encode("password123"))
                .role(UserRole.TEACHER)
                .build();
        UserEntity savedTeacher = userRepositoryJpa.save(teacher);
        teacherId = savedTeacher.getId();
        teacherToken = generateTokenForUser(savedTeacher);
    }

    @Test
    @DisplayName("Should create order successfully when authenticated as STUDENT")
    void shouldCreateOrderSuccessfully() throws Exception {

        OrderRequestDTO request = new OrderRequestDTO(
                Instrument.VIOLAO,
                Tone.C,
                "Minha Música",
                "Instruções especiais"
        );


        mockMvc.perform(post("/api/v1/orders")
                        .header("Authorization", "Bearer " + studentToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.musicName").value("Minha Música"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.userId").value(studentId.toString()));
    }

    @Test
    @DisplayName("Should return 401 when creating order without token")
    void shouldReturnUnauthorizedWhenNoToken() throws Exception {
        OrderRequestDTO request = new OrderRequestDTO(
                Instrument.VIOLAO,
                Tone.C,
                "Minha Música",
                null
        );

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return 400 when creating order with invalid data")
    void shouldReturnBadRequestWhenInvalidData() throws Exception {
        OrderRequestDTO request = new OrderRequestDTO(
                null,
                null,
                "",
                null
        );

        mockMvc.perform(post("/api/v1/orders")
                        .header("Authorization", "Bearer " + studentToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should list orders - STUDENT sees only their own orders")
    void shouldListOrdersForStudent() throws Exception {

        OrderEntity studentOrder = OrderEntity.builder()
                .userId(studentId)
                .musicName("Pedido do Aluno")
                .instrument(Instrument.VIOLAO)
                .tone(Tone.C)
                .status(OrderStatus.PENDING)
                .build();
        orderRepositoryJpa.save(studentOrder);


        OrderEntity otherOrder = OrderEntity.builder()
                .userId(UUID.randomUUID())
                .musicName("Pedido de Outro")
                .instrument(Instrument.VIOLA_CAIPIRA)
                .tone(Tone.D)
                .status(OrderStatus.PENDING)
                .build();
        orderRepositoryJpa.save(otherOrder);

        mockMvc.perform(get("/api/v1/orders")
                        .header("Authorization", "Bearer " + studentToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].musicName").value("Pedido do Aluno"));
    }

    @Test
    @DisplayName("Should list orders - TEACHER sees all orders")
    void shouldListAllOrdersForTeacher() throws Exception {

        orderRepositoryJpa.save(createOrderEntity(studentId, "Pedido 1"));
        orderRepositoryJpa.save(createOrderEntity(UUID.randomUUID(), "Pedido 2"));
        orderRepositoryJpa.save(createOrderEntity(UUID.randomUUID(), "Pedido 3"));

        mockMvc.perform(get("/api/v1/orders")
                        .header("Authorization", "Bearer " + teacherToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(3));
    }

    @Test
    @DisplayName("Should get order by id - STUDENT can see their own order")
    void shouldGetOrderByIdForStudent() throws Exception {
        OrderEntity saved = orderRepositoryJpa.save(createOrderEntity(studentId, "Meu Pedido"));

        mockMvc.perform(get("/api/v1/orders/{id}", saved.getId())
                        .header("Authorization", "Bearer " + studentToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.musicName").value("Meu Pedido"));
    }

    @Test
    @DisplayName("Should return 403 when STUDENT tries to see another user's order")
    void shouldReturnForbiddenWhenStudentSeesOtherOrder() throws Exception {
        OrderEntity otherOrder = orderRepositoryJpa.save(createOrderEntity(UUID.randomUUID(), "Pedido de Outro"));

        mockMvc.perform(get("/api/v1/orders/{id}", otherOrder.getId())
                        .header("Authorization", "Bearer " + studentToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should update status - TEACHER can update any order")
    void shouldUpdateStatusAsTeacher() throws Exception {
        OrderEntity order = orderRepositoryJpa.save(createOrderEntity(studentId, "Pedido para Atualizar"));
        UpdateOrderStatusRequestDTO request = new UpdateOrderStatusRequestDTO(OrderStatus.PROCESSING);

        mockMvc.perform(patch("/api/v1/orders/{id}/status", order.getId())
                        .header("Authorization", "Bearer " + teacherToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PROCESSING"));
    }

    @Test
    @DisplayName("Should return 403 when STUDENT tries to update status")
    void shouldReturnForbiddenWhenStudentUpdatesStatus() throws Exception {
        OrderEntity order = orderRepositoryJpa.save(createOrderEntity(studentId, "Pedido"));
        UpdateOrderStatusRequestDTO request = new UpdateOrderStatusRequestDTO(OrderStatus.PROCESSING);

        mockMvc.perform(patch("/api/v1/orders/{id}/status", order.getId())
                        .header("Authorization", "Bearer " + studentToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should return 404 when order not found")
    void shouldReturnNotFoundWhenOrderDoesNotExist() throws Exception {
        UUID nonExistentId = UUID.randomUUID();

        mockMvc.perform(get("/api/v1/orders/{id}", nonExistentId)
                        .header("Authorization", "Bearer " + teacherToken))
                .andExpect(status().isNotFound());
    }

    private OrderEntity createOrderEntity(UUID userId, String musicName) {
        return OrderEntity.builder()
                .userId(userId)
                .musicName(musicName)
                .instrument(Instrument.VIOLAO)
                .tone(Tone.C)
                .status(OrderStatus.PENDING)
                .build();
    }

    private String generateTokenForUser(UserEntity userEntity) {
        User user = User.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .password(userEntity.getPasswordHash())
                .role(userEntity.getRole())
                .build();

        return jwtService.generateToken(user);
    }
}