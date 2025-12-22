package com.rps.smartsplit.dto.analytics;

import java.math.BigDecimal;

public class MonthlySummaryDTO {
    private Integer year;
    private Integer month;
    private BigDecimal totalSpent;
    private Integer expenseCount;

    public MonthlySummaryDTO() {
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
}

