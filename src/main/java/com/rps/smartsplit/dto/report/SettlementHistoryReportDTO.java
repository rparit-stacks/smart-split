package com.rps.smartsplit.dto.report;

import com.rps.smartsplit.dto.response.SettlementResponseDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class SettlementHistoryReportDTO {
    private UUID groupId;
    private String startDate;
    private String endDate;
    private BigDecimal totalSettled;
    private Integer settlementCount;
    private List<SettlementResponseDTO> settlements;

    public SettlementHistoryReportDTO() {
    }

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getTotalSettled() {
        return totalSettled;
    }

    public void setTotalSettled(BigDecimal totalSettled) {
        this.totalSettled = totalSettled;
    }

    public Integer getSettlementCount() {
        return settlementCount;
    }

    public void setSettlementCount(Integer settlementCount) {
        this.settlementCount = settlementCount;
    }

    public List<SettlementResponseDTO> getSettlements() {
        return settlements;
    }

    public void setSettlements(List<SettlementResponseDTO> settlements) {
        this.settlements = settlements;
    }
}

