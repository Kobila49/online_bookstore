package com.example.online_bookstore.dto.response;

import java.math.BigDecimal;

public record BookOrderResponse(String title, String author, BigDecimal chargedPrice) {
}
