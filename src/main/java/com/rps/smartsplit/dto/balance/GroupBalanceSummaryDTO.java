package com.rps.smartsplit.dto.balance;

import java.math.BigDecimal;

public class GroupBalanceSummaryDTO {
    private BigDecimal totalOwed;
    private BigDecimal totalOwing;
    private BigDecimal netBalance;

    public GroupBalanceSummaryDTO() {
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

    public BigDecimal getNetBalance() {
        return netBalance;
    }

    public void setNetBalance(BigDecimal netBalance) {
        this.netBalance = netBalance;
    }
}

