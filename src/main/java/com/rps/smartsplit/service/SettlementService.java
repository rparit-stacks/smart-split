package com.rps.smartsplit.service;

import com.rps.smartsplit.dto.request.SettlementRequestDTO;
import com.rps.smartsplit.dto.response.SettlementResponseDTO;
import com.rps.smartsplit.model.Group;
import com.rps.smartsplit.model.Settlement;
import com.rps.smartsplit.model.User;
import com.rps.smartsplit.repository.SettlementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SettlementService {

    @Autowired
    private SettlementRepository settlementRepository;

    @Autowired
    @Lazy
    private GroupService groupService;

    @Autowired
    private UserService userService;

    /**
     * Get all settlements by group ID
     * Following SRP - SettlementService handles settlement-related queries
     */
    public <T> List<T> getSettlementsByGroupId(UUID groupId, Class<T> type) {

        List<Settlement> allSettlements = settlementRepository.findAll();
        List<T> result = new ArrayList<>();

        for (Settlement settlement : allSettlements) {
            if (settlement.getGroup() != null &&
                    settlement.getGroup().getId().equals(groupId)) {

                if (type.equals(Settlement.class)) {
                    result.add(type.cast(settlement));

                } else if (type.equals(SettlementResponseDTO.class)) {
                    result.add(type.cast(settlementToSettlementDto(settlement)));
                }
            }
        }
        return result;
    }



    /**
     * Get settlement by ID
     * Following SRP - SettlementService handles settlement queries
     */
    public Settlement getSettlementById(UUID settlementId) {
        return settlementRepository.findById(settlementId)
                .orElseThrow(() -> new IllegalArgumentException("Settlement not found"));
    }

    /**
     * Update an existing settlement
     * Following SRP - SettlementService handles settlement updates
     * 
     * This method allows updating settlement details like amount, description, or date.
     * Useful when a payment was recorded incorrectly and needs correction.
     */
    @Transactional
    public SettlementResponseDTO updateSettlement(UUID settlementId, SettlementRequestDTO settlementRequestDTO) {
        if (settlementRequestDTO == null) {
            throw new IllegalArgumentException("Settlement request must not be null");
        }

        // Get existing settlement
        Settlement existingSettlement = getSettlementById(settlementId);

        // Validate required fields
        if (settlementRequestDTO.getGroupId() == null) {
            throw new IllegalArgumentException("Group ID is required");
        }
        if (settlementRequestDTO.getPayerId() == null) {
            throw new IllegalArgumentException("Payer ID is required");
        }
        if (settlementRequestDTO.getPayeeId() == null) {
            throw new IllegalArgumentException("Payee ID is required");
        }
        if (settlementRequestDTO.getAmount() == null || settlementRequestDTO.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (settlementRequestDTO.getPayerId().equals(settlementRequestDTO.getPayeeId())) {
            throw new IllegalArgumentException("Payer and payee cannot be the same user");
        }

        // Get entities
        Group group = groupService.getAGroupById(settlementRequestDTO.getGroupId());
        User payer = userService.getUserByUserId(settlementRequestDTO.getPayerId());
        User payee = userService.getUserByUserId(settlementRequestDTO.getPayeeId());

        // Update settlement fields
        existingSettlement.setGroup(group);
        existingSettlement.setPayer(payer);
        existingSettlement.setPayee(payee);
        existingSettlement.setAmount(settlementRequestDTO.getAmount());
        existingSettlement.setDescription(settlementRequestDTO.getDescription());
        
        // Update settlement date (use provided date or keep existing)
        if (settlementRequestDTO.getSettlementDate() != null) {
            existingSettlement.setSettlementDate(settlementRequestDTO.getSettlementDate());
        }

        // Save updated settlement
        Settlement updatedSettlement = settlementRepository.save(existingSettlement);

        // Convert to DTO and return
        return settlementToSettlementDto(updatedSettlement);
    }

    /**
     * Delete a settlement
     * Following SRP - SettlementService handles settlement deletion
     * 
     * This method removes a settlement record. Should be used carefully as it
     * removes payment history. Typically used for:
     * - Removing duplicate entries
     * - Removing incorrect settlements (wrong group/people)
     * - Admin actions for fraud cases
     */
    @Transactional
    public void deleteSettlement(UUID settlementId) {
        Settlement settlement = getSettlementById(settlementId);
        settlementRepository.delete(settlement);
    }

    /**
     * Get all settlements with optional filters
     * Following SRP - SettlementService handles settlement queries
     */
    public List<SettlementResponseDTO> getSettlements(UUID groupId, UUID userId, String startDate, String endDate) {
        List<Settlement> allSettlements = settlementRepository.findAll();
        List<SettlementResponseDTO> filteredSettlements = new ArrayList<>();

        for (Settlement settlement : allSettlements) {
            boolean matches = true;

            // Filter by group
            if (groupId != null && (settlement.getGroup() == null || !settlement.getGroup().getId().equals(groupId))) {
                matches = false;
            }

            // Filter by user (payer or payee)
            if (userId != null) {
                boolean isPayer = settlement.getPayer() != null && settlement.getPayer().getId().equals(userId);
                boolean isPayee = settlement.getPayee() != null && settlement.getPayee().getId().equals(userId);
                
                if (!isPayer && !isPayee) {
                    matches = false;
                }
            }

            // Filter by date range
            if (startDate != null && settlement.getSettlementDate() != null) {
                Instant start = Instant.parse(startDate);
                if (settlement.getSettlementDate().isBefore(start)) {
                    matches = false;
                }
            }

            if (endDate != null && settlement.getSettlementDate() != null) {
                Instant end = Instant.parse(endDate);
                if (settlement.getSettlementDate().isAfter(end)) {
                    matches = false;
                }
            }

            if (matches) {
                filteredSettlements.add(settlementToSettlementDto(settlement));
            }
        }

        return filteredSettlements;
    }

    /**
     * Get settlements by group ID
     * Following SRP - SettlementService handles settlement queries
     */
    public List<SettlementResponseDTO> getSettlementsByGroup(UUID groupId) {
        List<Settlement> allSettlements = settlementRepository.findAll();
        List<SettlementResponseDTO> groupSettlements = new ArrayList<>();

        for (Settlement settlement : allSettlements) {
            if (settlement.getGroup() != null && settlement.getGroup().getId().equals(groupId)) {
                groupSettlements.add(settlementToSettlementDto(settlement));
            }
        }

        return groupSettlements;
    }

    /**
     * Get settlements by user ID (where user is payer or payee)
     * Following SRP - SettlementService handles settlement queries
     */
    public List<SettlementResponseDTO> getSettlementsByUser(UUID userId) {
        List<Settlement> allSettlements = settlementRepository.findAll();
        List<SettlementResponseDTO> userSettlements = new ArrayList<>();

        for (Settlement settlement : allSettlements) {
            boolean isPayer = settlement.getPayer() != null && settlement.getPayer().getId().equals(userId);
            boolean isPayee = settlement.getPayee() != null && settlement.getPayee().getId().equals(userId);

            if (isPayer || isPayee) {
                userSettlements.add(settlementToSettlementDto(settlement));
            }
        }

        return userSettlements;
    }

    /**
     * Create multiple settlements in bulk
     * Following SRP - SettlementService handles bulk settlement creation
     * 
     * This method allows creating multiple settlements at once. Useful when:
     * - Multiple payments happened at the same time
     * - Batch import of settlement data
     * - Recording multiple settlements from a single transaction
     */
    @Transactional
    public List<SettlementResponseDTO> createBulkSettlements(List<SettlementRequestDTO> settlementRequestDTOs) {
        if (settlementRequestDTOs == null || settlementRequestDTOs.isEmpty()) {
            throw new IllegalArgumentException("Settlement list must not be empty");
        }

        List<SettlementResponseDTO> createdSettlements = new ArrayList<>();

        for (SettlementRequestDTO settlementRequestDTO : settlementRequestDTOs) {
            SettlementResponseDTO createdSettlement = createSettlement(settlementRequestDTO);
            createdSettlements.add(createdSettlement);
        }

        return createdSettlements;
    }

    /**
     * Create a new settlement/payment
     * Following SRP - SettlementService handles settlement creation
     * 
     * This method records when one user pays another user to settle a debt.
     * For example: User A owes User B $50, User A pays User B $30 cash.
     * This settlement reduces the debt from $50 to $20.
     */
    @Transactional
    public SettlementResponseDTO createSettlement(SettlementRequestDTO settlementRequestDTO) {
        if (settlementRequestDTO == null) {
            throw new IllegalArgumentException("Settlement request must not be null");
        }

        // Validate required fields
        if (settlementRequestDTO.getGroupId() == null) {
            throw new IllegalArgumentException("Group ID is required");
        }
        if (settlementRequestDTO.getPayerId() == null) {
            throw new IllegalArgumentException("Payer ID is required");
        }
        if (settlementRequestDTO.getPayeeId() == null) {
            throw new IllegalArgumentException("Payee ID is required");
        }
        if (settlementRequestDTO.getAmount() == null || settlementRequestDTO.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (settlementRequestDTO.getPayerId().equals(settlementRequestDTO.getPayeeId())) {
            throw new IllegalArgumentException("Payer and payee cannot be the same user");
        }

        // Get entities
        Group group = groupService.getAGroupById(settlementRequestDTO.getGroupId());
        User payer = userService.getUserByUserId(settlementRequestDTO.getPayerId());
        User payee = userService.getUserByUserId(settlementRequestDTO.getPayeeId());

        // Create settlement entity
        Settlement settlement = new Settlement();
        settlement.setGroup(group);
        settlement.setPayer(payer);
        settlement.setPayee(payee);
        settlement.setAmount(settlementRequestDTO.getAmount());
        settlement.setDescription(settlementRequestDTO.getDescription());
        
        // Set settlement date (use provided date or current time)
        if (settlementRequestDTO.getSettlementDate() != null) {
            settlement.setSettlementDate(settlementRequestDTO.getSettlementDate());
        } else {
            settlement.setSettlementDate(Instant.now());
        }

        // Save settlement
        Settlement savedSettlement = settlementRepository.save(settlement);

        // Convert to DTO and return
        return settlementToSettlementDto(savedSettlement);
    }

    //Settlement To DTO
    public SettlementResponseDTO settlementToSettlementDto(Settlement settlement) {
        if (settlement == null) {
            return null;
        }

        SettlementResponseDTO dto = new SettlementResponseDTO();
        dto.setId(settlement.getId());
        dto.setGroupId(settlement.getGroup() != null ? settlement.getGroup().getId() : null);
        dto.setPayerId(settlement.getPayer() != null ? settlement.getPayer().getId() : null);
        dto.setPayeeId(settlement.getPayee() != null ? settlement.getPayee().getId() : null);
        dto.setAmount(settlement.getAmount());
        dto.setDescription(settlement.getDescription());
        dto.setSettlementDate(settlement.getSettlementDate());
        dto.setCreatedAt(settlement.getCreatedAt());
        return dto;
    }

}

