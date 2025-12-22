package com.rps.smartsplit.dto.dashboard;

import java.math.BigDecimal;
import java.util.List;

public class StatisticsDTO {
    private BigDecimal totalSpent;
    private Integer expenseCount;
    private List<CategoryBreakdownDTO> categoryBreakdown;

    public StatisticsDTO() {
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
}

