package com.rps.smartsplit.dto;

public class CurrencyResponseDTO {
    private String currency;

    public CurrencyResponseDTO() {
    }

    public CurrencyResponseDTO(String currency) {
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}

