package com.rps.smartsplit.dto.dashboard;

import com.rps.smartsplit.dto.balance.GroupBalanceSummaryDTO;

import java.math.BigDecimal;
import java.util.List;

public class BalanceSummaryDTO {
    private BigDecimal totalOwed;
    private BigDecimal totalOwing;
    private BigDecimal netBalance;
    private List<GroupBalanceSummaryDTO> byGroup;

    public BalanceSummaryDTO() {
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

    public List<GroupBalanceSummaryDTO> getByGroup() {
        return byGroup;
    }

    public void setByGroup(List<GroupBalanceSummaryDTO> byGroup) {
        this.byGroup = byGroup;
    }
}

