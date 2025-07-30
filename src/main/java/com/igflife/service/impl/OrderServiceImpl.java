package com.igflife.service.impl;

import com.igflife.exception.OrderNotFoundException;
import com.igflife.model.dto.OrderDto;
import com.igflife.model.entity.Order;
import com.igflife.repository.OrderRepository;
import com.igflife.service.OrderService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class OrderServiceImpl implements OrderService {

    @Inject
    OrderRepository orderRepository;


    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        Order orderResult = new Order(
                orderDto.getOrderId(),
                orderDto.getCustomerId(),
                orderDto.getOrderDate(),
                orderDto.getTotalAmount(),
                orderDto.getStatus()
        );
        orderRepository.save(orderResult);
        return orderDto;
    }

    @Override
    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto getOrderById(String orderId) {
        Order order = orderRepository.findById(orderId);
        if (order == null) {
            throw new OrderNotFoundException(orderId);
        }
        return convertToDTO(order);
    }

    private OrderDto convertToDTO(Order order) {
        return new OrderDto(
                order.getOrderId(),
                order.getCustomerId(),
                order.getOrderDate(),
                order.getTotalAmount(),
                order.getStatus()
        );
    }
}
