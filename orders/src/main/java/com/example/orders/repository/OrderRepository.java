package com.example.orders.repository;

import com.example.orders.enums.OrderStatus;
import com.example.orders.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByOrderStatus(OrderStatus orderStatus);

}
