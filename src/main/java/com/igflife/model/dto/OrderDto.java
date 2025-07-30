package com.igflife.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)  // Exclude null fields from JSON
public class OrderDto {
    @JsonProperty("orderId")  // Explicit JSON property name
    private String orderId;

    @JsonProperty("customerId")
    private String customerId;

    @JsonProperty("orderDate")
    private LocalDateTime orderDate;

    @JsonProperty("totalAmount")
    private BigDecimal totalAmount;

    @JsonProperty("status")
    private String status;
}