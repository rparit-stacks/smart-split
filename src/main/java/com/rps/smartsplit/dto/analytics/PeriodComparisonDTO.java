package com.rps.smartsplit.dto.analytics;

import java.math.BigDecimal;

public class PeriodComparisonDTO {
    private String period1Start;
    private String period1End;
    private BigDecimal period1Total;
    private String period2Start;
    private String period2End;
    private BigDecimal period2Total;
    private BigDecimal difference;
    private BigDecimal percentageChange;

    public PeriodComparisonDTO() {
    }

    public String getPeriod1Start() {
        return period1Start;
    }

    public void setPeriod1Start(String period1Start) {
        this.period1Start = period1Start;
    }

    public String getPeriod1End() {
        return period1End;
    }

    public void setPeriod1End(String period1End) {
        this.period1End = period1End;
    }

    public BigDecimal getPeriod1Total() {
        return period1Total;
    }

    public void setPeriod1Total(BigDecimal period1Total) {
        this.period1Total = period1Total;
    }

    public String getPeriod2Start() {
        return period2Start;
    }

    public void setPeriod2Start(String period2Start) {
        this.period2Start = period2Start;
    }

    public String getPeriod2End() {
        return period2End;
    }

    public void setPeriod2End(String period2End) {
        this.period2End = period2End;
    }

    public BigDecimal getPeriod2Total() {
        return period2Total;
    }

    public void setPeriod2Total(BigDecimal period2Total) {
        this.period2Total = period2Total;
    }

    public BigDecimal getDifference() {
        return difference;
    }

    public void setDifference(BigDecimal difference) {
        this.difference = difference;
    }

    public BigDecimal getPercentageChange() {
        return percentageChange;
    }

    public void setPercentageChange(BigDecimal percentageChange) {
        this.percentageChange = percentageChange;
    }
}

