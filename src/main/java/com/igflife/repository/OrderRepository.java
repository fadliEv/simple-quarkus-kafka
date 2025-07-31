package com.igflife.repository;

import com.igflife.model.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    String create(Order order);
    Optional<Order> findById(String orderId);
    List<Order> findAll(int page, int size);
    int countAll();
    Boolean updateStatus(String orderId, String newStatus);
}
