package com.example.orders.service;

import com.example.orders.dto.OrderDTO;
import com.example.orders.dto.ProcessedOrderDTO;
import com.example.orders.enums.OrderStatus;
import com.example.orders.exception.DataMismatchException;
import com.example.orders.exception.EntityNotFoundException;
import com.example.orders.exception.ErrorMessages;
import com.example.orders.feign.LoggingFeignClient;
import com.example.orders.model.Order;
import com.example.orders.model.OrderLine;
import com.example.orders.repository.OrderRepository;
import com.example.orders.utils.mappers.OrderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class OrderServiceImpl implements OrderService {

    Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);


    private final OrderRepository orderRepository;
    private final OrderLineService orderLineService;
    private final LoggingFeignClient loggingFeignClient;
    private final OrderMapper orderMapper;

    public OrderServiceImpl(OrderRepository orderRepository,
                            OrderLineService orderLineService,
                            LoggingFeignClient loggingFeignClient,
                            OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderLineService = orderLineService;
        this.loggingFeignClient = loggingFeignClient;
        this.orderMapper = orderMapper;
    }

    /**
     * Adds a new Order and its associated OrderLines based on the provided OrderDTO
     * @param dto The OrderDTO object containing the Order details and associated items
     * @return The ID of the newly created order.
     */
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

    /**
     * Takes an id and returns a DTO of the Order found
     * @param id The ID of the Order to be found
     * @return An OrderDTO object
     */
    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrder(Long id) {
        return orderRepository.findById(id).map(orderMapper::toOrderDTO).orElseThrow(
                () -> new EntityNotFoundException(ErrorMessages.ORDER_NOT_FOUND)
        );
    }

    /**
     * Takes an id and a DTO object and updates an Order entity and its associated OrderLines
     * @param id The ID of the Order to be updated
     * @param dto The OrderDTO object containing the updated values and the associated OrderLineDTOs
     * @return An OrderDTO with the updated Order
     */
    @Override
    @Transactional
    public OrderDTO updateOrder(Long id, OrderDTO dto) {
        doIdsMatch(id, dto.getId());

        // I CONSIDER THAT ONLY THE STATUS AND THE ITEMS ARE UPDATABLE IN THE ORDER
        Order order = orderRepository.findById(dto.getId()).orElseThrow(
                () -> new EntityNotFoundException(ErrorMessages.ORDER_NOT_FOUND)
        );
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
                orderLineService.createOrderLine(lineDto, order);
            } else {
                order.getOrderLines().stream()
                        .filter(line -> Objects.equals(line.getId(), lineDto.getId()))
                        .findFirst()
                        .ifPresent(line -> orderLineService.updateOrderLine(lineDto, line));
            }
        });


        return orderMapper.toOrderDTO(order);
    }

    /**
     * Will remove an Order based on the ID parameter
     * @param id The ID of the Order to be deleted
     */
    @Override
    @Transactional
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    /**
     * Takes a Spring pageable object and returns a Page of OrderDTOs
     * @param pageable The Spring Pageable object
     * @return a Page of OrderDTO objects
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OrderDTO> getOrders(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(orderMapper::toOrderDTO);
    }

    /**
     * Will update all Orders to "PROCESSED" if they are "UNPROCESSED"
     */
    @Override
    @Transactional
    public void processOrders() {
        List<Order> orders = orderRepository.findAllByOrderStatus(OrderStatus.UNPROCESSED);
        if (orders.isEmpty()) {
            LOGGER.info("No unprocessed orders found");
            return;
        }

        long successCount = orders.stream()
                .map(order -> {
                    try {
                        processIndividualOrder(order);
                        LOGGER.info("Successfully processed order: {}", order.getOrderId());
                        return true;
                    } catch (Exception e) {
                        LOGGER.error("Failed to process order: {}: {}", order.getOrderId(), e.getMessage());
                        return false;
                    }
                })
                .filter(success -> success)
                .count();

        LOGGER.info("Order processing completed. Successes: {}, Failures: {}",
                successCount, orders.size() - successCount
        );
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected void processIndividualOrder(Order order) {
        ProcessedOrderDTO processedOrderDTO = ProcessedOrderDTO.builder()
                .orderId(order.getOrderId())
                .amount(order.getOrderLines().stream()
                        .mapToDouble(OrderLine::getPrice)
                        .sum()
                )
                .itemsCount(order.getOrderLines().size())
                .date(order.getOrderDate())
                .build();

        storeProcessedOrder(processedOrderDTO);
        order.setOrderStatus(OrderStatus.PROCESSED);

        orderRepository.save(order);
    }

    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000, multiplier = 2))
    protected void storeProcessedOrder(ProcessedOrderDTO processedOrderDTO) {
        loggingFeignClient.storeProcessedOrder(processedOrderDTO);
    }


    private void doIdsMatch(Long pathId, Long dtoId) {
        if (!pathId.equals(dtoId)) {
            throw new DataMismatchException(ErrorMessages.DATA_MISMATCH_ERROR);
        }
    }

}
