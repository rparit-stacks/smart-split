package com.rps.smartsplit.dto.search;

import com.rps.smartsplit.dto.response.UserResponseDTO;
import com.rps.smartsplit.dto.response.GroupResponseDTO;
import com.rps.smartsplit.dto.response.ExpenseResponseDTO;
import com.rps.smartsplit.dto.response.SettlementResponseDTO;

import java.util.List;

public class GlobalSearchResponseDTO {
    private List<UserResponseDTO> users;
    private List<GroupResponseDTO> groups;
    private List<ExpenseResponseDTO> expenses;
    private List<SettlementResponseDTO> settlements;

    public GlobalSearchResponseDTO() {
    }

    public List<UserResponseDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserResponseDTO> users) {
        this.users = users;
    }

    public List<GroupResponseDTO> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupResponseDTO> groups) {
        this.groups = groups;
    }

    public List<ExpenseResponseDTO> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<ExpenseResponseDTO> expenses) {
        this.expenses = expenses;
    }

    public List<SettlementResponseDTO> getSettlements() {
        return settlements;
    }

    public void setSettlements(List<SettlementResponseDTO> settlements) {
        this.settlements = settlements;
    }
}

