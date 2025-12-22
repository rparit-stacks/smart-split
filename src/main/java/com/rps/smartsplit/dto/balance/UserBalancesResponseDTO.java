package com.rps.smartsplit.dto.balance;

import java.util.List;
import java.util.UUID;

public class UserBalancesResponseDTO {
    private UUID userId;
    private String userName;
    private List<BalanceDTO> balances;
    private UserBalanceSummaryDTO summary;
    private List<GroupBalanceInfoDTO> byGroup;

    public UserBalancesResponseDTO() {
    }

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

    public List<BalanceDTO> getBalances() {
        return balances;
    }

    public void setBalances(List<BalanceDTO> balances) {
        this.balances = balances;
    }

    public UserBalanceSummaryDTO getSummary() {
        return summary;
    }

    public void setSummary(UserBalanceSummaryDTO summary) {
        this.summary = summary;
    }

    public List<GroupBalanceInfoDTO> getByGroup() {
        return byGroup;
    }

    public void setByGroup(List<GroupBalanceInfoDTO> byGroup) {
        this.byGroup = byGroup;
    }
}

