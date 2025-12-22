package com.rps.smartsplit.dto.balance;

import java.util.List;
import java.util.UUID;

public class BalanceMatrixResponseDTO {
    private UUID groupId;
    private String groupName;
    private List<String> userIds; // List of user IDs in order
    private List<String> userNames; // List of user names in order
    private List<List<BalanceMatrixCellDTO>> matrix; // 2D matrix of balances

    public BalanceMatrixResponseDTO() {
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

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public List<String> getUserNames() {
        return userNames;
    }

    public void setUserNames(List<String> userNames) {
        this.userNames = userNames;
    }

    public List<List<BalanceMatrixCellDTO>> getMatrix() {
        return matrix;
    }

    public void setMatrix(List<List<BalanceMatrixCellDTO>> matrix) {
        this.matrix = matrix;
    }
}

