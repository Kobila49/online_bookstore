package com.example.online_bookstore.repository;

import com.example.online_bookstore.entity.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
}
