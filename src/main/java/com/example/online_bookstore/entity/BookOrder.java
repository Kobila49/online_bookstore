package com.example.online_bookstore.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity(name = "bookOrders")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
public class BookOrder implements Serializable {

    @ToString.Include
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ToString.Include
    @Column
    private BigDecimal sellingPrice;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @PrePersist
    @PreUpdate
    public void pricePrecisionConversion() {
        this.sellingPrice = this.sellingPrice.setScale(2, RoundingMode.HALF_UP);
    }
}
