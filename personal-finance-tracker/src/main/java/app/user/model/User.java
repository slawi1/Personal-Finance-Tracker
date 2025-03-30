package app.user.model;

import app.category.model.Category;
import app.recurringPayment.model.RecurringPayment;
import app.savingsGoal.model.SavingsGoal;
import app.transaction.model.Transaction;
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
@Table(name = "\"users\"")
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

    @OneToMany(mappedBy = "goalOwner", fetch = FetchType.EAGER)
    private List<SavingsGoal> goals = new ArrayList<>();

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    private List<Transaction> transactions = new ArrayList<>();

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    private List<Category> categories = new ArrayList<>();




}
