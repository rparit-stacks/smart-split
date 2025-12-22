package com.rps.smartsplit.dto.analytics;

import com.rps.smartsplit.dto.balance.UserBalanceSummaryDTO;

import java.math.BigDecimal;
import java.util.UUID;

public class UserAnalyticsDTO {
    private UUID userId;
    private BigDecimal totalSpent;
    private Integer expenseCount;
    private UserBalanceSummaryDTO balanceSummary;

    public UserAnalyticsDTO() {
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
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

    public UserBalanceSummaryDTO getBalanceSummary() {
        return balanceSummary;
    }

    public void setBalanceSummary(UserBalanceSummaryDTO balanceSummary) {
        this.balanceSummary = balanceSummary;
    }
}

