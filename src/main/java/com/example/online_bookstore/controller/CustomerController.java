package com.example.online_bookstore.controller;

import com.example.online_bookstore.dto.request.CustomerDTO;
import com.example.online_bookstore.dto.response.CustomerResponse;
import com.example.online_bookstore.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(path = "/customers")
public class CustomerController {

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }


    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(this.service.getCustomer(id));
    }

    @PostMapping
    public ResponseEntity<Void> addCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        var id = this.service.addNewCustomer(customerDTO);
        return ResponseEntity
                .created(getUri(id))
                .build();
    }

    private static URI getUri(Long id) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .replacePath("/api/customers/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
