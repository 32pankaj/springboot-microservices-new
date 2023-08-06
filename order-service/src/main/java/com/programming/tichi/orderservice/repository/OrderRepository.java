package com.programming.tichi.orderservice.repository;

import com.programming.tichi.orderservice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
