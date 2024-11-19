package com.example.orders.service;

import com.example.orders.dto.OrderDTO;
import com.example.orders.enums.OrderStatus;
import com.example.orders.model.Order;
import com.example.orders.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderLineService orderLineService;

    public OrderServiceImpl(OrderRepository orderRepository, OrderLineService orderLineService) {
        this.orderRepository = orderRepository;
        this.orderLineService = orderLineService;
    }

    @Override
    @Transactional
    public Long createOrder(OrderDTO dto) {
        Order order = new Order();
        order.setOrderId(dto.getOrderId());
        order.setCustomerName(dto.getCustomerName());
        order.setOrderStatus(OrderStatus.UNPROCESSED);
        order.setOrderDate(LocalDate.now());
        dto.getOrderLines().forEach(orderLineDto -> {
            orderLineService.createOrderLine(orderLineDto, order);
        });

        orderRepository.save(order);
        return order.getId();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrder(Long id) {
        return orderRepository.findById(id).map(order -> {
            OrderDTO dto = new OrderDTO();
            dto.setId(order.getId());
            dto.setOrderId(order.getOrderId());
            dto.setCustomerName(order.getCustomerName());
            dto.setOrderStatus(order.getOrderStatus());
            dto.setOrderDate(order.getOrderDate());
            return dto;
        }).orElse(null);
    }

    @Override
    @Transactional
    public OrderDTO updateOrder(OrderDTO dto) {
        Order order = orderRepository.findById(dto.getId()).orElse(null);

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
