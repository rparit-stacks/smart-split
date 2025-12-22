package com.rps.smartsplit.dto.balance;

import java.util.List;
import java.util.UUID;

public class SimplifiedBalancesResponseDTO {
    private UUID groupId;
    private String groupName;
    private List<BalanceDTO> simplifiedBalances;
    private List<SettlementSuggestionDTO> settlementSuggestions;
    private GroupBalanceSummaryDTO summary;

    public SimplifiedBalancesResponseDTO() {
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

    public List<BalanceDTO> getSimplifiedBalances() {
        return simplifiedBalances;
    }

    public void setSimplifiedBalances(List<BalanceDTO> simplifiedBalances) {
        this.simplifiedBalances = simplifiedBalances;
    }

    public List<SettlementSuggestionDTO> getSettlementSuggestions() {
        return settlementSuggestions;
    }

    public void setSettlementSuggestions(List<SettlementSuggestionDTO> settlementSuggestions) {
        this.settlementSuggestions = settlementSuggestions;
    }

    public GroupBalanceSummaryDTO getSummary() {
        return summary;
    }

    public void setSummary(GroupBalanceSummaryDTO summary) {
        this.summary = summary;
    }
}

