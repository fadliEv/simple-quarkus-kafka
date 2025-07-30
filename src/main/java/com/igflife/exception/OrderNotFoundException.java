package com.igflife.exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String orderId) {
        super("Order with ID " + orderId + " not found");
    }
}