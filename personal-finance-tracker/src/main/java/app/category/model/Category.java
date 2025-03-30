package app.category.model;

import app.transaction.model.Transaction;
import app.user.model.User;
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

    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL)
    private List<Transaction> transactions;

    private boolean systemCategories;

    private boolean deleted;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    public Category(String name, User owner,BigDecimal amount, boolean systemCategories, boolean deleted) {
        this.name = name;
        this.owner = owner;
        this.amount = amount;
        this.systemCategories = systemCategories;
        this.deleted = deleted;

    }


}
