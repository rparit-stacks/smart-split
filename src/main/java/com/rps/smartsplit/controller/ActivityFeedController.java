package com.rps.smartsplit.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/activity")
public class ActivityFeedController {

    /**
     * GET /api/activity
     * Get activity feed (all expenses and settlements)
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getActivityFeed(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        // TODO: Implement activity feed
        return null;
    }

    /**
     * GET /api/activity/group/{groupId}
     * Get group activity (group-specific feed)
     */
    @GetMapping("/group/{groupId}")
    public ResponseEntity<Map<String, Object>> getGroupActivity(
            @PathVariable UUID groupId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        // TODO: Implement group activity feed
        return null;
    }

    /**
     * GET /api/activity/user/{userId}
     * Get user activity (user-specific feed)
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getUserActivity(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        // TODO: Implement user activity feed
        return null;
    }

    /**
     * GET /api/activity/recent
     * Get recent activity (latest activities)
     */
    @GetMapping("/recent")
    public ResponseEntity<Map<String, Object>> getRecentActivity(
            @RequestParam(defaultValue = "10") int limit) {
        // TODO: Implement recent activity
        return null;
    }

    /**
     * GET /api/activity/filtered
     * Get filtered activity (with filters)
     */
    @GetMapping("/filtered")
    public ResponseEntity<Map<String, Object>> getFilteredActivity(
            @RequestParam(required = false) String type, // expense, settlement
            @RequestParam(required = false) UUID groupId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        // TODO: Implement filtered activity feed
        return null;
    }
}

