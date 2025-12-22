package com.rps.smartsplit.dto.report;

import com.rps.smartsplit.dto.balance.GroupBalanceSummaryDTO;
import com.rps.smartsplit.dto.response.ExpenseResponseDTO;
import com.rps.smartsplit.dto.response.SettlementResponseDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class GroupReportDTO {
    private UUID groupId;
    private String groupName;
    private String startDate;
    private String endDate;
    private BigDecimal totalExpenses;
    private BigDecimal totalSettlements;
    private Integer expenseCount;
    private Integer settlementCount;
    private GroupBalanceSummaryDTO balanceSummary;
    private List<ExpenseResponseDTO> expenses;
    private List<SettlementResponseDTO> settlements;

    public GroupReportDTO() {
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

    public BigDecimal getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(BigDecimal totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public BigDecimal getTotalSettlements() {
        return totalSettlements;
    }

    public void setTotalSettlements(BigDecimal totalSettlements) {
        this.totalSettlements = totalSettlements;
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

    public GroupBalanceSummaryDTO getBalanceSummary() {
        return balanceSummary;
    }

    public void setBalanceSummary(GroupBalanceSummaryDTO balanceSummary) {
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

