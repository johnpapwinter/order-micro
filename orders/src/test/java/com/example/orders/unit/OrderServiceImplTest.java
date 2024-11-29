package com.example.orders.unit;

import com.example.orders.dto.OrderDTO;
import com.example.orders.dto.OrderLineDTO;
import com.example.orders.dto.ProcessedOrderDTO;
import com.example.orders.enums.OrderStatus;
import com.example.orders.exception.DataMismatchException;
import com.example.orders.exception.EntityNotFoundException;
import com.example.orders.feign.LoggingFeignClient;
import com.example.orders.model.Order;
import com.example.orders.model.OrderLine;
import com.example.orders.repository.OrderRepository;
import com.example.orders.service.LoggingMessageService;
import com.example.orders.service.OrderLineService;
import com.example.orders.service.OrderService;
import com.example.orders.service.OrderServiceImpl;
import com.example.orders.utils.mappers.OrderMapper;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderLineService orderLineService;

    @Mock
    private LoggingFeignClient loggingFeignClient;

    @Mock
    private LoggingMessageService loggingMessageService;

    @Mock
    private OrderMapper orderMapper;

    private OrderService orderService;


    @BeforeEach
    void setUp() {
        orderService = new OrderServiceImpl(orderRepository, orderLineService, loggingFeignClient, orderMapper, loggingMessageService);
    }

    @Test
    @DisplayName("Should create Order with associated items")
    void createOrder_ShouldCreateOrderWithLines() {
        // Arrange
        OrderDTO orderDTO = createSampleOrderDTO();
        Order order = createSampleOrder();

        when(orderMapper.toOrder(orderDTO)).thenReturn(order);
        when(orderRepository.save(order)).thenReturn(order);

        // Act
        Long resultId = orderService.createOrder(orderDTO);

        // Assert
        verify(orderMapper).toOrder(orderDTO);
        verify(orderRepository).save(order);
        orderDTO.getOrderLines().forEach(lineDto ->
                verify(orderLineService).createOrderLine(lineDto, order));
        assertEquals(order.getId(), resultId);
    }

    @Test
    @DisplayName("Should return OrderDTO")
    void getOrder_ShouldReturnOrderDTO() {
        // Arrange
        Long orderId = 1L;
        Order order = createSampleOrder();
        OrderDTO orderDTO = createSampleOrderDTO();

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderMapper.toOrderDTO(order)).thenReturn(orderDTO);

        // Act
        OrderDTO result = orderService.getOrder(orderId);

        // Assert
        verify(orderRepository).findById(orderId);
        verify(orderMapper).toOrderDTO(order);
        assertEquals(orderDTO, result);
    }

    @Test
    @DisplayName("Should throw exception when Order not found")
    void getOrder_ShouldThrowException_WhenOrderNotFound() {
        // Arrange
        Long orderId = 1L;
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> orderService.getOrder(orderId));
    }

    @Test
    @DisplayName("Should update Order and associated items")
    void updateOrder_ShouldUpdateOrderAndLines() {
        // Arrange
        Long orderId = 1L;
        OrderDTO orderDTO = createSampleOrderDTO();
        orderDTO.setId(orderId);
        Order existingOrder = createSampleOrder();
        existingOrder.setId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(orderMapper.toOrderDTO(existingOrder)).thenReturn(orderDTO);

        // Act
        OrderDTO result = orderService.updateOrder(orderId, orderDTO);

        // Assert
        verify(orderRepository).findById(orderId);
        assertEquals(orderDTO.getOrderStatus(), existingOrder.getOrderStatus());
        verify(orderMapper).toOrderDTO(existingOrder);
    }

    @Test
    @DisplayName("Should throw exception when there is an ID mismatch")
    void updateOrder_ShouldThrowException_WhenIdsMismatch() {
        // Arrange
        Long pathId = 1L;
        OrderDTO orderDTO = createSampleOrderDTO();
        orderDTO.setId(2L);

        // Act & Assert
        assertThrows(DataMismatchException.class,
                () -> orderService.updateOrder(pathId, orderDTO));
    }

    @Test
    @DisplayName("Should delete an Order")
    void deleteOrder_ShouldDeleteOrder() {
        // Arrange
        Long orderId = 1L;

        // Act
        orderService.deleteOrder(orderId);

        // Assert
        verify(orderRepository).deleteById(orderId);
    }

    @Test
    @DisplayName("Should return a Page of Orders")
    void getOrders_ShouldReturnPageOfOrders() {
        // Arrange
        Pageable pageable = mock(Pageable.class);
        List<Order> orders = List.of(createSampleOrder());
        Page<Order> orderPage = new PageImpl<>(orders);
        Page<OrderDTO> expectedDtoPage = new PageImpl<>(List.of(createSampleOrderDTO()));

        when(orderRepository.findAll(pageable)).thenReturn(orderPage);
        when(orderMapper.toOrderDTO(any(Order.class))).thenReturn(createSampleOrderDTO());

        // Act
        Page<OrderDTO> result = orderService.getOrders(pageable);

        // Assert
        verify(orderRepository).findAll(pageable);
        assertEquals(expectedDtoPage.getContent().size(), result.getContent().size());
    }

    @Test
    @DisplayName("Should process orders successfully")
    void processOrders_ShouldProcessAllOrdersSuccessfully() {
        // Arrange
        List<Order> unprocessedOrders = List.of(
                createSampleOrder(),
                createSampleOrder()
        );
        when(orderRepository.findAllByOrderStatus(OrderStatus.UNPROCESSED))
                .thenReturn(unprocessedOrders);

        // Act
        orderService.processOrders();

        // Assert
        verify(orderRepository).findAllByOrderStatus(OrderStatus.UNPROCESSED);
        verify(loggingFeignClient, times(2)).storeProcessedOrder(any(ProcessedOrderDTO.class));
        verify(orderRepository, times(2)).save(any(Order.class));
        unprocessedOrders.forEach(order ->
                assertEquals(OrderStatus.PROCESSED, order.getOrderStatus()));
    }

    @Test
    @DisplayName("Should handle empty order list")
    void processOrders_ShouldHandleEmptyList() {
        // Arrange
        when(orderRepository.findAllByOrderStatus(OrderStatus.UNPROCESSED))
                .thenReturn(List.of());

        // Act
        orderService.processOrders();

        // Assert
        verify(orderRepository).findAllByOrderStatus(OrderStatus.UNPROCESSED);
        verify(loggingFeignClient, never()).storeProcessedOrder(any());
        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should process some orders successfully while others fail")
    void processOrders_ShouldHandlePartialSuccess() {
        // Arrange
        Order successOrder = createSampleOrder();
        successOrder.setOrderId("SUCCESS-1");
        Order failOrder = createSampleOrder();
        failOrder.setOrderId("FAIL-1");

        when(orderRepository.findAllByOrderStatus(OrderStatus.UNPROCESSED))
                .thenReturn(List.of(successOrder, failOrder));

        doNothing()
                .doThrow(FeignException.class)
                .when(loggingFeignClient)
                .storeProcessedOrder(any(ProcessedOrderDTO.class));

        // Act
        orderService.processOrders();

        // Assert
        verify(orderRepository).findAllByOrderStatus(OrderStatus.UNPROCESSED);
        verify(orderRepository, times(1)).save(any()); // Only successful order should be saved
        assertEquals(OrderStatus.PROCESSED, successOrder.getOrderStatus());
        assertEquals(OrderStatus.UNPROCESSED, failOrder.getOrderStatus());
    }

    @Test
    @DisplayName("Should handle item updates")
    void updateOrder_ShouldHandleLineUpdates() {
        // Arrange
        Long orderId = 1L;
        OrderDTO orderDTO = createSampleOrderDTO();
        orderDTO.setId(orderId);

        Order existingOrder = createSampleOrder();
        existingOrder.setId(orderId);

        OrderLine existingLine = new OrderLine();
        existingLine.setId(1L);
        existingOrder.setOrderLines(new ArrayList<>(List.of(existingLine)));

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(orderMapper.toOrderDTO(existingOrder)).thenReturn(orderDTO);

        // Add a new line and modify existing one
        OrderLineDTO newLineDTO = OrderLineDTO.builder().quantity(1).price(10.0).build();
        OrderLineDTO existingLineDTO = OrderLineDTO.builder().id(1L).quantity(2).price(20.0).build();
        orderDTO.setOrderLines(List.of(newLineDTO, existingLineDTO));

        // Act
        OrderDTO result = orderService.updateOrder(orderId, orderDTO);

        // Assert
        verify(orderLineService).createOrderLine(newLineDTO, existingOrder);
        verify(orderLineService).updateOrderLine(existingLineDTO, existingLine);
    }

    private OrderDTO createSampleOrderDTO() {
        return OrderDTO.builder()
                .orderId("ORD-001")
                .customerName("John Doe")
                .orderStatus(OrderStatus.UNPROCESSED)
                .orderDate(LocalDate.now())
                .orderLines(new ArrayList<>(List.of(
                        OrderLineDTO.builder()
                                .productId("PROD-1")
                                .quantity(1)
                                .price(10.0)
                                .build()
                )))
                .build();
    }

    private Order createSampleOrder() {
        Order order = new Order();
        order.setId(1L);
        order.setOrderId("ORD-001");
        order.setCustomerName("John Doe");
        order.setOrderStatus(OrderStatus.UNPROCESSED);
        order.setOrderDate(LocalDate.now());
        order.setOrderLines(new ArrayList<>());
        return order;
    }

    private ProcessedOrderDTO createExpectedProcessedOrderDTO(Order order) {
        return ProcessedOrderDTO.builder()
                .orderId(order.getOrderId())
                .amount(order.getOrderLines().stream()
                        .mapToDouble(OrderLine::getPrice)
                        .sum())
                .itemsCount(order.getOrderLines().size())
                .date(order.getOrderDate())
                .build();
    }

}