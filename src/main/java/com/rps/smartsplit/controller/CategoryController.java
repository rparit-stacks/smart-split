package com.rps.smartsplit.controller;

import com.rps.smartsplit.dto.CategoryRequestDTO;
import com.rps.smartsplit.dto.CategoryResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    /**
     * GET /api/categories
     * List all categories (default + custom)
     */
    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        // TODO: Implement get all categories
        return null;
    }

    /**
     * GET /api/categories/{id}
     * Get category details
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategory(@PathVariable UUID id) {
        // TODO: Implement get category by ID
        return null;
    }

    /**
     * POST /api/categories
     * Create custom category
     */
    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(@RequestBody CategoryRequestDTO categoryRequestDTO) {
        // TODO: Implement create category
        return null;
    }

    /**
     * PUT /api/categories/{id}
     * Update category (only if user owns it or is admin)
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(
            @PathVariable UUID id,
            @RequestBody CategoryRequestDTO categoryRequestDTO) {
        // TODO: Implement update category
        return null;
    }

    /**
     * DELETE /api/categories/{id}
     * Delete category (only if no expenses use it)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable UUID id) {
        // TODO: Implement delete category (with validation)
        return null;
    }

    /**
     * GET /api/categories/popular
     * Get popular categories (most-used)
     */
    @GetMapping("/popular")
    public ResponseEntity<List<CategoryResponseDTO>> getPopularCategories() {
        // TODO: Implement get popular categories
        return null;
    }
}

