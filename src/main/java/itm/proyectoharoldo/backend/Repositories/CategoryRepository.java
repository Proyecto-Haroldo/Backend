package itm.proyectoharoldo.backend.Repositories;

import itm.proyectoharoldo.backend.Models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
