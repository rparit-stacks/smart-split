package com.rps.smartsplit.dto.report;

import com.rps.smartsplit.dto.response.ExpenseResponseDTO;

import java.math.BigDecimal;
import java.util.List;

public class TaxReportDTO {
    private Integer year;
    private BigDecimal totalSpent;
    private Integer expenseCount;
    private List<ExpenseResponseDTO> expenses;

    public TaxReportDTO() {
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
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

