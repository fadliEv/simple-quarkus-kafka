package com.igflife.model.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateRequest {
    @NotBlank(message = "Customer ID is required")
    private String customerId;

    @NotNull @Positive
    private BigDecimal totalAmount;

    @Pattern(regexp = "PENDING|COMPLETED|CANCELLED")
    private String status;
}