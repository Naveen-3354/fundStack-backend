package com.test.FundStack.service;


import com.test.FundStack.entity.Category;
import com.test.FundStack.model.category.CategoryDTO;
import com.test.FundStack.repository.CategoryRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author NaveenDhanasekaran
 * 
 * History:
 * -13-02-2026 <NaveenDhanasekaran> CategoryService
 *      - Initial Version.
 */

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepo categoryRepository;

    // CREATE
    public CategoryDTO create(CategoryDTO dto) {

        Category parent = null;

        if (dto.getParentId() != null) {
            parent = categoryRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent not found"));
        }

        Category category = Category.builder()
                .name(dto.getName())
                .parent(parent)
                .build();

        categoryRepository.save(category);

        return mapToDTO(category);
    }

    // READ BY ID
    @Transactional
    public CategoryDTO getById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        return mapToDTO(category);
    }

    // GET ALL
    @Transactional
    public List<CategoryDTO> getAll() {
        return categoryRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    // UPDATE
    public CategoryDTO update(Long id, CategoryDTO dto) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setName(dto.getName());

        if (dto.getParentId() != null) {
            Category parent = categoryRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent not found"));
            category.setParent(parent);
        } else {
            category.setParent(null);
        }

        return mapToDTO(category);
    }

    // DELETE
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    private CategoryDTO mapToDTO(Category category) {
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .parentId(
                        category.getParent() != null
                                ? category.getParent().getId()
                                : null
                )
                .build();
    }
}
