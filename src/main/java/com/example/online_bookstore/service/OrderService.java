package com.example.online_bookstore.service;

import com.example.online_bookstore.dto.request.OrderDTO;
import com.example.online_bookstore.dto.response.OrderResponse;

public interface OrderService {

    OrderResponse createOrder(OrderDTO orderDTO, Long customerId);
}
