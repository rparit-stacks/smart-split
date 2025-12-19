package com.rps.smartsplit.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/balances")
public class BalanceController {

    /**
     * GET /api/balances/user/{userId}
     * Get user balances (all balances user has)
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getUserBalances(@PathVariable UUID userId) {
        // TODO: Implement get user balances
        return null;
    }

    /**
     * GET /api/balances/group/{groupId}
     * Get group balances (who owes whom in group)
     */
    @GetMapping("/group/{groupId}")
    public ResponseEntity<Map<String, Object>> getGroupBalances(@PathVariable UUID groupId) {
        // TODO: Implement get group balances
        return null;
    }

    /**
     * GET /api/balances/group/{groupId}/simplified
     * Get simplified balances (optimized settlement suggestions)
     */
    @GetMapping("/group/{groupId}/simplified")
    public ResponseEntity<Map<String, Object>> getSimplifiedBalances(@PathVariable UUID groupId) {
        // TODO: Implement simplified balances (debt simplification algorithm)
        return null;
    }

    /**
     * GET /api/balances/user/{userId}/with/{otherUserId}
     * Get balance between two users
     */
    @GetMapping("/user/{userId}/with/{otherUserId}")
    public ResponseEntity<Map<String, Object>> getBalanceBetweenUsers(
            @PathVariable UUID userId,
            @PathVariable UUID otherUserId) {
        // TODO: Implement balance between two users
        return null;
    }

    /**
     * GET /api/balances/group/{groupId}/matrix
     * Get balance matrix (visual balance representation)
     */
    @GetMapping("/group/{groupId}/matrix")
    public ResponseEntity<Map<String, Object>> getBalanceMatrix(@PathVariable UUID groupId) {
        // TODO: Implement balance matrix
        return null;
    }

    /**
     * POST /api/balances/group/{groupId}/simplify
     * Simplify group debts (optimize settlements)
     */
    @PostMapping("/group/{groupId}/simplify")
    public ResponseEntity<Map<String, Object>> simplifyGroupDebts(@PathVariable UUID groupId) {
        // TODO: Implement debt simplification algorithm
        return null;
    }

    /**
     * GET /api/balances/user/{userId}/summary
     * Get balance summary (total owe, owed, net)
     */
    @GetMapping("/user/{userId}/summary")
    public ResponseEntity<Map<String, Object>> getBalanceSummary(@PathVariable UUID userId) {
        // TODO: Implement balance summary
        return null;
    }
}

