package itm.proyectoharoldo.backend.Repositories;

import itm.proyectoharoldo.backend.Models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByCategory(String category);
}
