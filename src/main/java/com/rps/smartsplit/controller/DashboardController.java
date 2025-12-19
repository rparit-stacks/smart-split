package com.rps.smartsplit.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    /**
     * GET /api/dashboard
     * Get user dashboard data (all data in one call)
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getDashboard() {
        // TODO: Implement dashboard data aggregation
        return null;
    }

    /**
     * GET /api/dashboard/balances
     * Get balance summary (total owe, total owed, by group, net balance)
     */
    @GetMapping("/balances")
    public ResponseEntity<Map<String, Object>> getBalanceSummary() {
        // TODO: Implement balance summary
        return null;
    }

    /**
     * GET /api/dashboard/recent-activity
     * Get recent activity (last 10-20 expenses and settlements)
     */
    @GetMapping("/recent-activity")
    public ResponseEntity<Map<String, Object>> getRecentActivity() {
        // TODO: Implement recent activity
        return null;
    }

    /**
     * GET /api/dashboard/statistics
     * Get user statistics (total spent, categories breakdown, expense count)
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        // TODO: Implement statistics
        return null;
    }

    /**
     * GET /api/dashboard/groups-summary
     * Get groups summary (list of groups with member count, recent activity)
     */
    @GetMapping("/groups-summary")
    public ResponseEntity<Map<String, Object>> getGroupsSummary() {
        // TODO: Implement groups summary
        return null;
    }

    /**
     * GET /api/dashboard/upcoming-settlements
     * Get upcoming settlements (debts that need to be settled)
     */
    @GetMapping("/upcoming-settlements")
    public ResponseEntity<Map<String, Object>> getUpcomingSettlements() {
        // TODO: Implement upcoming settlements
        return null;
    }

    /**
     * GET /api/dashboard/quick-stats
     * Get quick statistics (total expenses, total groups, active balances)
     */
    @GetMapping("/quick-stats")
    public ResponseEntity<Map<String, Object>> getQuickStats() {
        // TODO: Implement quick stats
        return null;
    }
}

