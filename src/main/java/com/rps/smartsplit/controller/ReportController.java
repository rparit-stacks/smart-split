package com.rps.smartsplit.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    /**
     * GET /api/reports/expense-summary
     * Get expense summary report
     */
    @GetMapping("/expense-summary")
    public ResponseEntity<Map<String, Object>> getExpenseSummaryReport(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        // TODO: Implement expense summary report
        return null;
    }

    /**
     * GET /api/reports/group/{groupId}/report
     * Get group report (complete group financial report)
     */
    @GetMapping("/group/{groupId}/report")
    public ResponseEntity<Map<String, Object>> getGroupReport(
            @PathVariable UUID groupId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        // TODO: Implement group report
        return null;
    }

    /**
     * GET /api/reports/user/{userId}/report
     * Get user report (personal financial report)
     */
    @GetMapping("/user/{userId}/report")
    public ResponseEntity<Map<String, Object>> getUserReport(
            @PathVariable UUID userId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        // TODO: Implement user report
        return null;
    }

    /**
     * GET /api/reports/monthly/{year}/{month}
     * Get monthly report
     */
    @GetMapping("/monthly/{year}/{month}")
    public ResponseEntity<Map<String, Object>> getMonthlyReport(
            @PathVariable Integer year,
            @PathVariable Integer month) {
        // TODO: Implement monthly report
        return null;
    }

    /**
     * GET /api/reports/yearly/{year}
     * Get yearly report
     */
    @GetMapping("/yearly/{year}")
    public ResponseEntity<Map<String, Object>> getYearlyReport(@PathVariable Integer year) {
        // TODO: Implement yearly report
        return null;
    }

    /**
     * GET /api/reports/category-breakdown
     * Get category breakdown (spending by category)
     */
    @GetMapping("/category-breakdown")
    public ResponseEntity<Map<String, Object>> getCategoryBreakdown(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        // TODO: Implement category breakdown
        return null;
    }

    /**
     * GET /api/reports/tax-report
     * Get tax report (tax-deductible expenses)
     */
    @GetMapping("/tax-report")
    public ResponseEntity<Map<String, Object>> getTaxReport(
            @RequestParam Integer year) {
        // TODO: Implement tax report
        return null;
    }

    /**
     * GET /api/reports/settlement-history
     * Get settlement history (all settlements)
     */
    @GetMapping("/settlement-history")
    public ResponseEntity<Map<String, Object>> getSettlementHistory(
            @RequestParam(required = false) UUID groupId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        // TODO: Implement settlement history
        return null;
    }
}

