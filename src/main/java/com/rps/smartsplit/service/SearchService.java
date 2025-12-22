package com.rps.smartsplit.service;

import com.rps.smartsplit.dto.search.GlobalSearchResponseDTO;
import com.rps.smartsplit.dto.search.SearchSuggestionsResponseDTO;
import com.rps.smartsplit.dto.response.UserResponseDTO;
import com.rps.smartsplit.dto.response.GroupResponseDTO;
import com.rps.smartsplit.dto.response.ExpenseResponseDTO;
import com.rps.smartsplit.dto.response.SettlementResponseDTO;
import com.rps.smartsplit.service.UserService;
import com.rps.smartsplit.service.GroupService;
import com.rps.smartsplit.service.ExpenseService;
import com.rps.smartsplit.service.SettlementService;
import com.rps.smartsplit.model.*;
import com.rps.smartsplit.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private SettlementRepository settlementRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private SettlementService settlementService;

    /**
     * Global search across all entities
     * Following SRP - SearchService handles search operations
     */
    public GlobalSearchResponseDTO globalSearch(String query) {
        GlobalSearchResponseDTO result = new GlobalSearchResponseDTO();
        
        result.setUsers(searchUsers(query));
        result.setGroups(searchGroups(query));
        result.setExpenses(searchExpenses(query));
        result.setSettlements(searchSettlements(query));
        
        return result;
    }

    /**
     * Search users by name or email
     * Following SRP - SearchService handles user search
     */
    public List<UserResponseDTO> searchUsers(String query) {
        List<User> allUsers = userRepository.findAll();
        List<UserResponseDTO> matchingUsers = new ArrayList<>();
        String lowerQuery = query.toLowerCase();

        for (User user : allUsers) {
            boolean matches = false;

            if (user.getName() != null && user.getName().toLowerCase().contains(lowerQuery)) {
                matches = true;
            }

            if (user.getEmail() != null && user.getEmail().toLowerCase().contains(lowerQuery)) {
                matches = true;
            }

            if (matches) {
                matchingUsers.add(userService.UserToUserDto(user));
            }
        }

        return matchingUsers;
    }

    /**
     * Search groups by name or description
     * Following SRP - SearchService handles group search
     */
    public List<GroupResponseDTO> searchGroups(String query) {
        List<Group> allGroups = groupRepository.findAll();
        List<GroupResponseDTO> matchingGroups = new ArrayList<>();
        String lowerQuery = query.toLowerCase();

        for (Group group : allGroups) {
            boolean matches = false;

            if (group.getName() != null && group.getName().toLowerCase().contains(lowerQuery)) {
                matches = true;
            }

            if (group.getDescription() != null && group.getDescription().toLowerCase().contains(lowerQuery)) {
                matches = true;
            }

            if (matches) {
                matchingGroups.add(groupService.groupToGroupDto(group));
            }
        }

        return matchingGroups;
    }

    /**
     * Search expenses by title or description
     * Following SRP - SearchService handles expense search
     */
    public List<ExpenseResponseDTO> searchExpenses(String query) {
        List<Expense> allExpenses = expenseRepository.findAll();
        List<ExpenseResponseDTO> matchingExpenses = new ArrayList<>();
        String lowerQuery = query.toLowerCase();

        for (Expense expense : allExpenses) {
            boolean matches = false;

            if (expense.getTitle() != null && expense.getTitle().toLowerCase().contains(lowerQuery)) {
                matches = true;
            }

            if (expense.getDescription() != null && expense.getDescription().toLowerCase().contains(lowerQuery)) {
                matches = true;
            }

            if (matches) {
                matchingExpenses.add(expenseService.expenseToExpenseDto(expense));
            }
        }

        return matchingExpenses;
    }

    /**
     * Search settlements by description
     * Following SRP - SearchService handles settlement search
     */
    public List<SettlementResponseDTO> searchSettlements(String query) {
        List<Settlement> allSettlements = settlementRepository.findAll();
        List<SettlementResponseDTO> matchingSettlements = new ArrayList<>();
        String lowerQuery = query.toLowerCase();

        for (Settlement settlement : allSettlements) {
            boolean matches = false;

            if (settlement.getDescription() != null && settlement.getDescription().toLowerCase().contains(lowerQuery)) {
                matches = true;
            }

            if (matches) {
                matchingSettlements.add(settlementService.settlementToSettlementDto(settlement));
            }
        }

        return matchingSettlements;
    }

    /**
     * Get search suggestions (autocomplete)
     * Following SRP - SearchService handles search suggestions
     */
    public SearchSuggestionsResponseDTO getSearchSuggestions(String query) {
        SearchSuggestionsResponseDTO suggestions = new SearchSuggestionsResponseDTO();
        List<String> userSuggestions = new ArrayList<>();
        List<String> groupSuggestions = new ArrayList<>();
        String lowerQuery = query.toLowerCase();

        // Get user name suggestions
        List<User> allUsers = userRepository.findAll();
        for (User user : allUsers) {
            if (user.getName() != null && user.getName().toLowerCase().contains(lowerQuery)) {
                userSuggestions.add(user.getName());
            }
            if (userSuggestions.size() >= 5) {
                break;
            }
        }

        // Get group name suggestions
        List<Group> allGroups = groupRepository.findAll();
        for (Group group : allGroups) {
            if (group.getName() != null && group.getName().toLowerCase().contains(lowerQuery)) {
                groupSuggestions.add(group.getName());
            }
            if (groupSuggestions.size() >= 5) {
                break;
            }
        }

        suggestions.setUsers(userSuggestions);
        suggestions.setGroups(groupSuggestions);
        return suggestions;
    }
}

