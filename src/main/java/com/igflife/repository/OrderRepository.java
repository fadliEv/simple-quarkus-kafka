package com.igflife.repository;

import com.igflife.model.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    String create(Order order);
    List<Order> findAll(int page, int size);
    int countAll();
}
