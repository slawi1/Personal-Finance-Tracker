package com.spring_project.savingsGoal.repository;

import com.spring_project.savingsGoal.model.SavingsGoal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SavingsGoalRepository extends CrudRepository<SavingsGoal, UUID> {
}
