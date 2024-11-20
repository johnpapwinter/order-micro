package com.example.orders.service;

import com.example.orders.dto.OrderLineDTO;
import com.example.orders.model.Order;
import com.example.orders.model.OrderLine;
import com.example.orders.repository.OrderLineRepository;
import com.example.orders.utils.mappers.OrderLineMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderLineServiceImplTest {

    @Mock
    private OrderLineRepository orderLineRepository;

    @Mock
    private OrderLineMapper orderLineMapper;

    private OrderLineService orderLineService;

    @BeforeEach
    void setUp() {
        orderLineService = new OrderLineServiceImpl(orderLineRepository, orderLineMapper);
    }

    @Test
    @DisplayName("Should create and associate OrderLine")
    void createOrderLine_ShouldCreateAndAssociateOrderLine() {
        // Arrange
        OrderLineDTO dto = OrderLineDTO.builder()
                .productId("PROD-1")
                .quantity(2)
                .price(10.0)
                .build();

        Order order = new Order();
        order.setOrderLines(new ArrayList<>());

        OrderLine orderLine = new OrderLine();
        orderLine.setProductId("PROD-1");
        orderLine.setQuantity(2);
        orderLine.setPrice(10.0);

        when(orderLineMapper.toOrderLine(dto)).thenReturn(orderLine);

        // Act
        orderLineService.createOrderLine(dto, order);

        // Assert
        verify(orderLineMapper).toOrderLine(dto);
        assertEquals(1, order.getOrderLines().size());
        assertEquals(orderLine, order.getOrderLines().get(0));
        assertEquals(order, orderLine.getOrder());
    }

    @Test
    @DisplayName("Should update quantity and price of OrderLine")
    void updateOrderLine_ShouldUpdateQuantityAndPrice() {
        // Arrange
        OrderLineDTO dto = OrderLineDTO.builder()
                .quantity(5)
                .price(15.0)
                .build();

        OrderLine existingOrderLine = new OrderLine();
        existingOrderLine.setQuantity(2);
        existingOrderLine.setPrice(10.0);
        existingOrderLine.setProductId("PROD-1");

        // Act
        orderLineService.updateOrderLine(dto, existingOrderLine);

        // Assert
        assertEquals(5, existingOrderLine.getQuantity());
        assertEquals(15.0, existingOrderLine.getPrice());
        // ProductId should remain unchanged
        assertEquals("PROD-1", existingOrderLine.getProductId());
    }

    @Test
    @DisplayName("Should delete all provided order lines")
    void deleteOrderLines_ShouldDeleteAllProvidedOrderLines() {
        // Arrange
        List<OrderLine> orderLines = List.of(
                new OrderLine(),
                new OrderLine()
        );

        // Act
        orderLineService.deleteOrderLines(orderLines);

        // Assert
        verify(orderLineRepository).deleteAll(orderLines);
    }

    @Test
    @DisplayName("Should handle null OrderLines")
    void createOrderLine_ShouldHandleNullOrderLines() {
        // Arrange
        OrderLineDTO dto = OrderLineDTO.builder()
                .productId("PROD-1")
                .quantity(2)
                .price(10.0)
                .build();

        Order order = new Order();
        // Don't initialize orderLines list

        OrderLine orderLine = new OrderLine();
        orderLine.setProductId("PROD-1");
        orderLine.setQuantity(2);
        orderLine.setPrice(10.0);

        when(orderLineMapper.toOrderLine(dto)).thenReturn(orderLine);

        // Act & Assert
        assertDoesNotThrow(() -> orderLineService.createOrderLine(dto, order));
        assertNotNull(order.getOrderLines());
        assertEquals(1, order.getOrderLines().size());
    }

    @Test
    @DisplayName("Should not update other fields")
    void updateOrderLine_ShouldNotUpdateOtherFields() {
        // Arrange
        OrderLineDTO dto = OrderLineDTO.builder()
                .productId("PROD-2") // This should not update
                .quantity(5)
                .price(15.0)
                .build();

        OrderLine existingOrderLine = new OrderLine();
        existingOrderLine.setQuantity(2);
        existingOrderLine.setPrice(10.0);
        existingOrderLine.setProductId("PROD-1");
        Order order = new Order();
        existingOrderLine.setOrder(order);

        // Act
        orderLineService.updateOrderLine(dto, existingOrderLine);

        // Assert
        assertEquals(5, existingOrderLine.getQuantity());
        assertEquals(15.0, existingOrderLine.getPrice());
        assertEquals("PROD-1", existingOrderLine.getProductId()); // Should not change
        assertEquals(order, existingOrderLine.getOrder()); // Should not change
    }

    @Test
    @DisplayName("Should handle empty list")
    void deleteOrderLines_ShouldHandleEmptyList() {
        // Arrange
        List<OrderLine> emptyList = List.of();

        // Act
        orderLineService.deleteOrderLines(emptyList);

        // Assert
        verify(orderLineRepository).deleteAll(emptyList);
    }

}