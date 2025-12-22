package com.rps.smartsplit.controller;

import com.rps.smartsplit.dto.balance.BalanceSummaryDTO;
import com.rps.smartsplit.dto.dashboard.RecentActivityDTO;
import com.rps.smartsplit.dto.dashboard.StatisticsDTO;
import com.rps.smartsplit.dto.dashboard.QuickStatsDTO;
import com.rps.smartsplit.dto.common.GroupSummaryDTO;
import com.rps.smartsplit.dto.balance.BalanceDTO;
import com.rps.smartsplit.dto.dashboard.DashboardResponseDTO;
import com.rps.smartsplit.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * GET /api/dashboard
     * Get user dashboard data (all data in one call)
     */
    @GetMapping
    public ResponseEntity<DashboardResponseDTO> getDashboard() {
        try {
            return ResponseEntity.ok(dashboardService.getDashboard());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * GET /api/dashboard/balances
     * Get balance summary (total owe, total owed, by group, net balance)
     */
    @GetMapping("/balances")
    public ResponseEntity<BalanceSummaryDTO> getBalanceSummary() {
        try {
            return ResponseEntity.ok(dashboardService.getBalanceSummary());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * GET /api/dashboard/recent-activity
     * Get recent activity (last 15 expenses and settlements)
     */
    @GetMapping("/recent-activity")
    public ResponseEntity<RecentActivityDTO> getRecentActivity() {
        try {
            return ResponseEntity.ok(dashboardService.getRecentActivity());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * GET /api/dashboard/statistics
     * Get user statistics (total spent, categories breakdown, expense count)
     */
    @GetMapping("/statistics")
    public ResponseEntity<StatisticsDTO> getStatistics() {
        try {
            return ResponseEntity.ok(dashboardService.getStatistics());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * GET /api/dashboard/groups-summary
     * Get groups summary (list of groups with member count, recent activity)
     */
    @GetMapping("/groups-summary")
    public ResponseEntity<List<GroupSummaryDTO>> getGroupsSummary() {
        try {
            return ResponseEntity.ok(dashboardService.getGroupsSummary());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * GET /api/dashboard/upcoming-settlements
     * Get upcoming settlements (debts that need to be settled)
     */
    @GetMapping("/upcoming-settlements")
    public ResponseEntity<List<BalanceDTO>> getUpcomingSettlements() {
        try {
            return ResponseEntity.ok(dashboardService.getUpcomingSettlements());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * GET /api/dashboard/quick-stats
     * Get quick statistics (total expenses, total groups, active balances)
     */
    @GetMapping("/quick-stats")
    public ResponseEntity<QuickStatsDTO> getQuickStats() {
        try {
            return ResponseEntity.ok(dashboardService.getQuickStats());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}

