package com.rps.smartsplit.dto.balance;

import java.math.BigDecimal;

public class BalanceMatrixCellDTO {
    private BigDecimal amount;
    private String direction; // "from_to" or "to_from" or "zero"

    public BalanceMatrixCellDTO() {
    }

    public BalanceMatrixCellDTO(BigDecimal amount, String direction) {
        this.amount = amount;
        this.direction = direction;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}

