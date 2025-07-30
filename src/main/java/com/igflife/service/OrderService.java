package com.igflife.service;

import com.igflife.model.dto.OrderDto;
import com.igflife.model.entity.Order;

import java.util.List;

public interface OrderService {
    OrderDto createOrder(OrderDto order);
    List<OrderDto> getAllOrders();
    OrderDto getOrderById(String orderId);
}
