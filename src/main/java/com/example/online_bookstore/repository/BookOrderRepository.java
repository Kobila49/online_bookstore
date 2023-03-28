package com.example.online_bookstore.repository;

import com.example.online_bookstore.entity.BookOrder;
import org.springframework.data.repository.CrudRepository;

public interface BookOrderRepository extends CrudRepository<BookOrder, Long> {

}
