package com.example.orders.dto;

import com.example.orders.enums.OrderStatus;
import com.example.orders.exception.ErrorMessages;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Schema(description = "Order Data Transfer Object")
@Data
@Builder
public class OrderDTO implements Serializable {

    @Schema(description = "Unique Identifier", example = "1")
    private Long id;

    @Schema(description = "Order External Identifier", example = "ATS-1029-100")
    @NotBlank(message = ErrorMessages.ORDER_ID_CANNOT_BE_EMPTY)
    private String orderId;

    @Schema(description = "Customer Full Name", example = "John Smith")
    @NotBlank(message = ErrorMessages.CUSTOMER_NAME_CANNOT_BE_EMPTY)
    private String customerName;

    @Schema(description = "Order Status", example = "UNPROCESSED")
    private OrderStatus orderStatus;

    @Schema(description = "Order Date", example = "2025-01-01")
    private LocalDate orderDate;

    @Schema(description = "The items associated with the order")
    @NotNull(message = ErrorMessages.ORDER_MUST_CONTAIN_ITEMS)
    @NotEmpty(message = ErrorMessages.ORDER_MUST_NOT_BE_EMPTY)
    private List<OrderLineDTO> orderLines;

}
