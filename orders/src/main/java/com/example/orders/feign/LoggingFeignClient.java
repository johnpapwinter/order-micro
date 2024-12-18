package com.example.orders.feign;

import com.example.orders.dto.ProcessedOrderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(
        name = "${app-feign-names.logging-service.name}",
        url = "${app-feign-names.logging-service.url}/api/logs")
public interface LoggingFeignClient {

    @PostMapping
    void storeProcessedOrder(ProcessedOrderDTO processedOrderDTO);

}
