package com.example.orders.service;

import com.example.orders.dto.OrderDTO;
import com.example.orders.enums.OrderStatus;
import com.example.orders.exception.EntityNotFoundException;
import com.example.orders.exception.ErrorMessages;
import com.example.orders.model.Order;
import com.example.orders.model.OrderLine;
import com.example.orders.repository.OrderRepository;
import com.example.orders.utils.mappers.OrderLineMapper;
import com.example.orders.utils.mappers.OrderMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderLineService orderLineService;
    private final OrderMapper orderMapper;
    private final OrderLineMapper lineMapper;

    public OrderServiceImpl(OrderRepository orderRepository,
                            OrderLineService orderLineService,
                            OrderMapper orderMapper,
                            OrderLineMapper lineMapper) {
        this.orderRepository = orderRepository;
        this.orderLineService = orderLineService;
        this.orderMapper = orderMapper;
        this.lineMapper = lineMapper;
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
        // update fields
        order.setOrderStatus(dto.getOrderStatus());

        // find out if any lines where removed and delete them
        List<OrderLine> linesToRemove = order.getOrderLines().stream()
                .filter(existingLine -> dto.getOrderLines().stream()
                        .noneMatch(dtoLine -> Objects.equals(dtoLine.getId(), existingLine.getId())))
                .toList();

        order.getOrderLines().removeAll(linesToRemove);
        orderLineService.deleteOrderLines(linesToRemove);

        // add lines that where not there
        dto.getOrderLines().forEach(lineDto -> {
            if (lineDto.getId() == null) {
                OrderLine newLine = lineMapper.toOrderLine(lineDto);
                newLine.setOrder(order);
                order.getOrderLines().add(newLine);
            } else {
                order.getOrderLines().stream()
                        .filter(line -> Objects.equals(line.getId(), lineDto.getId()))
                        .findFirst()
                        .ifPresent(line -> {
                            line.setQuantity(lineDto.getQuantity());
                            line.setPrice(lineDto.getPrice());
                        });
            }
        });


        return orderMapper.toOrderDTO(order);
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
