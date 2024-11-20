package com.example.orders.service;

import com.example.orders.dto.OrderLineDTO;
import com.example.orders.model.Order;
import com.example.orders.model.OrderLine;
import com.example.orders.repository.OrderLineRepository;
import com.example.orders.utils.mappers.OrderLineMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderLineServiceImpl implements OrderLineService {

    private final OrderLineRepository orderLineRepository;
    private final OrderLineMapper orderLineMapper;

    public OrderLineServiceImpl(OrderLineRepository orderLineRepository,
                                OrderLineMapper orderLineMapper) {
        this.orderLineRepository = orderLineRepository;
        this.orderLineMapper = orderLineMapper;
    }

    /**
     * Takes an OrderLineDTO and an Order and will create a new OrderLine entity and associate it with the Order
     * @param dto The OrderLineDTO of the entity to be created
     * @param order The Order entity that the new OrderLine will be attached to
     */
    @Override
    @Transactional
    public void createOrderLine(OrderLineDTO dto, Order order) {
        OrderLine orderLine = orderLineMapper.toOrderLine(dto);
        orderLine.setOrder(order);
        order.getOrderLines().add(orderLine);
    }

    /**
     * Takes an OrderLineDTO object and an OrderLine entity and will update the entity based on the DTO
     * @param dto The OrderLineDTO containing the updated fields
     * @param orderLine The OrderLine entity that will be updated
     */
    @Override
    @Transactional
    public void updateOrderLine(OrderLineDTO dto, OrderLine orderLine) {
        // ONLY THE QUANTITY AND THE PRICE ARE UPDATABLE
        orderLine.setQuantity(dto.getQuantity());
        orderLine.setPrice(dto.getPrice());
    }

    /**
     * Will take a List of OrderLine entities and will delete them from the database
     * @param orderLines The List<OrderLine> with the entities to be deleted
     */
    @Override
    public void deleteOrderLines(List<OrderLine> orderLines) {

        orderLineRepository.deleteAll(orderLines);
    }

}
