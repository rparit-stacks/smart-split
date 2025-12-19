package com.rps.smartsplit.dto;

import java.util.List;
import java.util.UUID;

public class GroupBalancesResponseDTO {
    private UUID groupId;
    private String groupName;
    private List<BalanceDTO> balances;
    private GroupBalanceSummaryDTO summary;
    private List<UserBalanceDTO> userSummaries;

    public GroupBalancesResponseDTO() {
    }

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<BalanceDTO> getBalances() {
        return balances;
    }

    public void setBalances(List<BalanceDTO> balances) {
        this.balances = balances;
    }

    public GroupBalanceSummaryDTO getSummary() {
        return summary;
    }

    public void setSummary(GroupBalanceSummaryDTO summary) {
        this.summary = summary;
    }

    public List<UserBalanceDTO> getUserSummaries() {
        return userSummaries;
    }

    public void setUserSummaries(List<UserBalanceDTO> userSummaries) {
        this.userSummaries = userSummaries;
    }
}

