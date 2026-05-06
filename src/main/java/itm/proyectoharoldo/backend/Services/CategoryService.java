package itm.proyectoharoldo.backend.Services;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import itm.proyectoharoldo.backend.Models.Category;
import itm.proyectoharoldo.backend.Models.DTO.CategoryDTO;
import itm.proyectoharoldo.backend.Repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoryDTO getCategoryById(@NonNull Long id) {
        return categoryRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new NoSuchElementException("Categoría no encontrada con id: " + id));
    }

    @Transactional
    public CategoryDTO createCategory(CategoryDTO dto) {
        Category newCategory = new Category();
        newCategory.setTitle(dto.getTitle());
        newCategory.setDescription(dto.getDescription());
        newCategory.setDecimalvalue(BigDecimal.ZERO);
        newCategory.setIcon(dto.getIcon());
        return toDTO(categoryRepository.save(newCategory));
    }

    @SuppressWarnings("null")
    @Transactional
    public CategoryDTO updateCategory(Long id, CategoryDTO dto) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Categoría no encontrada con id: " + id));

        if (dto.getTitle() != null) existing.setTitle(dto.getTitle());
        if (dto.getDescription() != null) existing.setDescription(dto.getDescription());
        if (dto.getIcon() != 0) existing.setIcon(dto.getIcon());

        return toDTO(categoryRepository.save(existing));
    }

    @SuppressWarnings("null")
    @Transactional
    public void deleteCategory(Long id) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Categoría no encontrada con id: " + id));
        categoryRepository.delete(existing);
    }

    public CategoryDTO toDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setCategoryId(category.getCategoryid());
        dto.setTitle(category.getTitle());
        dto.setDescription(category.getDescription());
        dto.setIcon(category.getIcon());
        return dto;
    }
}
