package com.rps.smartsplit.controller;

import com.rps.smartsplit.dto.search.GlobalSearchResponseDTO;
import com.rps.smartsplit.dto.search.SearchSuggestionsResponseDTO;
import com.rps.smartsplit.dto.response.UserResponseDTO;
import com.rps.smartsplit.dto.response.GroupResponseDTO;
import com.rps.smartsplit.dto.response.ExpenseResponseDTO;
import com.rps.smartsplit.dto.response.SettlementResponseDTO;
import com.rps.smartsplit.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    /**
     * GET /api/search
     * Global search (search across all entities)
     */
    @GetMapping
    public ResponseEntity<GlobalSearchResponseDTO> globalSearch(@RequestParam String query) {
        try {
            return ResponseEntity.ok(searchService.globalSearch(query));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * GET /api/search/users
     * Search users by name/email
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDTO>> searchUsers(@RequestParam String query) {
        try {
            return ResponseEntity.ok(searchService.searchUsers(query));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * GET /api/search/groups
     * Search groups by name
     */
    @GetMapping("/groups")
    public ResponseEntity<List<GroupResponseDTO>> searchGroups(@RequestParam String query) {
        try {
            return ResponseEntity.ok(searchService.searchGroups(query));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * GET /api/search/expenses
     * Search expenses by title/description
     */
    @GetMapping("/expenses")
    public ResponseEntity<List<ExpenseResponseDTO>> searchExpenses(@RequestParam String query) {
        try {
            return ResponseEntity.ok(searchService.searchExpenses(query));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * GET /api/search/settlements
     * Search settlements
     */
    @GetMapping("/settlements")
    public ResponseEntity<List<SettlementResponseDTO>> searchSettlements(@RequestParam String query) {
        try {
            return ResponseEntity.ok(searchService.searchSettlements(query));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * GET /api/search/suggestions
     * Get search suggestions (autocomplete)
     */
    @GetMapping("/suggestions")
    public ResponseEntity<SearchSuggestionsResponseDTO> getSearchSuggestions(@RequestParam String query) {
        try {
            return ResponseEntity.ok(searchService.getSearchSuggestions(query));
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}

