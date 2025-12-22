package com.rps.smartsplit.dto.activity;

import java.util.List;

public class ActivityFeedResponseDTO {
    private List<ActivityItemDTO> activities;
    private Integer totalCount;

    public ActivityFeedResponseDTO() {
    }

    public List<ActivityItemDTO> getActivities() {
        return activities;
    }

    public void setActivities(List<ActivityItemDTO> activities) {
        this.activities = activities;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}

