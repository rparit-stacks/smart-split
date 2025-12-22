package com.rps.smartsplit.dto.analytics;

import java.math.BigDecimal;
import java.util.Map;

public class BalanceTrendsDTO {
    private Map<String, BigDecimal> trends; // date/period -> balance

    public BalanceTrendsDTO() {
    }

    public Map<String, BigDecimal> getTrends() {
        return trends;
    }

    public void setTrends(Map<String, BigDecimal> trends) {
        this.trends = trends;
    }
}

