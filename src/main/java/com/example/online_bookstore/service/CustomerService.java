package com.example.online_bookstore.service;

import com.example.online_bookstore.dto.request.CustomerDTO;
import com.example.online_bookstore.dto.response.CustomerResponse;

public interface CustomerService {

    Long addNewCustomer(CustomerDTO customerDTO);

    CustomerResponse getCustomer(Long id);
}
