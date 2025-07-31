package com.igflife.service;

import com.igflife.model.dto.common.PagedResponse;
import com.igflife.model.dto.request.OrderCreateRequest;
import com.igflife.model.dto.response.OrderResponse;

public interface OrderService {
    OrderResponse createOrder(OrderCreateRequest request);
    PagedResponse<OrderResponse> getOrders(int page, int size);
}
