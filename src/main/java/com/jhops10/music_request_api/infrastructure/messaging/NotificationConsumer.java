package com.jhops10.music_request_api.infrastructure.messaging;

import com.jhops10.music_request_api.domain.events.OrderCreatedEvent;
import com.jhops10.music_request_api.infrastructure.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {

    private static final Logger log = LoggerFactory.getLogger(NotificationConsumer.class);


    @RabbitListener(queues = RabbitMQConfig.ORDER_CREATED_QUEUE)
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        log.info("Recebido evento ORDER_CREATED para pedido: {}", event.orderId());

        try {

            sendEmailNotification(event);


            sendWhatsAppNotification(event);

            log.info("Notificações enviadas com sucesso para pedido: {}", event.orderId());

        } catch (Exception e) {
            log.error("Erro ao processar evento ORDER_CREATED: {}", event.orderId(), e);

            throw new RuntimeException("Falha ao processar notificação", e);
        }
    }

    private void sendEmailNotification(OrderCreatedEvent event) {

        log.info("📧 [EMAIL] Enviando para: {}", event.userEmail());
        log.info("📧 Assunto: Seu pedido foi criado!");
        log.info("📧 Corpo: Pedido #{} - Música: {}", event.orderId(), event.musicName());


        sleep(100);
    }

    private void sendWhatsAppNotification(OrderCreatedEvent event) {

        log.info("📱 [WHATSAPP] Enviando para: {}", event.userEmail());
        log.info("📱 Mensagem: Seu pedido #{} foi criado com sucesso!", event.orderId());


        sleep(100);
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}