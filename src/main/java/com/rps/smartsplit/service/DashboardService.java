package com.rps.smartsplit.service;

import com.rps.smartsplit.dto.balance.BalanceSummaryDTO;
import com.rps.smartsplit.dto.dashboard.RecentActivityDTO;
import com.rps.smartsplit.dto.dashboard.StatisticsDTO;
import com.rps.smartsplit.dto.dashboard.QuickStatsDTO;
import com.rps.smartsplit.dto.dashboard.CategoryBreakdownDTO;
import com.rps.smartsplit.dto.common.GroupSummaryDTO;
import com.rps.smartsplit.dto.balance.BalanceDTO;
import com.rps.smartsplit.dto.balance.GroupBalancesResponseDTO;
import com.rps.smartsplit.dto.balance.GroupBalanceSummaryDTO;
import com.rps.smartsplit.dto.balance.UserBalanceDTO;
import com.rps.smartsplit.dto.dashboard.DashboardResponseDTO;
import com.rps.smartsplit.dto.response.ExpenseResponseDTO;
import com.rps.smartsplit.dto.response.SettlementResponseDTO;
import com.rps.smartsplit.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class DashboardService {

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private SettlementService settlementService;

    @Autowired
    private CategoryService categoryService;

    /**
     * Get complete dashboard data for logged-in user
     * Following SRP - DashboardService aggregates data from multiple services
     */
    public DashboardResponseDTO getDashboard() {
        User currentUser = userService.getLoggedInUser();
        if (currentUser == null) {
            throw new RuntimeException("User not authenticated");
        }

        DashboardResponseDTO dashboard = new DashboardResponseDTO();
        dashboard.setBalanceSummary(getBalanceSummary());
        dashboard.setRecentActivity(getRecentActivity());
        dashboard.setStatistics(getStatistics());
        dashboard.setGroupsSummary(getGroupsSummary());
        dashboard.setUpcomingSettlements(getUpcomingSettlements());
        dashboard.setQuickStats(getQuickStats());

        return dashboard;
    }

    /**
     * Get balance summary (total owe, total owed, by group, net balance)
     * Following SRP - DashboardService handles dashboard balance aggregation
     */
    public BalanceSummaryDTO getBalanceSummary() {
        User currentUser = userService.getLoggedInUser();
        if (currentUser == null) {
            throw new RuntimeException("User not authenticated");
        }

        List<Group> userGroups = new ArrayList<>(currentUser.getGroups());
        BigDecimal totalOwed = BigDecimal.ZERO;
        BigDecimal totalOwing = BigDecimal.ZERO;
        List<GroupBalanceSummaryDTO> byGroup = new ArrayList<>();

        for (Group group : userGroups) {
            GroupBalancesResponseDTO groupBalances = groupService.getGroupBalancesWithSettlements(group.getId());
            GroupBalanceSummaryDTO summary = groupBalances.getSummary();

            if (summary != null) {
                // Calculate user's position in this group
                UserBalanceDTO userBalance = findUserBalance(groupBalances.getUserSummaries(), currentUser.getId());
                if (userBalance != null) {
                    totalOwed = totalOwed.add(userBalance.getTotalOwed());
                    totalOwing = totalOwing.add(userBalance.getTotalOwing());
                }

                GroupBalanceSummaryDTO groupSummary = new GroupBalanceSummaryDTO();
                groupSummary.setTotalOwed(summary.getTotalOwed());
                groupSummary.setTotalOwing(summary.getTotalOwing());
                groupSummary.setNetBalance(summary.getNetBalance());
                byGroup.add(groupSummary);
            }
        }

        BigDecimal netBalance = totalOwed.subtract(totalOwing);

        BalanceSummaryDTO balanceSummary = new BalanceSummaryDTO();
        balanceSummary.setTotalOwed(totalOwed);
        balanceSummary.setTotalOwing(totalOwing);
        balanceSummary.setNetBalance(netBalance);
        balanceSummary.setByGroup(byGroup);

        return balanceSummary;
    }

    /**
     * Get recent activity (last 15 expenses and settlements)
     * Following SRP - DashboardService handles dashboard activity aggregation
     */
    public RecentActivityDTO getRecentActivity() {
        User currentUser = userService.getLoggedInUser();
        if (currentUser == null) {
            throw new RuntimeException("User not authenticated");
        }

        // Get user's expenses
        List<ExpenseResponseDTO> userExpenses = expenseService.getExpensesByUser(currentUser.getId());
        
        // Sort by date descending and take first 15
        List<ExpenseResponseDTO> sortedExpenses = new ArrayList<>(userExpenses);
        sortedExpenses.sort((e1, e2) -> {
            if (e1.getExpenseDate() == null && e2.getExpenseDate() == null) return 0;
            if (e1.getExpenseDate() == null) return 1;
            if (e2.getExpenseDate() == null) return -1;
            return e2.getExpenseDate().compareTo(e1.getExpenseDate());
        });

        List<ExpenseResponseDTO> recentExpenses = new ArrayList<>();
        int expenseCount = Math.min(15, sortedExpenses.size());
        for (int i = 0; i < expenseCount; i++) {
            recentExpenses.add(sortedExpenses.get(i));
        }

        // Get user's settlements
        List<SettlementResponseDTO> userSettlements = settlementService.getSettlementsByUser(currentUser.getId());
        
        // Sort by date descending and take first 15
        List<SettlementResponseDTO> sortedSettlements = new ArrayList<>(userSettlements);
        sortedSettlements.sort((s1, s2) -> {
            if (s1.getSettlementDate() == null && s2.getSettlementDate() == null) return 0;
            if (s1.getSettlementDate() == null) return 1;
            if (s2.getSettlementDate() == null) return -1;
            return s2.getSettlementDate().compareTo(s1.getSettlementDate());
        });

        List<SettlementResponseDTO> recentSettlements = new ArrayList<>();
        int settlementCount = Math.min(15, sortedSettlements.size());
        for (int i = 0; i < settlementCount; i++) {
            recentSettlements.add(sortedSettlements.get(i));
        }

        RecentActivityDTO recentActivity = new RecentActivityDTO();
        recentActivity.setRecentExpenses(recentExpenses);
        recentActivity.setRecentSettlements(recentSettlements);

        return recentActivity;
    }

    /**
     * Get user statistics (total spent, categories breakdown, expense count)
     * Following SRP - DashboardService handles dashboard statistics aggregation
     */
    public StatisticsDTO getStatistics() {
        User currentUser = userService.getLoggedInUser();
        if (currentUser == null) {
            throw new RuntimeException("User not authenticated");
        }

        List<ExpenseResponseDTO> userExpenses = expenseService.getExpensesByUser(currentUser.getId());
        
        BigDecimal totalSpent = BigDecimal.ZERO;
        Map<UUID, CategoryBreakdownDTO> categoryMap = new HashMap<>();

        for (ExpenseResponseDTO expense : userExpenses) {
            if (expense.getAmount() != null) {
                totalSpent = totalSpent.add(expense.getAmount());
            }

            if (expense.getCategoryId() != null) {
                CategoryBreakdownDTO breakdown = categoryMap.get(expense.getCategoryId());
                if (breakdown == null) {
                    breakdown = new CategoryBreakdownDTO();
                    breakdown.setCategoryId(expense.getCategoryId());
                    breakdown.setTotalAmount(BigDecimal.ZERO);
                    breakdown.setExpenseCount(0);
                    categoryMap.put(expense.getCategoryId(), breakdown);
                }
                breakdown.setTotalAmount(breakdown.getTotalAmount().add(expense.getAmount()));
                breakdown.setExpenseCount(breakdown.getExpenseCount() + 1);
            }
        }

        // Get category names
        List<CategoryBreakdownDTO> categoryBreakdown = new ArrayList<>();
        for (CategoryBreakdownDTO breakdown : categoryMap.values()) {
            try {
                Category category = categoryService.getACategoryById(breakdown.getCategoryId());
                if (category != null) {
                    breakdown.setCategoryName(category.getName());
                }
            } catch (Exception e) {
                breakdown.setCategoryName("Unknown");
            }
            categoryBreakdown.add(breakdown);
        }

        StatisticsDTO statistics = new StatisticsDTO();
        statistics.setTotalSpent(totalSpent);
        statistics.setExpenseCount(userExpenses.size());
        statistics.setCategoryBreakdown(categoryBreakdown);

        return statistics;
    }

    /**
     * Get groups summary (list of groups with member count, recent activity)
     * Following SRP - DashboardService handles dashboard groups aggregation
     */
    public List<GroupSummaryDTO> getGroupsSummary() {
        User currentUser = userService.getLoggedInUser();
        if (currentUser == null) {
            throw new RuntimeException("User not authenticated");
        }

        List<Group> userGroups = new ArrayList<>(currentUser.getGroups());
        List<GroupSummaryDTO> groupsSummary = new ArrayList<>();

        for (Group group : userGroups) {
            GroupSummaryDTO summary = groupService.getGroupSummary(group.getId());
            groupsSummary.add(summary);
        }

        return groupsSummary;
    }

    /**
     * Get upcoming settlements (debts that need to be settled - where user owes money)
     * Following SRP - DashboardService handles dashboard settlements aggregation
     */
    public List<BalanceDTO> getUpcomingSettlements() {
        User currentUser = userService.getLoggedInUser();
        if (currentUser == null) {
            throw new RuntimeException("User not authenticated");
        }

        List<Group> userGroups = new ArrayList<>(currentUser.getGroups());
        List<BalanceDTO> upcomingSettlements = new ArrayList<>();

        for (Group group : userGroups) {
            GroupBalancesResponseDTO groupBalances = groupService.getGroupBalancesWithSettlements(group.getId());
            List<BalanceDTO> balances = groupBalances.getBalances();

            if (balances != null) {
                for (BalanceDTO balance : balances) {
                    // Only include balances where current user owes money
                    if (balance.getFromUserId().equals(currentUser.getId()) && 
                        balance.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                        upcomingSettlements.add(balance);
                    }
                }
            }
        }

        return upcomingSettlements;
    }

    /**
     * Get quick statistics (total expenses, total groups, active balances)
     * Following SRP - DashboardService handles dashboard quick stats aggregation
     */
    public QuickStatsDTO getQuickStats() {
        User currentUser = userService.getLoggedInUser();
        if (currentUser == null) {
            throw new RuntimeException("User not authenticated");
        }

        List<ExpenseResponseDTO> userExpenses = expenseService.getExpensesByUser(currentUser.getId());
        List<Group> userGroups = new ArrayList<>(currentUser.getGroups());
        
        BalanceSummaryDTO balanceSummary = getBalanceSummary();
        int activeBalances = getUpcomingSettlements().size();

        QuickStatsDTO quickStats = new QuickStatsDTO();
        quickStats.setTotalExpenses(userExpenses.size());
        quickStats.setTotalGroups(userGroups.size());
        quickStats.setActiveBalances(activeBalances);
        quickStats.setTotalOwed(balanceSummary.getTotalOwed());
        quickStats.setTotalOwing(balanceSummary.getTotalOwing());

        return quickStats;
    }

    /**
     * Helper method to find user balance in user summaries
     */
    private UserBalanceDTO findUserBalance(List<UserBalanceDTO> userSummaries, UUID userId) {
        if (userSummaries == null) {
            return null;
        }
        for (UserBalanceDTO userBalance : userSummaries) {
            if (userBalance.getUserId().equals(userId)) {
                return userBalance;
            }
        }
        return null;
    }
}

