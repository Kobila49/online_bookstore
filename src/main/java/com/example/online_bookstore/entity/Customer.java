package com.example.online_bookstore.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "customers")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString(onlyExplicitlyIncluded = true)
public class Customer implements Serializable {

    @ToString.Include
    @Id
    @GeneratedValue
    private Long id;

    @ToString.Include
    @Column
    private String firstName;

    @ToString.Include
    @Column
    private String lastName;

    @ToString.Include
    @Column(unique = true)
    private String oib;

    @ToString.Include
    @Builder.Default
    @Column
    private BigInteger loyaltyPoints = BigInteger.ZERO;

    @Builder.Default
    @OneToMany(mappedBy = "customer")
    private Set<Order> orders = new HashSet<>();

}
