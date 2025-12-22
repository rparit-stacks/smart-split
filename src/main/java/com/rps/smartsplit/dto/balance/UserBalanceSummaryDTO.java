package com.rps.smartsplit.dto;


import java.math.BigDecimal;
import java.util.UUID;

public class UserBalanceSummaryDTO {
    private UUID userId;
    private String userName;
    private BigDecimal totalOwed;
    private BigDecimal totalOwedToUser;
    private BigDecimal netBalance;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public BigDecimal getTotalOwed() {
        return totalOwed;
    }

    public void setTotalOwed(BigDecimal totalOwed) {
        this.totalOwed = totalOwed;
    }

    public BigDecimal getTotalOwedToUser() {
        return totalOwedToUser;
    }

    public void setTotalOwedToUser(BigDecimal totalOwedToUser) {
        this.totalOwedToUser = totalOwedToUser;
    }

    public BigDecimal getNetBalance() {
        return netBalance;
    }

    public void setNetBalance(BigDecimal netBalance) {
        this.netBalance = netBalance;
    }
}
