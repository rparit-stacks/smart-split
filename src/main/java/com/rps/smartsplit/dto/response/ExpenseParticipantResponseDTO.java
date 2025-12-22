package com.rps.smartsplit.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

public class ExpenseParticipantResponseDTO {
    private UUID id;
    private UUID expenseId;
    private UUID userId;
    private BigDecimal amount;
    private boolean isPaid;

    public ExpenseParticipantResponseDTO() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(UUID expenseId) {
        this.expenseId = expenseId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }
}

