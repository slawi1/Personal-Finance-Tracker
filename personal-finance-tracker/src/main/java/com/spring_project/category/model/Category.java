package com.spring_project.category.model;

import com.spring_project.transaction.model.Transaction;
import com.spring_project.user.model.User;
import jakarta.persistence.*;
import lombok.*;

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

    @OneToMany(mappedBy = "category")
    private List<Transaction> transactions;

    @ManyToOne
    private User userCategories;


}
