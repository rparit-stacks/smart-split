package com.rps.smartsplit.dto.balance;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class BalanceBetweenUsersResponseDTO {
    private UUID userId1;
    private String userName1;
    private UUID userId2;
    private String userName2;
    private BigDecimal netBalance;
    private String direction; // "userId1_owes_userId2" or "userId2_owes_userId1" or "settled"
    private List<BalanceDTO> individualBalances; // All balances between these two users across all groups

    public BalanceBetweenUsersResponseDTO() {
    }

    public UUID getUserId1() {
        return userId1;
    }

    public void setUserId1(UUID userId1) {
        this.userId1 = userId1;
    }

    public String getUserName1() {
        return userName1;
    }

    public void setUserName1(String userName1) {
        this.userName1 = userName1;
    }

    public UUID getUserId2() {
        return userId2;
    }

    public void setUserId2(UUID userId2) {
        this.userId2 = userId2;
    }

    public String getUserName2() {
        return userName2;
    }

    public void setUserName2(String userName2) {
        this.userName2 = userName2;
    }

    public BigDecimal getNetBalance() {
        return netBalance;
    }

    public void setNetBalance(BigDecimal netBalance) {
        this.netBalance = netBalance;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public List<BalanceDTO> getIndividualBalances() {
        return individualBalances;
    }

    public void setIndividualBalances(List<BalanceDTO> individualBalances) {
        this.individualBalances = individualBalances;
    }
}

