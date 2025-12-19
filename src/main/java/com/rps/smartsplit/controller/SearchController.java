package com.rps.smartsplit.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    /**
     * GET /api/search
     * Global search (search across all entities)
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> globalSearch(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        // TODO: Implement global search
        return null;
    }

    /**
     * GET /api/search/users
     * Search users by name/email
     */
    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> searchUsers(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        // TODO: Implement user search
        return null;
    }

    /**
     * GET /api/search/groups
     * Search groups by name
     */
    @GetMapping("/groups")
    public ResponseEntity<Map<String, Object>> searchGroups(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        // TODO: Implement group search
        return null;
    }

    /**
     * GET /api/search/expenses
     * Search expenses by title/description
     */
    @GetMapping("/expenses")
    public ResponseEntity<Map<String, Object>> searchExpenses(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        // TODO: Implement expense search
        return null;
    }

    /**
     * GET /api/search/settlements
     * Search settlements
     */
    @GetMapping("/settlements")
    public ResponseEntity<Map<String, Object>> searchSettlements(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        // TODO: Implement settlement search
        return null;
    }

    /**
     * GET /api/search/suggestions
     * Get search suggestions (autocomplete)
     */
    @GetMapping("/suggestions")
    public ResponseEntity<Map<String, Object>> getSearchSuggestions(@RequestParam String query) {
        // TODO: Implement search suggestions
        return null;
    }
}

