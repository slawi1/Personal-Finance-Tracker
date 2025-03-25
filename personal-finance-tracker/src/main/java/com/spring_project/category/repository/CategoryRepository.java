package com.spring_project.category.repository;

import com.spring_project.category.model.Category;
import com.spring_project.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    Optional<Category> findCategoryByName(String name);

    Optional<Category> findCategoryByNameAndOwner(String name, User user);

    List<Category> findCategoriesByOwnerAndDeleted(User owner, boolean deleted);

    Optional<Category> findCategoryById(UUID id);
}
