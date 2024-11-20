package com.example.orders.dto;

import com.example.orders.enums.OrderStatus;
import com.example.orders.exception.ErrorMessages;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class OrderDTO {

    private Long id;
    @NotBlank(message = ErrorMessages.ORDER_ID_CANNOT_BE_EMPTY)
    private String orderId;
    @NotBlank(message = ErrorMessages.CUSTOMER_NAME_CANNOT_BE_EMPTY)
    private String customerName;
    private OrderStatus orderStatus;
    private LocalDate orderDate;
    @NotNull(message = ErrorMessages.ORDER_MUST_CONTAIN_ITEMS)
    @NotEmpty(message = ErrorMessages.ORDER_MUST_NOT_BE_EMPTY)
    private List<OrderLineDTO> orderLines;

}
