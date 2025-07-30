package com.igflife.repository;

import com.igflife.model.entity.Order;

import java.util.List;

public interface OrderRepository {
    void save(Order order);
    List<Order> findAll();
    Order findById(String orderId);
}
