package com.rps.smartsplit.controller;

import com.rps.smartsplit.dto.CategoryRequestDTO;
import com.rps.smartsplit.dto.CategoryResponseDTO;
import com.rps.smartsplit.dto.CategoryUpdateRequest;
import com.rps.smartsplit.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * GET /api/categories
     * List all categories (default + custom)
     */
    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        try {
            return ResponseEntity.ok(categoryService.getAllCategories());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * GET /api/categories/{id}
     * Get category details
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategory(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(categoryService.getCategoryById(id));

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * POST /api/categories
     * Create custom category
     */
    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(@RequestBody CategoryRequestDTO categoryRequestDTO) {
        try {
            return ResponseEntity.ok(categoryService.createCategory(categoryRequestDTO));

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * PUT /api/categories/{id}
     * Update category (only if user owns it or is admin)
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(
            @PathVariable UUID id,
            @RequestBody CategoryUpdateRequest categoryRequestDTO) {

        try {
            return ResponseEntity.ok(categoryService.updateCategory(categoryRequestDTO, id));

        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * DELETE /api/categories/{id}
     * Delete category (only if no expenses use it)
     * 
     * This endpoint handles category deletion. The deletion logic checks for expenses
     * in CategoryService (not ExpenseService) because:
     * 1. Category deletion is a category operation (Single Responsibility Principle)
     * 2. We prevent deletion if expenses exist (preserve data integrity)
     * 3. Expenses are financial records that should never be deleted automatically
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable UUID id) {
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok("Category deleted successfully");
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}



