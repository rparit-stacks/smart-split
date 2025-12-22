package com.rps.smartsplit.dto.balance;

import java.util.List;
import java.util.UUID;

public class GroupBalanceInfoDTO {
    private UUID groupId;
    private String groupName;
    private List<BalanceDTO> balances;
    private UserBalanceDTO userBalance;

    public GroupBalanceInfoDTO() {
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

    public UserBalanceDTO getUserBalance() {
        return userBalance;
    }

    public void setUserBalance(UserBalanceDTO userBalance) {
        this.userBalance = userBalance;
    }
}

