package com.spring_project.user.model;

import com.spring_project.category.model.Category;
import com.spring_project.recurringPayment.model.RecurringPayment;
import com.spring_project.savingsGoal.model.SavingsGoal;
import com.spring_project.transaction.model.Transaction;
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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    private String firstName;

    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    private String profilePicture;

    private Boolean activeProfile;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    private List<RecurringPayment> recurringPayment;

    @OneToMany(mappedBy = "goalOwner", fetch = FetchType.EAGER)
    private List<SavingsGoal> goals;

    @OneToMany(mappedBy = "transactionUser", fetch = FetchType.EAGER)
    private List<Transaction> transactions;

    @OneToMany(mappedBy = "userCategories", fetch = FetchType.EAGER)
    private List<Category> categories;




}
