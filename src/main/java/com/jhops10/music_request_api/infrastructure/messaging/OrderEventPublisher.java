package com.jhops10.music_request_api.infrastructure.messaging;

import com.jhops10.music_request_api.domain.events.OrderCreatedEvent;
import com.jhops10.music_request_api.domain.model.Order;
import com.jhops10.music_request_api.domain.ports.outgoing.UserRepositoryPort;
import com.jhops10.music_request_api.infrastructure.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(OrderEventPublisher.class);

    private final RabbitTemplate rabbitTemplate;
    private final UserRepositoryPort userRepositoryPort;

    public OrderEventPublisher(RabbitTemplate rabbitTemplate,
                               UserRepositoryPort userRepositoryPort) {
        this.rabbitTemplate = rabbitTemplate;
        this.userRepositoryPort = userRepositoryPort;
    }

    /**
     * Publica um evento de pedido criado para ser processado
     */
    public void publishOrderCreatedEvent(Order order) {
        try {

            String userEmail = userRepositoryPort.findById(order.getUserId())
                    .map(user -> user.getEmail())
                    .orElse("unknown@email.com");


            OrderCreatedEvent event = OrderCreatedEvent.from(order, userEmail);


            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.ORDER_CREATED_EXCHANGE,
                    RabbitMQConfig.ORDER_CREATED_ROUTING_KEY,
                    event
            );

            log.info("Evento ORDER_CREATED publicado para pedido: {}", order.getId());

        } catch (Exception e) {
            log.error("Erro ao publicar evento ORDER_CREATED para pedido: {}", order.getId(), e);
        }
    }
}