package app.transaction.model;

import app.category.model.Category;
import app.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String transactionName;


    @Column(nullable = false)
    private BigDecimal amount;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id",nullable = false)
    private Category category;

    @ManyToOne()
    @JoinColumn(name = "owner_id",nullable = false)
    private User owner;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    @Column(nullable = false)
    private LocalDate transactionDate;

    @Column(nullable = false)
    private LocalDateTime transactionCreationDate;

    private String description;

    public Transaction(String transactionName, BigDecimal amount, Type type, LocalDate transactionDate, String description) {
        this.transactionName = transactionName;
        this.amount = amount;
        this.type = type;
        this.transactionDate = transactionDate;
        this.description = description;
    }

}
