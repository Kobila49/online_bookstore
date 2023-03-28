package com.example.online_bookstore.dto.response;

import java.math.BigInteger;

public record CustomerResponse(Long id,String firstName, String lastName, String oib, BigInteger loyaltyPoints) {
}
