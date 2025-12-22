package com.rps.smartsplit.dto.activity;

import com.rps.smartsplit.dto.response.ExpenseResponseDTO;
import com.rps.smartsplit.dto.response.SettlementResponseDTO;

import java.time.Instant;
import java.util.UUID;

public class ActivityItemDTO {
    private String type; // "expense" or "settlement"
    private UUID id;
    private String description;
    private Instant activityDate;
    private UUID groupId;
    private String groupName;
    private ExpenseResponseDTO expense; // If type is "expense"
    private SettlementResponseDTO settlement; // If type is "settlement"

    public ActivityItemDTO() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(Instant activityDate) {
        this.activityDate = activityDate;
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

    public ExpenseResponseDTO getExpense() {
        return expense;
    }

    public void setExpense(ExpenseResponseDTO expense) {
        this.expense = expense;
    }

    public SettlementResponseDTO getSettlement() {
        return settlement;
    }

    public void setSettlement(SettlementResponseDTO settlement) {
        this.settlement = settlement;
    }
}

