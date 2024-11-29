package com.example.orders.service;

import com.example.orders.dto.ProcessedOrderDTO;
import com.example.orders.dto.ResponseDTO;
import com.example.orders.exception.ErrorMessages;
import com.example.orders.exception.LoggingServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LoggingMessageService {

    private static final Logger log = LoggerFactory.getLogger(LoggingMessageService.class);

    private final RabbitTemplate rabbitTemplate;
    private final String loggingRequestQueue;

    public LoggingMessageService(RabbitTemplate rabbitTemplate,
                                 @Value("${rabbitmq.queue.logging.request}") String loggingRequestQueue) {
        this.rabbitTemplate = rabbitTemplate;
        this.loggingRequestQueue = loggingRequestQueue;
    }


    public void storeProcessedOrder(ProcessedOrderDTO dto) {
        log.info("Sending order to logging service: {}", dto.getOrderId());

        try {
            ResponseDTO response = (ResponseDTO) rabbitTemplate.convertSendAndReceive(loggingRequestQueue, dto);

            if (response == null) {
                throw new LoggingServiceException(ErrorMessages.TIMEOUT_WAITING_FOR_PROCESSING);
            }

            if (!response.isSuccess()) {
                throw new LoggingServiceException(ErrorMessages.FAILED_TO_PROCESS_ORDER);
            }

            log.info("Successfully processed order {}", dto.getOrderId());
        } catch (Exception e) {
            e.printStackTrace();
            throw new LoggingServiceException(ErrorMessages.FAILED_TO_PROCESS_ORDER);
        }
    }


}
