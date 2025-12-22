package com.rps.smartsplit.dto.analytics;

import java.math.BigDecimal;
import java.util.Map;

public class SpendingTrendsDTO {
    private String period; // day, week, month, year
    private Map<String, BigDecimal> trends; // period -> amount

    public SpendingTrendsDTO() {
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Map<String, BigDecimal> getTrends() {
        return trends;
    }

    public void setTrends(Map<String, BigDecimal> trends) {
        this.trends = trends;
    }
}

