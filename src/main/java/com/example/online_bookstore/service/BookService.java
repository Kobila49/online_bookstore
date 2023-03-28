package com.example.online_bookstore.service;

import com.example.online_bookstore.dto.request.BookDTO;
import com.example.online_bookstore.dto.response.BookResponse;

import java.util.List;

public interface BookService {

    List<BookResponse> getBooks();

    void addNewBook(BookDTO bookDTO);

    void updateBook(BookDTO bookDTO, Long id);

    void deleteBook(Long id);

    BookResponse getBook(Long id);
}
