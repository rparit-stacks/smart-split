package com.rps.smartsplit.controller;

import com.rps.smartsplit.dto.request.SettlementRequestDTO;
import com.rps.smartsplit.dto.response.SettlementResponseDTO;
import com.rps.smartsplit.dto.request.PaymentRequestDTO;
import com.rps.smartsplit.dto.response.PaymentResponseDTO;
import com.rps.smartsplit.service.SettlementService;
import com.rps.smartsplit.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/settlements")
@RequiredArgsConstructor
public class SettlementController {

    private final SettlementService settlementService;
    private final PaymentService paymentService;

    /**
     * POST /api/settlements
     * Record a settlement/payment (updates balances between two users)
     */
    @PostMapping
    public ResponseEntity<SettlementResponseDTO> createSettlement(@RequestBody SettlementRequestDTO settlementRequestDTO) {
        try {
            return ResponseEntity.ok(settlementService.createSettlement(settlementRequestDTO));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * GET /api/settlements/{id}
     * Get settlement details
     */
    @GetMapping("/{id}")
    public ResponseEntity<SettlementResponseDTO> getSettlement(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(settlementService.settlementToSettlementDto(settlementService.getSettlementById(id)));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * DELETE /api/settlements/{id}
     * Delete/undo settlement (revert balance changes)
     * 
     * Note: This should ideally be admin-only to prevent unauthorized deletion
     * of financial records. Used for removing duplicates or incorrect settlements.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSettlement(@PathVariable UUID id) {
        try {
            settlementService.deleteSettlement(id);
            return ResponseEntity.ok("Settlement deleted successfully");
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * GET /api/settlements
     * List settlements with filters (group, user, date range)
     */
    @GetMapping
    public ResponseEntity<List<SettlementResponseDTO>> getSettlements(
            @RequestParam(required = false) UUID groupId,
            @RequestParam(required = false) UUID userId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        try {
            return ResponseEntity.ok(settlementService.getSettlements(groupId, userId, startDate, endDate));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * GET /api/settlements/group/{groupId}
     * Get settlements by group
     */
    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<SettlementResponseDTO>> getSettlementsByGroup(
            @PathVariable UUID groupId) {
        try {
            return ResponseEntity.ok(settlementService.getSettlementsByGroup(groupId));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * GET /api/settlements/user/{userId}
     * Get settlements by user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SettlementResponseDTO>> getSettlementsByUser(
            @PathVariable UUID userId) {
        try {
            return ResponseEntity.ok(settlementService.getSettlementsByUser(userId));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * POST /api/settlements/bulk
     * Record multiple settlements (batch payment recording)
     */
    @PostMapping("/bulk")
    public ResponseEntity<List<SettlementResponseDTO>> createBulkSettlements(
            @RequestBody List<SettlementRequestDTO> settlementRequestDTOs) {
        try {
            return ResponseEntity.ok(settlementService.createBulkSettlements(settlementRequestDTOs));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * POST /api/settlements/{id}/payments
     * Record payment method for settlement
     */
    @PostMapping("/{id}/payments")
    public ResponseEntity<PaymentResponseDTO> recordPayment(
            @PathVariable UUID id,
            @RequestBody PaymentRequestDTO paymentRequestDTO) {
        try {
            return ResponseEntity.ok(paymentService.createPayment(id, paymentRequestDTO));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * GET /api/settlements/{id}/payments
     * Get payment details for settlement
     */
    @GetMapping("/{id}/payments")
    public ResponseEntity<List<PaymentResponseDTO>> getSettlementPayments(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(paymentService.getPaymentsBySettlementId(id));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}

