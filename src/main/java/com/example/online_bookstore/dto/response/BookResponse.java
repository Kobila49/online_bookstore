package com.example.online_bookstore.dto.response;

import com.example.online_bookstore.enums.BookType;

import java.math.BigDecimal;

public record BookResponse(Long id, String title, String author, BookType type, BigDecimal price) {
}
