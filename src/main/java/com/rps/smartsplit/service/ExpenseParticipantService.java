package com.rps.smartsplit.service;

import com.rps.smartsplit.dto.request.ExpenseParticipantRequestDTO;
import com.rps.smartsplit.dto.response.ExpenseParticipantResponseDTO;
import com.rps.smartsplit.model.Expense;
import com.rps.smartsplit.model.ExpenseParticipant;
import com.rps.smartsplit.model.SplitType;
import com.rps.smartsplit.model.User;
import com.rps.smartsplit.repository.ExpenseParticipantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ExpenseParticipantService {

    @Autowired
    private ExpenseParticipantRepository expenseParticipantRepository;

    @Autowired
    private UserService userService;


    public List<ExpenseParticipant> saveParticipantForExpense(
            List<ExpenseParticipantRequestDTO> participantDTOs,
            Expense expense
    ) {
        if (expense == null) {
            throw new IllegalArgumentException("Expense must not be null");
        }

        if (participantDTOs == null || participantDTOs.isEmpty()) {
            throw new IllegalArgumentException("Participants list must not be empty");
        }

        SplitType splitType = expense.getSplitType();
        BigDecimal totalAmount = expense.getAmount();
        int participantCount = participantDTOs.size();

        if (splitType == null) {
            throw new IllegalArgumentException("Split type must be provided for an expense");
        }

        List<ExpenseParticipant> participants = new ArrayList<>();

        switch (splitType) {
            case EQUAL -> {
                if (totalAmount == null) {
                    throw new IllegalArgumentException("Total amount must be provided for equal split");
                }
                BigDecimal equalShare = totalAmount
                        .divide(BigDecimal.valueOf(participantCount), 2, RoundingMode.HALF_UP);

                for (ExpenseParticipantRequestDTO dto : participantDTOs) {
                    participants.add(buildParticipant(expense, dto, equalShare));
                }
            }
            case EXACT -> {
                BigDecimal sum = BigDecimal.ZERO;
                for (ExpenseParticipantRequestDTO dto : participantDTOs) {
                    if (dto.getAmount() == null) {
                        throw new IllegalArgumentException("Amount must be provided for each participant in EXACT split");
                    }
                    sum = sum.add(dto.getAmount());
                }
                if (totalAmount != null && sum.compareTo(totalAmount) != 0) {
                    throw new IllegalArgumentException("Sum of participant amounts must equal total expense amount");
                }

                for (ExpenseParticipantRequestDTO dto : participantDTOs) {
                    participants.add(buildParticipant(expense, dto, dto.getAmount()));
                }
            }
            case PERCENTAGE -> {
                if (totalAmount == null) {
                    throw new IllegalArgumentException("Total amount must be provided for percentage split");
                }

                BigDecimal hundred = BigDecimal.valueOf(100);
                BigDecimal totalPercentage = BigDecimal.ZERO;
                for (ExpenseParticipantRequestDTO dto : participantDTOs) {
                    if (dto.getPercentage() == null) {
                        throw new IllegalArgumentException("Percentage must be provided for each participant in PERCENTAGE split");
                    }
                    totalPercentage = totalPercentage.add(dto.getPercentage());
                }

                if (totalPercentage.compareTo(hundred) != 0) {
                    throw new IllegalArgumentException("Total percentage for all participants must be 100");
                }

                for (ExpenseParticipantRequestDTO dto : participantDTOs) {
                    BigDecimal share = totalAmount
                            .multiply(dto.getPercentage())
                            .divide(hundred, 2, RoundingMode.HALF_UP);
                    participants.add(buildParticipant(expense, dto, share));
                }
            }
            default -> throw new IllegalStateException("Unhandled split type: " + splitType);
        }

        return expenseParticipantRepository.saveAll(participants);
    }

    private ExpenseParticipant buildParticipant(
            Expense expense,
            ExpenseParticipantRequestDTO dto,
            BigDecimal amount
    ) {
        if (dto.getUserId() == null) {
            throw new IllegalArgumentException("User id must be provided for each participant");
        }

        User user = userService.getUserByUserId(dto.getUserId());

        ExpenseParticipant participant = new ExpenseParticipant();
        participant.setExpense(expense);
        participant.setUser(user);
        participant.setAmount(amount);
        participant.setPaid(dto.isPaid());
        return participant;
    }

    /**
     * Delete all participants for an expense
     * Following SRP - ExpenseParticipantService handles participant deletion
     */
    @Transactional
    public void deleteParticipantsByExpenseId(UUID expenseId) {
        List<ExpenseParticipant> participants = expenseParticipantRepository.findAll();
        List<ExpenseParticipant> toDelete = new ArrayList<>();
        
        for (ExpenseParticipant participant : participants) {
            if (participant.getExpense() != null && participant.getExpense().getId().equals(expenseId)) {
                toDelete.add(participant);
            }
        }
        
        expenseParticipantRepository.deleteAll(toDelete);
    }

    /**
     * Mark all participants as paid for an expense
     * Following SRP - ExpenseParticipantService handles participant status updates
     */
    @Transactional
    public void markAllParticipantsAsPaid(UUID expenseId) {
        List<ExpenseParticipant> participants = expenseParticipantRepository.findAll();
        
        for (ExpenseParticipant participant : participants) {
            if (participant.getExpense() != null && participant.getExpense().getId().equals(expenseId)) {
                participant.setPaid(true);
                expenseParticipantRepository.save(participant);
            }
        }
    }

    /**
     * Get all participants for an expense
     * Following SRP - ExpenseParticipantService handles participant queries
     */
    public List<ExpenseParticipantResponseDTO> getParticipantsByExpenseId(UUID expenseId) {
        List<ExpenseParticipant> allParticipants = expenseParticipantRepository.findAll();
        List<ExpenseParticipant> expenseParticipants = new ArrayList<>();
        
        for (ExpenseParticipant participant : allParticipants) {
            if (participant.getExpense() != null && participant.getExpense().getId().equals(expenseId)) {
                expenseParticipants.add(participant);
            }
        }
        
        return convertToDtoList(expenseParticipants);
    }

    /**
     * Update participant status (paid/unpaid)
     * Following SRP - ExpenseParticipantService handles participant updates
     */
    @Transactional
    public ExpenseParticipantResponseDTO updateParticipantStatus(UUID participantId, boolean isPaid) {
        ExpenseParticipant participant = expenseParticipantRepository.findById(participantId)
                .orElseThrow(() -> new IllegalArgumentException("Participant not found"));
        
        participant.setPaid(isPaid);
        ExpenseParticipant updated = expenseParticipantRepository.save(participant);
        
        return convertToDto(updated);
    }

    /**
     * Update participant amount and recalculate if needed
     * Following SRP - ExpenseParticipantService handles participant updates
     */
    @Transactional
    public ExpenseParticipantResponseDTO updateParticipantAmount(UUID participantId, BigDecimal amount) {
        ExpenseParticipant participant = expenseParticipantRepository.findById(participantId)
                .orElseThrow(() -> new IllegalArgumentException("Participant not found"));
        
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        
        participant.setAmount(amount);
        ExpenseParticipant updated = expenseParticipantRepository.save(participant);
        
        return convertToDto(updated);
    }

    /**
     * Remove participant from expense
     * Following SRP - ExpenseParticipantService handles participant deletion
     */
    @Transactional
    public void removeParticipant(UUID participantId) {
        ExpenseParticipant participant = expenseParticipantRepository.findById(participantId)
                .orElseThrow(() -> new IllegalArgumentException("Participant not found"));
        
        expenseParticipantRepository.delete(participant);
    }

    /**
     * Convert ExpenseParticipant to DTO
     * Following SRP - ExpenseParticipantService handles DTO conversion
     */
    private ExpenseParticipantResponseDTO convertToDto(ExpenseParticipant participant) {
        if (participant == null) {
            return null;
        }
        
        ExpenseParticipantResponseDTO dto = new ExpenseParticipantResponseDTO();
        dto.setId(participant.getId());
        dto.setExpenseId(participant.getExpense() != null ? participant.getExpense().getId() : null);
        dto.setUserId(participant.getUser() != null ? participant.getUser().getId() : null);
        dto.setAmount(participant.getAmount());
        dto.setPaid(participant.isPaid());
        
        return dto;
    }

    /**
     * Convert list of participants to DTOs
     * Following SRP - ExpenseParticipantService handles DTO conversion
     */
    private List<ExpenseParticipantResponseDTO> convertToDtoList(List<ExpenseParticipant> participants) {
        List<ExpenseParticipantResponseDTO> dtos = new ArrayList<>();
        for (ExpenseParticipant participant : participants) {
            dtos.add(convertToDto(participant));
        }
        return dtos;
    }
}
