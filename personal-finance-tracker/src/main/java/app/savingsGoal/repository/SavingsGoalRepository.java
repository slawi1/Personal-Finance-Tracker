package app.savingsGoal.repository;

import app.savingsGoal.model.SavingsGoal;
import app.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SavingsGoalRepository extends JpaRepository<SavingsGoal, UUID> {
    List<SavingsGoal> findAllByGoalOwner(User goalOwner);
}
