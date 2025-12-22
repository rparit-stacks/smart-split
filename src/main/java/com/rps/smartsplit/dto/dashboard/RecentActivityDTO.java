package com.rps.smartsplit.dto.dashboard;

import com.rps.smartsplit.dto.response.ExpenseResponseDTO;
import com.rps.smartsplit.dto.response.SettlementResponseDTO;

import java.util.List;

public class RecentActivityDTO {
    private List<ExpenseResponseDTO> recentExpenses;
    private List<SettlementResponseDTO> recentSettlements;

    public RecentActivityDTO() {
    }

    public List<ExpenseResponseDTO> getRecentExpenses() {
        return recentExpenses;
    }

    public void setRecentExpenses(List<ExpenseResponseDTO> recentExpenses) {
        this.recentExpenses = recentExpenses;
    }

    public List<SettlementResponseDTO> getRecentSettlements() {
        return recentSettlements;
    }

    public void setRecentSettlements(List<SettlementResponseDTO> recentSettlements) {
        this.recentSettlements = recentSettlements;
    }
}

