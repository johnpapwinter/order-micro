package com.example.orders.service;

import com.example.orders.dto.OrderDTO;
import com.example.orders.enums.OrderStatus;
import com.example.orders.exception.EntityNotFoundException;
import com.example.orders.exception.ErrorMessages;
import com.example.orders.model.Order;
import com.example.orders.repository.OrderRepository;
import com.example.orders.utils.mappers.OrderMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderLineService orderLineService;
    private final OrderMapper orderMapper;

    public OrderServiceImpl(OrderRepository orderRepository,
                            OrderLineService orderLineService,
                            OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderLineService = orderLineService;
        this.orderMapper = orderMapper;
    }

    @Override
    @Transactional
    public Long createOrder(OrderDTO dto) {
        Order order = orderMapper.toOrder(dto);
        dto.getOrderLines().forEach(orderLineDto -> {
            orderLineService.createOrderLine(orderLineDto, order);
        });

        orderRepository.save(order);
        return order.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrder(Long id) {
        return orderRepository.findById(id).map(orderMapper::toOrderDTO).orElseThrow(
                () -> new EntityNotFoundException(ErrorMessages.ORDER_NOT_FOUND)
        );
    }

    @Override
    @Transactional
    public OrderDTO updateOrder(OrderDTO dto) {
        Order order = orderRepository.findById(dto.getId()).orElseThrow(
                () -> new EntityNotFoundException(ErrorMessages.ORDER_NOT_FOUND)
        );

        return null;
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void processOrders() {
        List<Order> orders = orderRepository.findAllByOrderStatus(OrderStatus.UNPROCESSED);
        orders.forEach(order -> {
            order.setOrderStatus(OrderStatus.PROCESSED);
        });
    }

}
