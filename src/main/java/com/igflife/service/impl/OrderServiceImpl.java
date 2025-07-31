package com.igflife.service.impl;

import com.igflife.model.dto.common.PagedResponse;
import com.igflife.model.dto.request.OrderCreateRequest;
import com.igflife.model.dto.response.OrderResponse;
import com.igflife.model.entity.Order;
import com.igflife.repository.OrderRepository;
import com.igflife.service.OrderService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class OrderServiceImpl implements OrderService {

    private static final Logger LOG = Logger.getLogger(OrderServiceImpl.class);

    @Inject
    OrderRepository orderRepository;

    @Inject
    KafkaEventProducer kafkaEventProducer;

    @Override
    public OrderResponse createOrder(OrderCreateRequest request) {
        LOG.info("Creating order with customerId: " + request.getCustomerId());

        try {
            // Simpan ke database
            Order order = new Order();
            order.setCustomerId(request.getCustomerId());
            order.setTotalAmount(request.getTotalAmount());
            order.setStatus(request.getStatus());
            order.setOrderDate(LocalDateTime.now());

            // Save to database
            String orderId = orderRepository.create(order);
            order.setOrderId(orderId);

            // Publish event to Kafka setelah berhasil save ke database
            LOG.info("Publishing ORDER_CREATED event to Kafka...");
            kafkaEventProducer.sendOrderCreatedEvent(orderId, request.getCustomerId());

            LOG.infof("Order created successfully with ID: %s", orderId);
            return convertToResponse(order);

        } catch (Exception e) {
            LOG.errorf("Failed to create order: %s", e.getMessage());
            throw new RuntimeException("Failed to create order", e);
        }
    }

    private OrderResponse convertToResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getOrderId());
        response.setCustomerId(order.getCustomerId());
        response.setOrderDate(order.getOrderDate());
        response.setTotalAmount(order.getTotalAmount());
        response.setStatus(order.getStatus());
        return response;
    }

    @Override
    public PagedResponse<OrderResponse> getOrders(int page, int size) {
        // Validate page number (must be ≥ 1)
        if (page < 1) {
            throw new IllegalArgumentException("Page must be greater than or equal to 1");
        }

        List<Order> orders = orderRepository.findAll(page, size);
        int totalItems = orderRepository.countAll();

        List<OrderResponse> content = orders.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return new PagedResponse<>(
                content,
                page,
                size,
                totalItems,
                (int) Math.ceil((double) totalItems / size)
        );
    }
}