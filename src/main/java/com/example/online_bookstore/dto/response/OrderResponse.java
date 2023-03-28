package com.example.online_bookstore.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record OrderResponse(Long id,
                            BigDecimal totalPrice,
                            List<BookOrderResponse> bookOrders,
                            CustomerResponse customer) {
}
