package com.rps.smartsplit.service;

import com.rps.smartsplit.dto.request.ExpenseRequestDTO;
import com.rps.smartsplit.dto.response.ExpenseResponseDTO;
import com.rps.smartsplit.model.*;
import com.rps.smartsplit.repository.ExpenseRepository;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;
    @Autowired
    @Lazy
    private GroupService groupService;
    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ExpenseParticipantService expenseParticipantService;



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



    @Transactional
    public @Nullable ExpenseResponseDTO createExpense(ExpenseRequestDTO expenseRequestDTO) {
        if (expenseRequestDTO == null) {
            throw new IllegalArgumentException("Expense request must not be null");
        }

        // Build Expense entity from request
        Expense expense = new Expense();
        expense.setGroup(groupService.getAGroupById(expenseRequestDTO.getGroupId()));
        expense.setPaidBy(userService.getUserByUserId(expenseRequestDTO.getPaidById()));
        expense.setCategory(categoryService.getACategoryById(expenseRequestDTO.getCategoryId()));
        expense.setExpenseDate(expenseRequestDTO.getExpenseDate());
        expense.setTitle(expenseRequestDTO.getTitle());
        expense.setDescription(expenseRequestDTO.getDescription());
        expense.setAmount(expenseRequestDTO.getAmount());
        expense.setSplitType(expenseRequestDTO.getSplitType());

        // Save expense first
        Expense savedExpense = expenseRepository.save(expense);

        // Save participants for this expense (handles EQUAL / EXACT / PERCENTAGE)
        expenseParticipantService.saveParticipantForExpense(
                expenseRequestDTO.getParticipants(),
                savedExpense
        );

        // Return DTO of saved expense
        return expenseToExpenseDto(savedExpense);
    }
    
    /**
     * Update expense and recalculate participants if needed
     * Following SRP - ExpenseService handles expense updates
     */
    @Transactional
    public ExpenseResponseDTO updateExpense(UUID expenseId, ExpenseRequestDTO expenseRequestDTO) {
        if (expenseRequestDTO == null) {
            throw new IllegalArgumentException("Expense request must not be null");
        }
        
        Expense existingExpense = getExpenseById(expenseId);
        
        // Update expense fields
        existingExpense.setGroup(groupService.getAGroupById(expenseRequestDTO.getGroupId()));
        existingExpense.setPaidBy(userService.getUserByUserId(expenseRequestDTO.getPaidById()));
        existingExpense.setCategory(categoryService.getACategoryById(expenseRequestDTO.getCategoryId()));
        existingExpense.setExpenseDate(expenseRequestDTO.getExpenseDate());
        existingExpense.setTitle(expenseRequestDTO.getTitle());
        existingExpense.setDescription(expenseRequestDTO.getDescription());
        existingExpense.setAmount(expenseRequestDTO.getAmount());
        existingExpense.setSplitType(expenseRequestDTO.getSplitType());

        // Delete existing participants
        expenseParticipantService.deleteParticipantsByExpenseId(expenseId);
        
        // Save updated expense
        Expense updatedExpense = expenseRepository.save(existingExpense);
        
        // Save new participants
        expenseParticipantService.saveParticipantForExpense(
                expenseRequestDTO.getParticipants(),
                updatedExpense
        );
        
        return expenseToExpenseDto(updatedExpense);
    }
    
    /**
     * Delete expense and all its participants
     * Following SRP - ExpenseService handles expense deletion
     */
    @Transactional
    public void deleteExpense(UUID expenseId) {
        Expense expense = getExpenseById(expenseId);
        
        // Delete all participants first
        expenseParticipantService.deleteParticipantsByExpenseId(expenseId);
        
        // Delete expense
        expenseRepository.delete(expense);
    }
    
    /**
     * Get all expenses with optional filters
     * Following SRP - ExpenseService handles expense queries
     */
    public List<ExpenseResponseDTO> getExpenses(UUID groupId, UUID userId, UUID categoryId, 
                                                 String startDate, String endDate) {
        List<Expense> allExpenses = expenseRepository.findAll();
        List<Expense> filteredExpenses = new ArrayList<>();

        for (Expense expense : allExpenses) {
            boolean matches = true;

            if (groupId != null && (expense.getGroup() == null || !expense.getGroup().getId().equals(groupId))) {
                matches = false;
            }

            if (userId != null) {
                // Check if user paid for this expense
                boolean isPaidBy = expense.getPaidBy() != null && expense.getPaidBy().getId().equals(userId);
                
                // Check if user is a participant
                boolean isParticipant = false;
                if (expense.getExpenseParticipants() != null) {
                    for (ExpenseParticipant participant : expense.getExpenseParticipants()) {
                        if (participant.getUser() != null && participant.getUser().getId().equals(userId)) {
                            isParticipant = true;
                            break;
                        }
                    }
                }
                
                // User must be either the payer or a participant
                if (!isPaidBy && !isParticipant) {
                    matches = false;
                }
        }
        
            if (categoryId != null && (expense.getCategory() == null || !expense.getCategory().getId().equals(categoryId))) {
                matches = false;
        }
        
            if (startDate != null && expense.getExpenseDate() != null) {
                Instant start = Instant.parse(startDate);
                if (expense.getExpenseDate().isBefore(start)) {
                    matches = false;
                }
            }

            if (endDate != null && expense.getExpenseDate() != null) {
                Instant end = Instant.parse(endDate);
                if (expense.getExpenseDate().isAfter(end)) {
                    matches = false;
                }
            }

            if (matches) {
                filteredExpenses.add(expense);
            }
        }

        return convertToDtoList(filteredExpenses);
    }
    
    /**
     * Get recent expenses (last 20)
     * Following SRP - ExpenseService handles expense queries
     */
    public List<ExpenseResponseDTO> getRecentExpenses() {
        List<Expense> allExpenses = expenseRepository.findAll();
        List<Expense> sortedExpenses = new ArrayList<>(allExpenses);
        
        // Sort by expense date descending (most recent first)
        sortedExpenses.sort((e1, e2) -> {
            if (e1.getExpenseDate() == null && e2.getExpenseDate() == null) return 0;
            if (e1.getExpenseDate() == null) return 1;
            if (e2.getExpenseDate() == null) return -1;
            return e2.getExpenseDate().compareTo(e1.getExpenseDate());
        });

        // Get first 20
        List<Expense> recentExpenses = new ArrayList<>();
        int count = Math.min(20, sortedExpenses.size());
        for (int i = 0; i < count; i++) {
            recentExpenses.add(sortedExpenses.get(i));
        }

        return convertToDtoList(recentExpenses);
    }

    /**
     * Get expenses by group
     * Following SRP - ExpenseService handles expense queries
     */
    public List<ExpenseResponseDTO> getExpensesByGroup(UUID groupId) {
        List<Expense> expenses = getExpensesByGroupId(groupId);
        return convertToDtoList(expenses);
    }
    
    /**
     * Get expenses by user (paid or participated)
     * Following SRP - ExpenseService handles expense queries
     */
    public List<ExpenseResponseDTO> getExpensesByUser(UUID userId) {
        List<Expense> allExpenses = expenseRepository.findAll();
        List<Expense> userExpenses = new ArrayList<>();

        for (Expense expense : allExpenses) {
            // Check if user paid for this expense
            if (expense.getPaidBy() != null && expense.getPaidBy().getId().equals(userId)) {
                userExpenses.add(expense);
                continue;
            }

            // Check if user is a participant
            if (expense.getExpenseParticipants() != null) {
                for (ExpenseParticipant participant : expense.getExpenseParticipants()) {
                    if (participant.getUser() != null && participant.getUser().getId().equals(userId)) {
                        userExpenses.add(expense);
                        break;
                    }
                }
            }
        }

        return convertToDtoList(userExpenses);
    }
    
    /**
     * Get expenses by category
     * Following SRP - ExpenseService handles expense queries
     */
    public List<ExpenseResponseDTO> getExpensesByCategory(UUID categoryId) {
        List<Expense> allExpenses = expenseRepository.findAll();
        List<Expense> categoryExpenses = new ArrayList<>();

        for (Expense expense : allExpenses) {
            if (expense.getCategory() != null && expense.getCategory().getId().equals(categoryId)) {
                categoryExpenses.add(expense);
            }
        }

        return convertToDtoList(categoryExpenses);
    }
    
    /**
     * Get expenses by date range
     * Following SRP - ExpenseService handles expense queries
     */
    public List<ExpenseResponseDTO> getExpensesByDateRange(String startDate, String endDate) {
        List<Expense> allExpenses = expenseRepository.findAll();
        List<Expense> filteredExpenses = new ArrayList<>();

        Instant start = startDate != null ? Instant.parse(startDate) : null;
        Instant end = endDate != null ? Instant.parse(endDate) : null;

        for (Expense expense : allExpenses) {
            if (expense.getExpenseDate() == null) {
                continue;
    }
    
            boolean matches = true;

            if (start != null && expense.getExpenseDate().isBefore(start)) {
                matches = false;
            }

            if (end != null && expense.getExpenseDate().isAfter(end)) {
                matches = false;
        }

            if (matches) {
                filteredExpenses.add(expense);
            }
        }

        return convertToDtoList(filteredExpenses);
    }

    /**
     * Mark expense as paid (mark all participants as paid)
     * Following SRP - ExpenseService handles expense status updates
     */
    @Transactional
    public ExpenseResponseDTO markExpenseAsPaid(UUID expenseId) {
        Expense expense = getExpenseById(expenseId);
        expenseParticipantService.markAllParticipantsAsPaid(expenseId);
        return expenseToExpenseDto(expense);
    }

    /**
     * Convert list of expenses to DTOs
     * Following SRP - ExpenseService handles DTO conversion
     */
    private List<ExpenseResponseDTO> convertToDtoList(List<Expense> expenses) {
        List<ExpenseResponseDTO> dtos = new ArrayList<>();
        for (Expense expense : expenses) {
            dtos.add(expenseToExpenseDto(expense));
            }
        return dtos;
    }
}
