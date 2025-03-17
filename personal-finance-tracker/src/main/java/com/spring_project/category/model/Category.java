package com.spring_project.category.model;

import com.spring_project.transaction.model.Transaction;
import com.spring_project.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private BigDecimal amount;

    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL,fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Transaction> transactions;


    @ManyToOne
    @JoinColumn(name = "owner_id",nullable = false)
    private User owner;

    public Category(String name, User owner,BigDecimal amount) {
        this.name = name;
        this.owner = owner;
        this.amount = amount;

    }


}
