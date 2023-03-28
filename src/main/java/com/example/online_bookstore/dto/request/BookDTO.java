package com.example.online_bookstore.dto.request;

import com.example.online_bookstore.enums.BookType;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record BookDTO(String title, String author, BookType type, @Positive BigDecimal price) {
}
