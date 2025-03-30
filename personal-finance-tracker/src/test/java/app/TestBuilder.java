package app;

import app.category.model.Category;
import app.transaction.model.Transaction;
import app.transaction.model.Type;
import app.user.model.Role;
import app.user.model.User;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@UtilityClass
public class TestBuilder {

    public static User getUser() {


        UUID uuid = UUID.randomUUID();
        User user = User.builder()
                .id(uuid)
                .username("user123")
                .password("1234")
                .email("user@gmail.com")
                .balance(BigDecimal.ZERO)
                .role(Role.USER)
                .activeProfile(true)
                .build();

        Transaction transaction = Transaction.builder()
                .id(UUID.randomUUID())
                .transactionName("transactionName")
                .amount(BigDecimal.ONE)
                .owner(user)
                .type(Type.EXPENSE)
                .transactionDate(LocalDate.now())
                .build();

        Category category = Category.builder()
                .id(UUID.randomUUID())
                .name("testCategory")
                .systemCategories(false)
                .deleted(false)
                .owner(user)
                .build();


        transaction.setCategory(category);
        user.setCategories(List.of(category));
        user.setTransactions(List.of(transaction));
        return user;
    }


}
