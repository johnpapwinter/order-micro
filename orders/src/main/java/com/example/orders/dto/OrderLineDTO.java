package com.example.orders.dto;

import com.example.orders.exception.ErrorMessages;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Schema(description = "Order Line Data Transfer Object")
@Data
@Builder
public class OrderLineDTO {

    @Schema(description = "Unique Identifier", example = "1")
    private Long id;

    @Schema(description = "Product Unique Identifier", example = "P-100")
    @NotBlank(message = ErrorMessages.PRODUCT_ID_CANNOT_BE_EMPTY)
    private String productId;

    @Schema(description = "Quantity of the Product", example = "4")
    @Positive(message = ErrorMessages.QUANTITY_CANNOT_BE_NEGATIVE)
    private Integer quantity;

    @Schema(description = "Price of the Items", example = "10.5")
    @Positive(message = ErrorMessages.PRICE_CANNOT_BE_NEGATIVE)
    private Double price;


}
