package com.rps.smartsplit.dto.report;

import com.rps.smartsplit.dto.common.CategoryBreakdownDTO;

import java.math.BigDecimal;
import java.util.List;

public class CategoryBreakdownReportDTO {
    private String startDate;
    private String endDate;
    private BigDecimal totalSpent;
    private List<CategoryBreakdownDTO> categoryBreakdown;

    public CategoryBreakdownReportDTO() {
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

    public List<CategoryBreakdownDTO> getCategoryBreakdown() {
        return categoryBreakdown;
    }

    public void setCategoryBreakdown(List<CategoryBreakdownDTO> categoryBreakdown) {
        this.categoryBreakdown = categoryBreakdown;
    }
}

