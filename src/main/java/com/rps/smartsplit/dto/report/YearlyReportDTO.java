package com.rps.smartsplit.dto.report;

import java.math.BigDecimal;
import java.util.Map;

public class YearlyReportDTO {
    private Integer year;
    private BigDecimal totalSpent;
    private Integer expenseCount;
    private Map<Integer, BigDecimal> monthlyBreakdown; // month -> amount

    public YearlyReportDTO() {
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

    public Map<Integer, BigDecimal> getMonthlyBreakdown() {
        return monthlyBreakdown;
    }

    public void setMonthlyBreakdown(Map<Integer, BigDecimal> monthlyBreakdown) {
        this.monthlyBreakdown = monthlyBreakdown;
    }
}

