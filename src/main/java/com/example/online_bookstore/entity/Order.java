package com.example.online_bookstore.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;
@Entity(name = "orders")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
public class Order implements Serializable {
    @ToString.Include
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Builder.Default
    @OneToMany(mappedBy = "order")
    private Set<BookOrder> bookOrders = new HashSet<>();

    @ToString.Include
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column
    private BigDecimal totalPrice;


    @PrePersist
    @PreUpdate
    public void pricePrecisionConversion() {
        this.totalPrice = this.totalPrice.setScale(2, RoundingMode.HALF_UP);
    }

}
