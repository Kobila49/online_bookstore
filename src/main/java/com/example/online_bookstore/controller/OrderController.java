package com.example.online_bookstore.controller;

import com.example.online_bookstore.dto.request.OrderDTO;
import com.example.online_bookstore.dto.response.OrderResponse;
import com.example.online_bookstore.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(path = "/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping("/{id}")
    public ResponseEntity<OrderResponse> createOrder(@PathVariable(name = "id") Long customerId, @Valid @RequestBody OrderDTO orderDTO) {

        return ResponseEntity
                .created(getUri(customerId))
                .body(this.service.createOrder(orderDTO, customerId));
    }

    private static URI getUri(Long id) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replacePath("/api/orders/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
