package com.rps.smartsplit.service;

import com.rps.smartsplit.dto.request.PaymentRequestDTO;
import com.rps.smartsplit.dto.response.PaymentResponseDTO;
import com.rps.smartsplit.model.Payment;
import com.rps.smartsplit.model.Settlement;
import com.rps.smartsplit.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private SettlementService settlementService;

    /**
     * Create a payment record for a settlement
     * Following SRP - PaymentService handles payment creation
     * 
     * This method records payment method details for a settlement.
     * A settlement can have multiple payment records (e.g., partial payments via different methods).
     */
    @Transactional
    public PaymentResponseDTO createPayment(UUID settlementId, PaymentRequestDTO paymentRequestDTO) {
        if (paymentRequestDTO == null) {
            throw new IllegalArgumentException("Payment request must not be null");
        }

        // Validate required fields
        if (paymentRequestDTO.getAmount() == null || paymentRequestDTO.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (paymentRequestDTO.getPaymentMethod() == null || paymentRequestDTO.getPaymentMethod().trim().isEmpty()) {
            throw new IllegalArgumentException("Payment method is required");
        }

        // Get settlement
        Settlement settlement = settlementService.getSettlementById(settlementId);

        // Create payment entity
        Payment payment = new Payment();
        payment.setSettlement(settlement);
        payment.setAmount(paymentRequestDTO.getAmount());
        payment.setPaymentMethod(paymentRequestDTO.getPaymentMethod());
        payment.setNotes(paymentRequestDTO.getNotes());
        
        // Set payment date (use provided date or current time)
        if (paymentRequestDTO.getPaymentDate() != null) {
            payment.setPaymentDate(paymentRequestDTO.getPaymentDate());
        } else {
            payment.setPaymentDate(Instant.now());
        }

        // Save payment
        Payment savedPayment = paymentRepository.save(payment);

        // Convert to DTO and return
        return paymentToPaymentDto(savedPayment);
    }

    /**
     * Get all payments for a settlement
     * Following SRP - PaymentService handles payment queries
     */
    public List<PaymentResponseDTO> getPaymentsBySettlementId(UUID settlementId) {
        // Verify settlement exists
        settlementService.getSettlementById(settlementId);

        List<Payment> allPayments = paymentRepository.findAll();
        List<PaymentResponseDTO> settlementPayments = new ArrayList<>();

        for (Payment payment : allPayments) {
            if (payment.getSettlement() != null && payment.getSettlement().getId().equals(settlementId)) {
                settlementPayments.add(paymentToPaymentDto(payment));
            }
        }

        return settlementPayments;
    }

    /**
     * Convert Payment entity to PaymentResponseDTO
     * Following SRP - PaymentService handles DTO conversion
     */
    public PaymentResponseDTO paymentToPaymentDto(Payment payment) {
        if (payment == null) {
            return null;
        }

        PaymentResponseDTO dto = new PaymentResponseDTO();
        dto.setId(payment.getId());
        dto.setSettlementId(payment.getSettlement() != null ? payment.getSettlement().getId() : null);
        dto.setAmount(payment.getAmount());
        dto.setPaymentMethod(payment.getPaymentMethod());
        dto.setPaymentDate(payment.getPaymentDate());
        dto.setNotes(payment.getNotes());

        return dto;
    }
}

