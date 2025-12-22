package com.rps.smartsplit.dto.report;

import com.rps.smartsplit.dto.common.CategoryBreakdownDTO;
import com.rps.smartsplit.dto.response.ExpenseResponseDTO;

import java.math.BigDecimal;
import java.util.List;

public class ExpenseSummaryReportDTO {
    private String startDate;
    private String endDate;
    private BigDecimal totalSpent;
    private Integer expenseCount;
    private List<CategoryBreakdownDTO> categoryBreakdown;
    private List<ExpenseResponseDTO> expenses;

    public ExpenseSummaryReportDTO() {
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

    public Integer getExpenseCount() {
        return expenseCount;
    }

    public void setExpenseCount(Integer expenseCount) {
        this.expenseCount = expenseCount;
    }

    public List<CategoryBreakdownDTO> getCategoryBreakdown() {
        return categoryBreakdown;
    }

    public void setCategoryBreakdown(List<CategoryBreakdownDTO> categoryBreakdown) {
        this.categoryBreakdown = categoryBreakdown;
    }

    public List<ExpenseResponseDTO> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<ExpenseResponseDTO> expenses) {
        this.expenses = expenses;
    }
}

