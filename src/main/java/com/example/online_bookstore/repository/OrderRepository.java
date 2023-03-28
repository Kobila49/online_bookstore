package com.example.online_bookstore.repository;

import com.example.online_bookstore.entity.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {
}
