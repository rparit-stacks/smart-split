package com.rps.smartsplit.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class PaymentResponseDTO {
    private UUID id;
    private UUID settlementId;
    private BigDecimal amount;
    private String paymentMethod;
    private Instant paymentDate;
    private String notes;

    public PaymentResponseDTO() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getSettlementId() {
        return settlementId;
    }

    public void setSettlementId(UUID settlementId) {
        this.settlementId = settlementId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Instant getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Instant paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}

