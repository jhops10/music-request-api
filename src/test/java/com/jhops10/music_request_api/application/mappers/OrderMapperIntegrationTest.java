package com.jhops10.music_request_api.application.mappers;

import com.jhops10.music_request_api.application.dtos.OrderRequestDTO;
import com.jhops10.music_request_api.application.dtos.OrderResponseDTO;
import com.jhops10.music_request_api.domain.enums.Instrument;
import com.jhops10.music_request_api.domain.enums.OrderStatus;
import com.jhops10.music_request_api.domain.enums.Tone;
import com.jhops10.music_request_api.domain.model.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class OrderMapperIntegrationTest {

    @Autowired
    private OrderMapper orderMapper;

    @Test
    @DisplayName("Should convert OrderRequestDTO to Order domain")
    void shouldConvertRequestToDomain() {

        UUID userId = UUID.randomUUID();
        OrderRequestDTO request = new OrderRequestDTO(
                Instrument.VIOLAO,
                Tone.C,
                "Test Music",
                "Test Instructions"
        );


        Order result = orderMapper.toDomain(request, userId);


        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getInstrument()).isEqualTo(Instrument.VIOLAO);
        assertThat(result.getTone()).isEqualTo(Tone.C);
        assertThat(result.getMusicName()).isEqualTo("Test Music");
        assertThat(result.getInstructions()).isEqualTo("Test Instructions");
        assertThat(result.getStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(result.getId()).isNull(); // ID deve ser gerado pelo banco
        assertThat(result.getCreatedAt()).isNull(); // Timestamp deve ser gerado pelo banco
    }

    @Test
    @DisplayName("Should convert Order domain to OrderResponseDTO")
    void shouldConvertDomainToResponse() {

        UUID orderId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        Order order = Order.builder()
                .id(orderId)
                .userId(userId)
                .instrument(Instrument.VIOLA_CAIPIRA)
                .tone(Tone.G)
                .musicName("Response Music")
                .instructions("Response Instructions")
                .status(OrderStatus.PROCESSING)
                .build();


        OrderResponseDTO result = orderMapper.toResponse(order);


        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(orderId);
        assertThat(result.userId()).isEqualTo(userId);
        assertThat(result.instrument()).isEqualTo(Instrument.VIOLA_CAIPIRA);
        assertThat(result.tone()).isEqualTo(Tone.G);
        assertThat(result.musicName()).isEqualTo("Response Music");
        assertThat(result.instructions()).isEqualTo("Response Instructions");
        assertThat(result.status()).isEqualTo(OrderStatus.PROCESSING);
    }
}