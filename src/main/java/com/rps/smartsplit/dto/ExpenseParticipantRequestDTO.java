package com.rps.smartsplit.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class ExpenseParticipantRequestDTO {
    private UUID expenseId;
    private UUID userId;
    private BigDecimal amount;
    private boolean isPaid;

    public ExpenseParticipantRequestDTO() {
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

