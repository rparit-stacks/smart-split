package com.rps.smartsplit.dto.report;

import com.rps.smartsplit.dto.balance.UserBalanceSummaryDTO;
import com.rps.smartsplit.dto.response.ExpenseResponseDTO;
import com.rps.smartsplit.dto.response.SettlementResponseDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class UserReportDTO {
    private UUID userId;
    private String userName;
    private String startDate;
    private String endDate;
    private BigDecimal totalSpent;
    private BigDecimal totalReceived;
    private Integer expenseCount;
    private Integer settlementCount;
    private UserBalanceSummaryDTO balanceSummary;
    private List<ExpenseResponseDTO> expenses;
    private List<SettlementResponseDTO> settlements;

    public UserReportDTO() {
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(BigDecimal totalSpent) {
        this.totalSpent = totalSpent;
    }

    public BigDecimal getTotalReceived() {
        return totalReceived;
    }

    public void setTotalReceived(BigDecimal totalReceived) {
        this.totalReceived = totalReceived;
    }

    public Integer getExpenseCount() {
        return expenseCount;
    }

    public void setExpenseCount(Integer expenseCount) {
        this.expenseCount = expenseCount;
    }

    public Integer getSettlementCount() {
        return settlementCount;
    }

    public void setSettlementCount(Integer settlementCount) {
        this.settlementCount = settlementCount;
    }

    public UserBalanceSummaryDTO getBalanceSummary() {
        return balanceSummary;
    }

    public void setBalanceSummary(UserBalanceSummaryDTO balanceSummary) {
        this.balanceSummary = balanceSummary;
    }

    public List<ExpenseResponseDTO> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<ExpenseResponseDTO> expenses) {
        this.expenses = expenses;
    }

    public List<SettlementResponseDTO> getSettlements() {
        return settlements;
    }

    public void setSettlements(List<SettlementResponseDTO> settlements) {
        this.settlements = settlements;
    }
}

