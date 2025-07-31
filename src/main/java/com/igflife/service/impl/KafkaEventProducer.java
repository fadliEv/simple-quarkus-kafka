package com.igflife.service.impl;

import io.smallrye.reactive.messaging.kafka.Record;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.time.LocalDateTime;

@ApplicationScoped
public class KafkaEventProducer {

    private static final Logger LOG = Logger.getLogger(KafkaEventProducer.class);

    @Inject
    @Channel("order-events-out")
    Emitter<Record<String, String>> orderEventEmitter;

    public void sendOrderCreatedEvent(String orderId, String customerId) {
        try {
            String eventKey = orderId;
            String eventValue = String.format(
                    "{\"eventType\":\"ORDER_CREATED\",\"orderId\":\"%s\",\"customerId\":\"%s\",\"timestamp\":\"%s\"}",
                    orderId, customerId, LocalDateTime.now()
            );

            LOG.infof("Sending ORDER_CREATED event for order: %s", orderId);

            // Kirim sebagai Kafka Record dengan key dan value
            orderEventEmitter.send(Record.of(eventKey, eventValue));

            LOG.infof("Successfully sent ORDER_CREATED event for order: %s", orderId);

        } catch (Exception e) {
            LOG.errorf("Failed to send ORDER_CREATED event for order %s: %s", orderId, e.getMessage());
            throw new RuntimeException("Failed to send Kafka event", e);
        }
    }

    public void sendOrderStatusUpdateEvent(String orderId, String oldStatus, String newStatus) {
        try {
            String eventKey = orderId;
            String eventValue = String.format(
                    "{\"eventType\":\"ORDER_STATUS_UPDATED\",\"orderId\":\"%s\",\"oldStatus\":\"%s\",\"newStatus\":\"%s\",\"timestamp\":\"%s\"}",
                    orderId, oldStatus, newStatus, LocalDateTime.now()
            );

            LOG.infof("Sending ORDER_STATUS_UPDATED event for order: %s", orderId);
            orderEventEmitter.send(Record.of(eventKey, eventValue));
            LOG.infof("Successfully sent ORDER_STATUS_UPDATED event for order: %s", orderId);

        } catch (Exception e) {
            LOG.errorf("Failed to send ORDER_STATUS_UPDATED event for order %s: %s", orderId, e.getMessage());
            throw new RuntimeException("Failed to send Kafka event", e);
        }
    }
}