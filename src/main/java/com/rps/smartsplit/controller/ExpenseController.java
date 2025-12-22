package com.rps.smartsplit.controller;

import com.rps.smartsplit.dto.request.ExpenseRequestDTO;
import com.rps.smartsplit.dto.response.ExpenseResponseDTO;
import com.rps.smartsplit.dto.request.ExpenseParticipantRequestDTO;
import com.rps.smartsplit.dto.response.ExpenseParticipantResponseDTO;
import com.rps.smartsplit.service.ExpenseService;
import com.rps.smartsplit.service.ExpenseParticipantService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;
    private final ExpenseParticipantService expenseParticipantService;
    private final ModelMapper modelMapper;
    /**
     * POST /api/expenses
     * Create new expense with split logic
     */
    @PostMapping
    public ResponseEntity<ExpenseResponseDTO> createExpense(@RequestBody ExpenseRequestDTO expenseRequestDTO) {
        try{
            return ResponseEntity.ok(expenseService.createExpense(expenseRequestDTO));
        }catch (RuntimeException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * GET /api/expenses/{id}
     * Get expense details with all participants
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponseDTO> getExpense(@PathVariable UUID id) {
        try{
            return ResponseEntity.ok(modelMapper.map(expenseService.getExpenseById(id), ExpenseResponseDTO.class));
        }catch (RuntimeException e){
            throw new RuntimeException(e);
        }

    }

    /**
     * PUT /api/expenses/{id}
     * Update expense (recalculate balances if amount/split changes)
     */
    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponseDTO> updateExpense(
            @PathVariable UUID id,
            @RequestBody ExpenseRequestDTO expenseRequestDTO) {
        try {
            return ResponseEntity.ok(expenseService.updateExpense(id, expenseRequestDTO));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * DELETE /api/expenses/{id}
     * Delete expense (recalculate all affected balances)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteExpense(@PathVariable UUID id) {
        try {
            expenseService.deleteExpense(id);
            return ResponseEntity.ok("Expense deleted successfully");
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * GET /api/expenses
     * List expenses with filters (group, user, category, date range)
     */
    @GetMapping
    public ResponseEntity<List<ExpenseResponseDTO>> getExpenses(
            @RequestParam(required = false) UUID groupId,
            @RequestParam(required = false) UUID userId,
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        try {
            return ResponseEntity.ok(expenseService.getExpenses(groupId, userId, categoryId, startDate, endDate));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * GET /api/expenses/recent
     * Get recent expenses (last 20 across all groups)
     */
    @GetMapping("/recent")
    public ResponseEntity<List<ExpenseResponseDTO>> getRecentExpenses() {
        try {
            return ResponseEntity.ok(expenseService.getRecentExpenses());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * GET /api/expenses/group/{groupId}
     * Get expenses by group
     */
    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<ExpenseResponseDTO>> getExpensesByGroup(
            @PathVariable UUID groupId) {
        try {
            return ResponseEntity.ok(expenseService.getExpensesByGroup(groupId));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * GET /api/expenses/user/{userId}
     * Get expenses by user (all expenses user paid or participated in)
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ExpenseResponseDTO>> getExpensesByUser(
            @PathVariable UUID userId) {
        try {
            return ResponseEntity.ok(expenseService.getExpensesByUser(userId));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * GET /api/expenses/category/{categoryId}
     * Get expenses by category
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ExpenseResponseDTO>> getExpensesByCategory(
            @PathVariable UUID categoryId) {
        try {
            return ResponseEntity.ok(expenseService.getExpensesByCategory(categoryId));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * GET /api/expenses/date-range
     * Get expenses by date range
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<ExpenseResponseDTO>> getExpensesByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            return ResponseEntity.ok(expenseService.getExpensesByDateRange(startDate, endDate));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * PATCH /api/expenses/{id}/mark-paid
     * Mark expense as paid (update participant payment status)
     */
    @PatchMapping("/{id}/mark-paid")
    public ResponseEntity<ExpenseResponseDTO> markExpenseAsPaid(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(expenseService.markExpenseAsPaid(id));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * GET /api/expenses/{id}/participants
     * Get all participants for expense
     */
    @GetMapping("/{id}/participants")
    public ResponseEntity<List<ExpenseParticipantResponseDTO>> getExpenseParticipants(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(expenseParticipantService.getParticipantsByExpenseId(id));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
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
        try {
            return ResponseEntity.ok(expenseParticipantService.updateParticipantStatus(participantId, participantRequestDTO.isPaid()));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
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
        try {
            return ResponseEntity.ok(expenseParticipantService.updateParticipantAmount(participantId, participantRequestDTO.getAmount()));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * DELETE /api/expenses/{id}/participants/{participantId}
     * Remove participant from expense (recalculate remaining shares)
     */
    @DeleteMapping("/{id}/participants/{participantId}")
    public ResponseEntity<String> removeParticipant(
            @PathVariable UUID id,
            @PathVariable UUID participantId) {
        try {
            expenseParticipantService.removeParticipant(participantId);
            return ResponseEntity.ok("Participant removed successfully");
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}

