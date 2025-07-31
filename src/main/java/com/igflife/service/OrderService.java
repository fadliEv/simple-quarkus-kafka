package com.igflife.service;

import com.igflife.model.dto.OrderDto;
import com.igflife.model.dto.common.PagedResponse;
import com.igflife.model.dto.request.OrderCreateRequest;
import com.igflife.model.dto.response.OrderResponse;
import com.igflife.model.entity.Order;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderCreateRequest request);
    OrderResponse getOrderById(String orderId);
    PagedResponse<OrderResponse> getOrders(int page, int size);
}
