package com.rps.smartsplit.dto.analytics;

import java.util.Map;

public class PaymentFrequencyDTO {
    private Map<String, Integer> frequency; // daily, weekly, monthly, yearly -> count
    private Integer totalSettlements;

    public PaymentFrequencyDTO() {
    }

    public Map<String, Integer> getFrequency() {
        return frequency;
    }

    public void setFrequency(Map<String, Integer> frequency) {
        this.frequency = frequency;
    }

    public Integer getTotalSettlements() {
        return totalSettlements;
    }

    public void setTotalSettlements(Integer totalSettlements) {
        this.totalSettlements = totalSettlements;
    }
}

