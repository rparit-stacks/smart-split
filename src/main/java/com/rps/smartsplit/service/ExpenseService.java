package com.rps.smartsplit.service;

import com.rps.smartsplit.dto.ExpenseResponseDTO;
import com.rps.smartsplit.model.Expense;
import com.rps.smartsplit.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    // Method to convert Expense entity to ExpenseResponseDTO
    public ExpenseResponseDTO expenseToExpenseDto(Expense expense) {
        if (expense == null) {
            return null;
        }
        ExpenseResponseDTO dto = new ExpenseResponseDTO();
        dto.setId(expense.getId());
        dto.setGroupId(expense.getGroup() != null ? expense.getGroup().getId() : null);
        dto.setPaidById(expense.getPaidBy() != null ? expense.getPaidBy().getId() : null);
        dto.setCategoryId(expense.getCategory() != null ? expense.getCategory().getId() : null);
        dto.setExpenseDate(expense.getExpenseDate());
        dto.setTitle(expense.getTitle());
        dto.setDescription(expense.getDescription());
        dto.setAmount(expense.getAmount());
        return dto;
    }

    /**
     * Get all expenses by group ID
     * Following SRP - ExpenseService handles expense-related queries
     */
    public List<Expense> getExpensesByGroupId(UUID groupId) {
        List<Expense> allExpenses = expenseRepository.findAll();
        List<Expense> groupExpenses = new ArrayList<>();
        
        for (Expense expense : allExpenses) {
            if (expense.getGroup() != null && expense.getGroup().getId().equals(groupId)) {
                groupExpenses.add(expense);
            }
        }
        
        return groupExpenses;
    }

    /**
     * Get expense by ID with participants loaded
     */
    public Expense getExpenseById(UUID expenseId) {
        return expenseRepository.findById(expenseId)
            .orElseThrow(() -> new IllegalArgumentException("Expense not found"));
    }

}
