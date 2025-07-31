package com.igflife.service.impl;


import com.igflife.model.dto.common.PagedResponse;
import com.igflife.model.dto.request.OrderCreateRequest;
import com.igflife.model.dto.response.OrderResponse;
import com.igflife.model.entity.Order;
import com.igflife.repository.OrderRepository;
import com.igflife.service.OrderService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class OrderServiceImpl implements OrderService {

    @Inject
    OrderRepository orderRepository;

    @Override
    public OrderResponse createOrder(OrderCreateRequest request) {
        Order order = new Order();
        order.setCustomerId(request.getCustomerId());
        order.setTotalAmount(request.getTotalAmount());
        order.setStatus(request.getStatus());

        String orderId = orderRepository.create(order);
        return convertToResponse(orderId, order);
    }

    private OrderResponse convertToResponse(String orderId, Order order) {
        OrderResponse response = new OrderResponse();
        response.setOrderId(orderId);
        response.setCustomerId(order.getCustomerId());
        response.setOrderDate(LocalDateTime.now()); // Or fetch from DB
        response.setTotalAmount(order.getTotalAmount());
        response.setStatus(order.getStatus());
        return response;
    }

    @Override
    public PagedResponse<OrderResponse> getOrders(int page, int size) {
        List<Order> orders = orderRepository.findAll(page, size);
        int totalItems = orderRepository.countAll();

        List<OrderResponse> content = orders.stream()
                .map(order -> convertToResponse(order.getOrderId(), order))
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
