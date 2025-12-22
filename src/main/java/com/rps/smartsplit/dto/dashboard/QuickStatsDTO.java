package com.rps.smartsplit.dto.dashboard;

import java.math.BigDecimal;

public class QuickStatsDTO {
    private Integer totalExpenses;
    private Integer totalGroups;
    private Integer activeBalances;
    private BigDecimal totalOwed;
    private BigDecimal totalOwing;

    public QuickStatsDTO() {
    }

    public Integer getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(Integer totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public Integer getTotalGroups() {
        return totalGroups;
    }

    public void setTotalGroups(Integer totalGroups) {
        this.totalGroups = totalGroups;
    }

    public Integer getActiveBalances() {
        return activeBalances;
    }

    public void setActiveBalances(Integer activeBalances) {
        this.activeBalances = activeBalances;
    }

    public BigDecimal getTotalOwed() {
        return totalOwed;
    }

    public void setTotalOwed(BigDecimal totalOwed) {
        this.totalOwed = totalOwed;
    }

    public BigDecimal getTotalOwing() {
        return totalOwing;
    }

    public void setTotalOwing(BigDecimal totalOwing) {
        this.totalOwing = totalOwing;
    }
}

