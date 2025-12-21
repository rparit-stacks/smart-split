package com.rps.smartsplit.repository;

import com.rps.smartsplit.model.Category;
import com.rps.smartsplit.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, UUID> {
    /**
     * Check if any expenses exist for the given category
     * @param category The category to check
     * @return true if expenses exist, false otherwise
     */
    boolean existsByCategory(Category category);
    
    /**
     * Count expenses for a given category
     * @param category The category to count expenses for
     * @return number of expenses using this category
     */
    long countByCategory(Category category);
}

