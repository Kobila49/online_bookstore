package com.example.online_bookstore;

import com.example.online_bookstore.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
public class SharedTestClass {

    @Autowired
    protected BookRepository bookRepository;
}
