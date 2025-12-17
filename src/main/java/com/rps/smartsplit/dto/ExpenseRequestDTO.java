package com.rps.smartsplit.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class ExpenseRequestDTO {
    private UUID groupId;
    private UUID paidById;
    private UUID categoryId;
    private Instant expenseDate;
    private String title;
    private String description;
    private BigDecimal amount;

    public ExpenseRequestDTO() {
    }

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }

    public UUID getPaidById() {
        return paidById;
    }

    public void setPaidById(UUID paidById) {
        this.paidById = paidById;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(UUID categoryId) {
        this.categoryId = categoryId;
    }

    public Instant getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(Instant expenseDate) {
        this.expenseDate = expenseDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}

