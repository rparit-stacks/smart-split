package com.rps.smartsplit.controller;

import com.rps.smartsplit.dto.balance.BalanceBetweenUsersResponseDTO;
import com.rps.smartsplit.dto.balance.BalanceMatrixResponseDTO;
import com.rps.smartsplit.dto.balance.GroupBalancesResponseDTO;
import com.rps.smartsplit.dto.balance.SimplifiedBalancesResponseDTO;
import com.rps.smartsplit.dto.balance.UserBalanceSummaryDTO;
import com.rps.smartsplit.dto.balance.UserBalancesResponseDTO;
import com.rps.smartsplit.service.BalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/balances")
@RequiredArgsConstructor
public class BalanceController {

    private final BalanceService balanceService;

    /**
     * GET /api/balances/user/{userId}
     * Get user balances (all balances user has)
     * 
     * This API returns all balances for a specific user across all groups they're part of.
     * It includes:
     * - All individual balances (who owes whom, with amounts)
     * - Summary totals (total owed by user, total owed to user, net balance)
     * - Breakdown by group (showing balances per group)
     * 
     * Why this API?
     * - Users need to see their complete financial picture across all groups
     * - Helps track debts and credits in one place
     * - Useful for dashboard and balance overview pages
     * - Shows both individual transactions and aggregated totals
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<UserBalancesResponseDTO> getUserBalances(@PathVariable UUID userId) {
        try {
            return ResponseEntity.ok(balanceService.getUserBalances(userId));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * GET /api/balances/group/{groupId}
     * Get group balances (who owes whom in group)
     * 
     * This API returns all balances within a specific group, showing who owes whom and how much.
     * It includes:
     * - All individual balances (who owes whom, with amounts)
     * - Group summary (total owed, total owing, net balance for the group)
     * - Per-user summaries (each member's total owed, total owing, net balance)
     * 
     * Balances are calculated with settlements applied, so if someone paid, it's already reflected.
     * 
     * Why this API?
     * - Group members need to see who owes whom in their group
     * - Essential for settlement planning - shows what needs to be settled
     * - Group balance overview - see the financial state of the group
     * - Helps identify who should pay whom to settle all debts
     * - Useful for group detail pages and settlement screens
     * 
     * Example: In a group with 3 members:
     * - User A owes User B ₹500
     * - User C owes User A ₹300
     * - User B owes User C ₹200
     * 
     * This API shows all these balances, plus summaries for each user.
     */
    @GetMapping("/group/{groupId}")
    public ResponseEntity<GroupBalancesResponseDTO> getGroupBalances(@PathVariable UUID groupId) {
        try {
            return ResponseEntity.ok(balanceService.getGroupBalances(groupId));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * GET /api/balances/group/{groupId}/simplified
     * Get simplified balances (optimized settlement suggestions)
     * 
     * This API returns simplified balances with optimized settlement suggestions.
     * It uses a debt simplification algorithm to minimize the number of transactions
     * needed to settle all debts in the group.
     * 
     * What it does:
     * - Calculates net balance for each user (what they owe minus what they're owed)
     * - Identifies creditors (users who are owed money) and debtors (users who owe money)
     * - Matches them using a greedy algorithm to minimize transactions
     * - Provides settlement suggestions (who should pay whom and how much)
     * 
     * Why this API?
     * - Settlement optimization - Shows the minimum number of payments needed
     * - Better UX - Users see clear settlement paths instead of complex debt webs
     * - Reduces confusion - Instead of A→B→C→A, shows direct A→C
     * - Settlement planning - Helps users plan how to settle debts efficiently
     * - Useful for "Settle Up" features - Shows optimal settlement path
     * 
     * 
     * DETAILED EXAMPLE:
     * 
     * Scenario: 4 friends went on a trip together
     * 
     * Original Expenses & Balances:
     * 1. Rahul paid ₹2000 for hotel (split equally: ₹500 each)
     *    → Priya owes Rahul ₹500
     *    → Amit owes Rahul ₹500
     *    → Sita owes Rahul ₹500
     * 
     * 2. Priya paid ₹1200 for food (split equally: ₹300 each)
     *    → Rahul owes Priya ₹300
     *    → Amit owes Priya ₹300
     *    → Sita owes Priya ₹300
     * 
     * 3. Amit paid ₹800 for taxi (split equally: ₹200 each)
     *    → Rahul owes Amit ₹200
     *    → Priya owes Amit ₹200
     *    → Sita owes Amit ₹200
     * 
     * 4. Sita paid ₹400 for snacks (split equally: ₹100 each)
     *    → Rahul owes Sita ₹100
     *    → Priya owes Sita ₹100
     *    → Amit owes Sita ₹100
     * 
     * Individual Balances (Before Simplification):
     * - Rahul ko dena hai: Priya ko ₹300, Amit ko ₹200, Sita ko ₹100 = Total: ₹600
     *   Rahul ko lena hai: Priya se ₹500, Amit se ₹500, Sita se ₹500 = Total: ₹1500
     *   Net Balance: +₹900 (Rahul ko ₹900 milna hai)
     * 
     * - Priya ko dena hai: Rahul ko ₹500, Amit ko ₹200, Sita ko ₹100 = Total: ₹800
     *   Priya ko lena hai: Rahul se ₹300, Amit se ₹300, Sita se ₹300 = Total: ₹900
     *   Net Balance: +₹100 (Priya ko ₹100 milna hai)
     * 
     * - Amit ko dena hai: Rahul ko ₹500, Priya ko ₹300, Sita ko ₹100 = Total: ₹900
     *   Amit ko lena hai: Rahul se ₹200, Priya se ₹200, Sita se ₹200 = Total: ₹600
     *   Net Balance: -₹300 (Amit ko ₹300 dena hai)
     * 
     * - Sita ko dena hai: Rahul ko ₹500, Priya ko ₹300, Amit ko ₹200 = Total: ₹1000
     *   Sita ko lena hai: Rahul se ₹100, Priya se ₹100, Amit se ₹100 = Total: ₹300
     *   Net Balance: -₹700 (Sita ko ₹700 dena hai)
     * 
     * SIMPLIFIED SETTLEMENT SUGGESTIONS (After Algorithm):
     * 12 individual transactions ki jagah, algorithm yeh suggest karta hai:
     * 
     * 1. Sita ko Rahul ko ₹700 dena chahiye
     *    (Yeh settle karta hai: Sita→Rahul ₹500, Sita→Priya ₹300, Sita→Amit ₹200,
     *     aur partially settle: Amit→Rahul ₹200, Priya→Rahul ₹300)
     * 
     * 2. Amit ko Rahul ko ₹100 dena chahiye
     *    (Yeh settle karta hai remaining: Amit→Rahul ₹300, Amit→Priya ₹300, Amit→Sita ₹200,
     *     aur partially settle: Priya→Rahul ₹200)
     * 
     * Result: Sirf 2 transactions instead of 12!
     * 
     * Final State:
     * - Rahul: ₹800 milega (Sita se ₹700 + Amit se ₹100) = Net +₹900 ✓
     * - Priya: ₹100 milega (indirectly) = Net +₹100 ✓
     * - Amit: ₹100 dega = Net -₹300 ✓
     * - Sita: ₹700 dega = Net -₹700 ✓
     * 
     * Sab debts minimum transactions mein settle ho gaye!
     */
    @GetMapping("/group/{groupId}/simplified")
    public ResponseEntity<SimplifiedBalancesResponseDTO> getSimplifiedBalances(@PathVariable UUID groupId) {
        try {
            return ResponseEntity.ok(balanceService.getSimplifiedBalances(groupId));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * GET /api/balances/user/{userId}/with/{otherUserId}
     * Get balance between two users
     * 
     * This API returns the balance between two specific users across all groups
     * where both users are members. It shows:
     * - All individual balances between the two users (from all common groups)
     * - Net balance (final amount one owes to the other)
     * - Direction (who owes whom)
     * 
     * Why this API?
     * - Direct balance check - See how much one user owes another
     * - Cross-group balance - Shows balance across all common groups
     * - Settlement planning - Know exactly how much to pay someone
     * - Personal finance - Track debts/credits with specific friends
     * - Useful for "Settle with friend" features
     * 
     * Example:
     * User A and User B are in 2 groups together:
     * - Group 1: A owes B ₹500
     * - Group 2: B owes A ₹200
     * 
     * Net balance: A owes B ₹300
     * Direction: "userId1_owes_userId2" (if userId1 is A and userId2 is B)
     */
    @GetMapping("/user/{userId}/with/{otherUserId}")
    public ResponseEntity<BalanceBetweenUsersResponseDTO> getBalanceBetweenUsers(
            @PathVariable UUID userId,
            @PathVariable UUID otherUserId) {
        try {
            return ResponseEntity.ok(balanceService.getBalanceBetweenUsers(userId, otherUserId));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * GET /api/balances/group/{groupId}/matrix
     * Get balance matrix (visual balance representation)
     * 
     * This API returns a 2D matrix showing balances between all users in a group.
     * Matrix structure:
     * - Rows represent "from" users (who owes)
     * - Columns represent "to" users (who is owed)
     * - Each cell [i][j] shows amount from user i to user j
     * 
     * Why this API?
     * - Visual representation - Easy to see all balances in a table/grid format
     * - Frontend display - Perfect for rendering balance tables
     * - Quick overview - See all debts at a glance
     * - Matrix visualization - Useful for charts and graphs
     * - Data analysis - Easy to process for visualizations
     * 
     * Example Matrix (3 users: A, B, C):
     *        A    B    C
     *    A [ 0  ₹500  ₹200]  (A owes B ₹500, A owes C ₹200)
     *    B [₹300   0  ₹100]  (B owes A ₹300, B owes C ₹100)
     *    C [₹150 ₹250    0]  (C owes A ₹150, C owes B ₹250)
     * 
     * Diagonal is always zero (user can't owe themselves).
     */
    @GetMapping("/group/{groupId}/matrix")
    public ResponseEntity<BalanceMatrixResponseDTO> getBalanceMatrix(@PathVariable UUID groupId) {
        try {
            return ResponseEntity.ok(balanceService.getBalanceMatrix(groupId));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * POST /api/balances/group/{groupId}/simplify
     * Simplify group debts (optimize settlements)
     * 
     * This API performs debt simplification and returns optimized settlement suggestions.
     * It's similar to GET /simplified but uses POST to indicate an "action" of simplification.
     * 
     * What it does:
     * - Calculates net balance for each user
     * - Identifies creditors and debtors
     * - Matches them using greedy algorithm to minimize transactions
     * - Returns settlement suggestions
     * 
     * Why POST instead of GET?
     * - POST indicates an action/operation (simplifying debts)
     * - Can be extended in future to actually create settlements
     * - Semantic difference: GET is for viewing, POST is for processing
     * 
     * Returns the same structure as GET /simplified endpoint.
     */
    @PostMapping("/group/{groupId}/simplify")
    public ResponseEntity<SimplifiedBalancesResponseDTO> simplifyGroupDebts(@PathVariable UUID groupId) {
        try {
            return ResponseEntity.ok(balanceService.getSimplifiedBalances(groupId));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * GET /api/balances/user/{userId}/summary
     * Get balance summary (total owe, owed, net)
     * 
     * This API returns a summary of user's balances across all groups.
     * It's a simplified version that only shows totals, not individual balances.
     * 
     * Response includes:
     * - Total owed (how much user owes to others across all groups)
     * - Total owed to user (how much others owe to user across all groups)
     * - Net balance (difference - positive means user is owed, negative means user owes)
     * 
     * Why this API?
     * - Quick overview - See totals without loading all individual balances
     * - Dashboard display - Perfect for showing summary cards
     * - Lightweight - Faster than full balance details
     * - Simple totals - Just need to know overall financial position
     * - Useful for "My Balance" widgets
     * 
     * Example:
     * User is in 3 groups:
     * - Group 1: User owes ₹500, is owed ₹300
     * - Group 2: User owes ₹200, is owed ₹100
     * - Group 3: User owes ₹0, is owed ₹400
     * 
     * Summary:
     * - Total owed: ₹700 (500 + 200 + 0)
     * - Total owed to user: ₹800 (300 + 100 + 400)
     * - Net balance: +₹100 (user should receive ₹100 overall)
     */
    @GetMapping("/user/{userId}/summary")
    public ResponseEntity<UserBalanceSummaryDTO> getBalanceSummary(@PathVariable UUID userId) {
        try {
            return ResponseEntity.ok(balanceService.getUserBalanceSummary(userId));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}

