package com.rps.smartsplit.dto.dashboard;

import com.rps.smartsplit.dto.balance.BalanceDTO;
import com.rps.smartsplit.dto.balance.BalanceSummaryDTO;
import com.rps.smartsplit.dto.common.GroupSummaryDTO;

import java.util.List;

public class DashboardResponseDTO {
    private BalanceSummaryDTO balanceSummary;
    private RecentActivityDTO recentActivity;
    private StatisticsDTO statistics;
    private List<GroupSummaryDTO> groupsSummary;
    private List<BalanceDTO> upcomingSettlements;
    private QuickStatsDTO quickStats;

    public DashboardResponseDTO() {
    }

    public BalanceSummaryDTO getBalanceSummary() {
        return balanceSummary;
    }

    public void setBalanceSummary(BalanceSummaryDTO balanceSummary) {
        this.balanceSummary = balanceSummary;
    }

    public RecentActivityDTO getRecentActivity() {
        return recentActivity;
    }

    public void setRecentActivity(RecentActivityDTO recentActivity) {
        this.recentActivity = recentActivity;
    }

    public StatisticsDTO getStatistics() {
        return statistics;
    }

    public void setStatistics(StatisticsDTO statistics) {
        this.statistics = statistics;
    }

    public List<GroupSummaryDTO> getGroupsSummary() {
        return groupsSummary;
    }

    public void setGroupsSummary(List<GroupSummaryDTO> groupsSummary) {
        this.groupsSummary = groupsSummary;
    }

    public List<BalanceDTO> getUpcomingSettlements() {
        return upcomingSettlements;
    }

    public void setUpcomingSettlements(List<BalanceDTO> upcomingSettlements) {
        this.upcomingSettlements = upcomingSettlements;
    }

    public QuickStatsDTO getQuickStats() {
        return quickStats;
    }

    public void setQuickStats(QuickStatsDTO quickStats) {
        this.quickStats = quickStats;
    }
}

