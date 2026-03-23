package itm.proyectoharoldo.backend.Controllers;

import itm.proyectoharoldo.backend.Repositories.CategoryRepository;
import itm.proyectoharoldo.backend.Models.Category;
import itm.proyectoharoldo.backend.Models.DTO.CategoryDTO;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@AllArgsConstructor
public class CategoriesController {

    private final CategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getById(@PathVariable Long id){
        return categoryRepository.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Category> create(@RequestBody CategoryDTO category){
        Category newCategory = new Category();
        newCategory.setTitle(category.getTitle());
        newCategory.setDecimalvalue(BigDecimal.ZERO);
        newCategory.setDescription(category.getDescription());
        return ResponseEntity.ok(categoryRepository.save(newCategory));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> update(@PathVariable Long id, @RequestBody Category category) {
        if (!categoryRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(categoryRepository.save(category));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @RequestBody Category category) {
        if (!categoryRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        categoryRepository.delete(category);
        return ResponseEntity.ok().build();
    }

}
