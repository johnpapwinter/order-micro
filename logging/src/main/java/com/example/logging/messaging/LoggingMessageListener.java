package com.example.logging.messaging;

import com.example.logging.dto.LoggedOrderDTO;
import com.example.logging.dto.ResponseDTO;
import com.example.logging.service.LoggedOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class LoggingMessageListener {

    private static final Logger log = LoggerFactory.getLogger(LoggingMessageListener.class);

    private final LoggedOrderService loggedOrderService;

    public LoggingMessageListener(LoggedOrderService loggedOrderService) {
        this.loggedOrderService = loggedOrderService;
    }


    @RabbitListener(queues = "${rabbitmq.queue.logging.request}")
    public ResponseDTO handleProcessedOrder(LoggedOrderDTO dto) {
        try {
            log.info("Received processed order: {}", dto.getOrderId());
            loggedOrderService.createLoggedOrder(dto);
            log.info("Successfully stored processed order: {}", dto.getOrderId());

            return new ResponseDTO(dto.getOrderId(), true, "ORDER PROCESSED SUCCESSFULLY");
        } catch (Exception e) {
            log.error("Error processing order: {}", dto.getOrderId(), e);

            return new ResponseDTO(dto.getOrderId(), false, "ORDER PROCESSED SUCCESSFULLY");
        }
    }


}
