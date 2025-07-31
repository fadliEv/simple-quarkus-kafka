package com.igflife.service.impl;

import io.smallrye.reactive.messaging.kafka.Record;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

@ApplicationScoped
public class OrderEventConsumer {

    private static final Logger LOG = Logger.getLogger(OrderEventConsumer.class);

    @Inject
    ObjectMapper objectMapper;

    // Consumer untuk events dari aplikasi lain atau sistem eksternal
    @Incoming("external-order-events-in")
    public void processExternalOrderEvent(Record<String, String> record) {
        try {
            String orderId = record.key();
            String eventPayload = record.value();

            LOG.infof("Received external order event - OrderID: %s, Payload: %s", orderId, eventPayload);

            // Parse JSON event
            JsonNode eventNode = objectMapper.readTree(eventPayload);
            String eventType = eventNode.get("eventType").asText();

            switch (eventType) {
                case "ORDER_CREATED":
                    handleExternalOrderCreated(eventNode);
                    break;
                case "ORDER_STATUS_UPDATED":
                    handleExternalOrderStatusUpdated(eventNode);
                    break;
                case "ORDER_CANCELLED":
                    handleExternalOrderCancelled(eventNode);
                    break;
                default:
                    LOG.warnf("Unknown event type: %s", eventType);
            }

        } catch (Exception e) {
            LOG.errorf("Error processing external order event: %s", e.getMessage());
            // Dalam production, Anda mungkin ingin mengirim ke Dead Letter Queue
        }
    }

    private void handleExternalOrderCreated(JsonNode event) {
        String orderId = event.get("orderId").asText();
        String customerId = event.get("customerId").asText();

        LOG.infof("Processing external ORDER_CREATED event - OrderID: %s, CustomerID: %s", orderId, customerId);

        // Contoh: Update inventory, send notification, etc.
        // inventoryService.reserveItems(orderId);
        // notificationService.sendOrderConfirmation(customerId, orderId);
    }

    private void handleExternalOrderStatusUpdated(JsonNode event) {
        String orderId = event.get("orderId").asText();
        String newStatus = event.get("newStatus").asText();

        LOG.infof("Processing external ORDER_STATUS_UPDATED event - OrderID: %s, New Status: %s", orderId, newStatus);

        // Contoh: Update local cache, trigger business logic, etc.
        if ("SHIPPED".equals(newStatus)) {
            // trackingService.initiateTracking(orderId);
        } else if ("DELIVERED".equals(newStatus)) {
            // loyaltyService.awardPoints(customerId);
        }
    }

    private void handleExternalOrderCancelled(JsonNode event) {
        String orderId = event.get("orderId").asText();

        LOG.infof("Processing external ORDER_CANCELLED event - OrderID: %s", orderId);

        // Contoh: Release inventory, process refund, etc.
        // inventoryService.releaseItems(orderId);
        // paymentService.processRefund(orderId);
    }
}