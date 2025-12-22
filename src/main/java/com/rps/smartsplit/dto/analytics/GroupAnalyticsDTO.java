package com.rps.smartsplit.dto.analytics;

import com.rps.smartsplit.dto.balance.GroupBalanceSummaryDTO;

import java.math.BigDecimal;
import java.util.UUID;

public class GroupAnalyticsDTO {
    private UUID groupId;
    private BigDecimal totalSpent;
    private Integer expenseCount;
    private GroupBalanceSummaryDTO balanceSummary;

    public GroupAnalyticsDTO() {
    }

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }

    public BigDecimal getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(BigDecimal totalSpent) {
        this.totalSpent = totalSpent;
    }

    public Integer getExpenseCount() {
        return expenseCount;
    }

    public void setExpenseCount(Integer expenseCount) {
        this.expenseCount = expenseCount;
    }

    public GroupBalanceSummaryDTO getBalanceSummary() {
        return balanceSummary;
    }

    public void setBalanceSummary(GroupBalanceSummaryDTO balanceSummary) {
        this.balanceSummary = balanceSummary;
    }
}

