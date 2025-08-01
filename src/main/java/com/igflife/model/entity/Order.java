package com.igflife.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private String orderId;
    private String customerId;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    private String status; // PENDING, COMPLETED, CANCELLED
}