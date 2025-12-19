package com.rps.smartsplit.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class GroupSummaryDTO {
    private UUID groupId;
    private String groupName;
    private String description;
    private int memberCount;
    private int expenseCount;
    private BigDecimal totalExpenseAmount;
    private BigDecimal totalSettledAmount;
    private GroupBalanceSummaryDTO balanceSummary;
    private List<ExpenseResponseDTO> recentExpenses;
    private List<SettlementResponseDTO> recentSettlements;

    public GroupSummaryDTO() {
    }

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public int getExpenseCount() {
        return expenseCount;
    }

    public void setExpenseCount(int expenseCount) {
        this.expenseCount = expenseCount;
    }

    public BigDecimal getTotalExpenseAmount() {
        return totalExpenseAmount;
    }

    public void setTotalExpenseAmount(BigDecimal totalExpenseAmount) {
        this.totalExpenseAmount = totalExpenseAmount;
    }

    public BigDecimal getTotalSettledAmount() {
        return totalSettledAmount;
    }

    public void setTotalSettledAmount(BigDecimal totalSettledAmount) {
        this.totalSettledAmount = totalSettledAmount;
    }

    public GroupBalanceSummaryDTO getBalanceSummary() {
        return balanceSummary;
    }

    public void setBalanceSummary(GroupBalanceSummaryDTO balanceSummary) {
        this.balanceSummary = balanceSummary;
    }

    public List<ExpenseResponseDTO> getRecentExpenses() {
        return recentExpenses;
    }

    public void setRecentExpenses(List<ExpenseResponseDTO> recentExpenses) {
        this.recentExpenses = recentExpenses;
    }

    public List<SettlementResponseDTO> getRecentSettlements() {
        return recentSettlements;
    }

    public void setRecentSettlements(List<SettlementResponseDTO> recentSettlements) {
        this.recentSettlements = recentSettlements;
    }
}
