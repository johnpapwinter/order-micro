package com.example.logging.migrations;

import com.example.logging.model.LoggedOrder;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Profile("dev")
@ChangeUnit(id = "seed-logged-orders", order = "001", author = "johnpapwinter")
public class V0_SeedDevData {

    @Execution
    public void changeSet(MongoTemplate mongoTemplate) {
        List<LoggedOrder> initialOrders = Arrays.asList(
                createLoggedOrder("XK1203", 30.2, 2, LocalDate.of(2024, 11, 20)),
                createLoggedOrder("XK2304", 35.3, 3, LocalDate.of(2024, 11, 20))
        );

        mongoTemplate.insertAll(initialOrders);
    }

    @RollbackExecution
    public void rollback(MongoTemplate mongoTemplate) {
        mongoTemplate.dropCollection(LoggedOrder.class);
    }


    private LoggedOrder createLoggedOrder(String orderId, Double amount, Integer itemsCount, LocalDate date) {
        LoggedOrder order = new LoggedOrder();
        order.setOrderId(orderId);
        order.setAmount(amount);
        order.setItemsCount(itemsCount);
        order.setDate(date);
        return order;
    }


}
