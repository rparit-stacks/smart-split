package com.rps.smartsplit.service;

import com.rps.smartsplit.dto.request.CategoryRequestDTO;
import com.rps.smartsplit.dto.response.CategoryResponseDTO;
import com.rps.smartsplit.dto.request.CategoryUpdateRequest;
import com.rps.smartsplit.model.Category;
import com.rps.smartsplit.repository.CategoryRepository;
import com.rps.smartsplit.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;

    public @Nullable List<CategoryResponseDTO> getAllCategories() {
        List<Category> allCategories = categoryRepository.findAll();
        List<CategoryResponseDTO> categoryResponseDTOList = new ArrayList<>();


        for (Category category : allCategories) {
            CategoryResponseDTO categoryResponseDTO = modelMapper.map(category, CategoryResponseDTO.class);
            categoryResponseDTOList.add(categoryResponseDTO);
        }

        return categoryResponseDTOList;
    }

    public @Nullable CategoryResponseDTO getCategoryById(UUID id) {
        if(categoryRepository.existsById(id)){
            return modelMapper.map(categoryRepository.findById(id).get(), CategoryResponseDTO.class);
        }
        else throw new RuntimeException("Category not found");
    }

    public @Nullable Category getACategoryById(UUID id) {
        if(categoryRepository.existsById(id)){
            return categoryRepository.findById(id).get();
        }
        else throw new RuntimeException("Category not found");
    }

    public @Nullable CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO) {
        Category category = new Category();
        category.setName(categoryRequestDTO.getName());
        category.setDescription(categoryRequestDTO.getDescription());
        category.setColor(categoryRequestDTO.getColor());
        category.setCreatedAt(java.time.Instant.now());

        return modelMapper.map(categoryRepository.save(category), CategoryResponseDTO.class);
    }


    public CategoryResponseDTO updateCategory(
            CategoryUpdateRequest categoryRequestDTO,
            UUID id
    ) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        UUID loggedInUserId = userService.getLoggedInUser().getId();

        if (categoryRequestDTO.getAdminId() == null
                || !categoryRequestDTO.getAdminId().equals(loggedInUserId)) {
            throw new RuntimeException("You are not authorized to update this category");
        }

        if (categoryRequestDTO.getName() != null)
            category.setName(categoryRequestDTO.getName());

        if (categoryRequestDTO.getDescription() != null)
            category.setDescription(categoryRequestDTO.getDescription());

        if (categoryRequestDTO.getColor() != null)
            category.setColor(categoryRequestDTO.getColor());

        if (categoryRequestDTO.getAdminId() != null)
            category.setAdminId(categoryRequestDTO.getAdminId());

        category.setUpdateAt(Instant.now());

        return modelMapper.map(
                categoryRepository.save(category),
                CategoryResponseDTO.class
        );
    }

  
    @Transactional
    public void deleteCategory(UUID id) {
        // Check if category exists
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));




        // If no expenses exist, safe to delete
        categoryRepository.delete(category);
    }

}
