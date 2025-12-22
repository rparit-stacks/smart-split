package com.rps.smartsplit.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class SettlementResponseDTO {
    private UUID id;
    private UUID groupId;
    private UUID payerId;
    private UUID payeeId;
    private BigDecimal amount;
    private String description;
    private Instant settlementDate;
    private Instant createdAt;

    public SettlementResponseDTO() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }

    public UUID getPayerId() {
        return payerId;
    }

    public void setPayerId(UUID payerId) {
        this.payerId = payerId;
    }

    public UUID getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(UUID payeeId) {
        this.payeeId = payeeId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(Instant settlementDate) {
        this.settlementDate = settlementDate;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}

