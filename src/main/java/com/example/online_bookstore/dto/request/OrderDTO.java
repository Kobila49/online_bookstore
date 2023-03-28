package com.example.online_bookstore.dto.request;

import java.util.List;

public record OrderDTO(List<Long> booksIds) {
}
