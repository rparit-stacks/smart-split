package com.rps.smartsplit.dto.report;

import com.rps.smartsplit.dto.response.ExpenseResponseDTO;

import java.math.BigDecimal;
import java.util.List;

public class MonthlyReportDTO {
    private Integer year;
    private Integer month;
    private BigDecimal totalSpent;
    private Integer expenseCount;
    private List<ExpenseResponseDTO> expenses;

    public MonthlyReportDTO() {
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
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

    public List<ExpenseResponseDTO> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<ExpenseResponseDTO> expenses) {
        this.expenses = expenses;
    }
}

