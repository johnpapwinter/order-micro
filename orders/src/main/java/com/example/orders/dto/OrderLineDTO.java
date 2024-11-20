package com.example.orders.dto;

import com.example.orders.exception.ErrorMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderLineDTO {

    private Long id;
    @NotBlank(message = ErrorMessages.PRODUCT_ID_CANNOT_BE_EMPTY)
    private String productId;
    @Positive(message = ErrorMessages.QUANTITY_CANNOT_BE_NEGATIVE)
    private Integer quantity;
    @Positive(message = ErrorMessages.PRICE_CANNOT_BE_NEGATIVE)
    private Double price;


}
