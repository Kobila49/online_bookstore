package com.example.online_bookstore.entity;

import com.example.online_bookstore.enums.BookType;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "books")
@Getter
@ToString(onlyExplicitlyIncluded = true)
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Book implements Serializable {
    @ToString.Include
    @Id
    @GeneratedValue
    private Long id;

    @ToString.Include
    @Column
    private String title;

    @ToString.Include
    @Column
    private String author;

    @ToString.Include
    @Column
    @Enumerated(EnumType.STRING)
    private BookType type;

    @ToString.Include
    @Column
    private BigDecimal price;
    @Builder.Default
    @OneToMany(mappedBy = "book")
    private Set<BookOrder> bookOrders = new HashSet<>();
}
