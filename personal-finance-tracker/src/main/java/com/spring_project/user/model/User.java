package com.spring_project.user.model;

import com.spring_project.category.model.Category;
import com.spring_project.recurringPayment.model.RecurringPayment;
import com.spring_project.savingsGoal.model.SavingsGoal;
import com.spring_project.transaction.model.Transaction;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
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

    @Column(nullable = false)
    private BigDecimal balance;

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
    private List<RecurringPayment> recurringPayment = new ArrayList<>();

    @OneToMany(mappedBy = "goalOwner", fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<SavingsGoal> goals = new ArrayList<>();

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER,orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<Category> categories = new ArrayList<>();




}
