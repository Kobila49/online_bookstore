package com.example.online_bookstore.repository;

import com.example.online_bookstore.entity.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Long> {
}
