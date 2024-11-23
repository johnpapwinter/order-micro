package com.example.logging.repository;

import com.example.logging.model.LoggedOrder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoggedOrderRepository extends MongoRepository<LoggedOrder, String> {


}
