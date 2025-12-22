package com.rps.smartsplit.dto.analytics;

import com.rps.smartsplit.dto.common.CategoryBreakdownDTO;

import java.math.BigDecimal;
import java.util.List;

public class AnalyticsOverviewDTO {
    private BigDecimal totalSpent;
    private Integer totalExpenses;
    private List<CategoryBreakdownDTO> categoryBreakdown;

    public AnalyticsOverviewDTO() {
    }

    public BigDecimal getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(BigDecimal totalSpent) {
        this.totalSpent = totalSpent;
    }

    public Integer getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(Integer totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public List<CategoryBreakdownDTO> getCategoryBreakdown() {
        return categoryBreakdown;
    }

    public void setCategoryBreakdown(List<CategoryBreakdownDTO> categoryBreakdown) {
        this.categoryBreakdown = categoryBreakdown;
    }
}

