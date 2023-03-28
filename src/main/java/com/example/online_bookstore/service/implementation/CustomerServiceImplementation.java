package com.example.online_bookstore.service.implementation;

import com.example.online_bookstore.dto.request.CustomerDTO;
import com.example.online_bookstore.dto.response.CustomerResponse;
import com.example.online_bookstore.entity.Customer;
import com.example.online_bookstore.exception.ResourceNotFoundException;
import com.example.online_bookstore.repository.CustomerRepository;
import com.example.online_bookstore.service.CustomerService;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class CustomerServiceImplementation implements CustomerService {

    private final CustomerRepository repository;

    public CustomerServiceImplementation(CustomerRepository repository) {
        this.repository = repository;
    }


    @Override
    public Long addNewCustomer(CustomerDTO customerDTO) {
        var customer = repository.save(createCustomer(customerDTO));
        return customer.getId();
    }

    @Override
    public CustomerResponse getCustomer(Long id) {
        var optionalCustomer = repository.findById(id);
        if (optionalCustomer.isEmpty()) {
            throw new ResourceNotFoundException("Not found customer with given id: %d".formatted(id));
        } else {
            return getCustomerResponse(optionalCustomer.get());
        }
    }

    public static CustomerResponse getCustomerResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getOib(),
                customer.getLoyaltyPoints());
    }

    private Customer createCustomer(CustomerDTO bookDTO) {
        return Customer.builder()
                .firstName(bookDTO.firstName())
                .lastName(bookDTO.lastName())
                .oib(bookDTO.oib())
                .loyaltyPoints(BigInteger.ZERO)
                .build();
    }
}
