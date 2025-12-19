package com.rps.smartsplit.controller;

import com.rps.smartsplit.dto.ExpenseRequestDTO;
import com.rps.smartsplit.dto.ExpenseResponseDTO;
import com.rps.smartsplit.dto.ExpenseParticipantRequestDTO;
import com.rps.smartsplit.dto.ExpenseParticipantResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    /**
     * POST /api/expenses
     * Create new expense with split logic
     */
    @PostMapping
    public ResponseEntity<ExpenseResponseDTO> createExpense(@RequestBody ExpenseRequestDTO expenseRequestDTO) {
        // TODO: Implement expense creation with split logic
        return null;
    }

    /**
     * GET /api/expenses/{id}
     * Get expense details with all participants
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponseDTO> getExpense(@PathVariable UUID id) {
        // TODO: Implement get expense by ID
        return null;
    }

    /**
     * PUT /api/expenses/{id}
     * Update expense (recalculate balances if amount/split changes)
     */
    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponseDTO> updateExpense(
            @PathVariable UUID id,
            @RequestBody ExpenseRequestDTO expenseRequestDTO) {
        // TODO: Implement expense update with balance recalculation
        return null;
    }

    /**
     * DELETE /api/expenses/{id}
     * Delete expense (recalculate all affected balances)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteExpense(@PathVariable UUID id) {
        // TODO: Implement expense deletion with balance recalculation
        return null;
    }

    /**
     * GET /api/expenses
     * List expenses with filters (group, user, category, date range, pagination)
     */
    @GetMapping
    public ResponseEntity<List<ExpenseResponseDTO>> getExpenses(
            @RequestParam(required = false) UUID groupId,
            @RequestParam(required = false) UUID userId,
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        // TODO: Implement filtered expense list with pagination
        return null;
    }

    /**
     * GET /api/expenses/recent
     * Get recent expenses (last 10-20 across all groups)
     */
    @GetMapping("/recent")
    public ResponseEntity<List<ExpenseResponseDTO>> getRecentExpenses() {
        // TODO: Implement recent expenses
        return null;
    }

    /**
     * GET /api/expenses/group/{groupId}
     * Get expenses by group (paginated)
     */
    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<ExpenseResponseDTO>> getExpensesByGroup(
            @PathVariable UUID groupId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        // TODO: Implement group expenses
        return null;
    }

    /**
     * GET /api/expenses/user/{userId}
     * Get expenses by user (all expenses user paid or participated in)
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ExpenseResponseDTO>> getExpensesByUser(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        // TODO: Implement user expenses
        return null;
    }

    /**
     * GET /api/expenses/category/{categoryId}
     * Get expenses by category
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ExpenseResponseDTO>> getExpensesByCategory(
            @PathVariable UUID categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        // TODO: Implement category expenses
        return null;
    }

    /**
     * GET /api/expenses/date-range
     * Get expenses by date range
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<ExpenseResponseDTO>> getExpensesByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        // TODO: Implement date range expenses
        return null;
    }

    /**
     * PATCH /api/expenses/{id}/mark-paid
     * Mark expense as paid (update participant payment status)
     */
    @PatchMapping("/{id}/mark-paid")
    public ResponseEntity<ExpenseResponseDTO> markExpenseAsPaid(@PathVariable UUID id) {
        // TODO: Implement mark expense as paid
        return null;
    }

    /**
     * GET /api/expenses/{id}/participants
     * Get all participants for expense
     */
    @GetMapping("/{id}/participants")
    public ResponseEntity<List<ExpenseParticipantResponseDTO>> getExpenseParticipants(@PathVariable UUID id) {
        // TODO: Implement get expense participants
        return null;
    }

    /**
     * PATCH /api/expenses/{id}/participants/{participantId}
     * Update participant status (mark paid/unpaid)
     */
    @PatchMapping("/{id}/participants/{participantId}")
    public ResponseEntity<ExpenseParticipantResponseDTO> updateParticipantStatus(
            @PathVariable UUID id,
            @PathVariable UUID participantId,
            @RequestBody ExpenseParticipantRequestDTO participantRequestDTO) {
        // TODO: Implement update participant status
        return null;
    }

    /**
     * PUT /api/expenses/{id}/participants/{participantId}/amount
     * Update participant share amount (recalculate balances)
     */
    @PutMapping("/{id}/participants/{participantId}/amount")
    public ResponseEntity<ExpenseParticipantResponseDTO> updateParticipantAmount(
            @PathVariable UUID id,
            @PathVariable UUID participantId,
            @RequestBody ExpenseParticipantRequestDTO participantRequestDTO) {
        // TODO: Implement update participant amount
        return null;
    }

    /**
     * DELETE /api/expenses/{id}/participants/{participantId}
     * Remove participant from expense (recalculate remaining shares)
     */
    @DeleteMapping("/{id}/participants/{participantId}")
    public ResponseEntity<String> removeParticipant(
            @PathVariable UUID id,
            @PathVariable UUID participantId) {
        // TODO: Implement remove participant
        return null;
    }
}

