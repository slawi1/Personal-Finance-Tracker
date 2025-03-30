package app.category.repository;

import app.category.model.Category;
import app.user.model.User;
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
