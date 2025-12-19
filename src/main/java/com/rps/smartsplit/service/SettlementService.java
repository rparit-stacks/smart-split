package com.rps.smartsplit.service;

import com.rps.smartsplit.dto.SettlementResponseDTO;
import com.rps.smartsplit.model.Settlement;
import com.rps.smartsplit.repository.SettlementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SettlementService {

    @Autowired
    private SettlementRepository settlementRepository;

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



    //Settlement To DTO
    public SettlementResponseDTO settlementToSettlementDto(Settlement settlement) {
        if (settlement == null) {
            return null;
        }

        SettlementResponseDTO dto = new SettlementResponseDTO();
        dto.setId(settlement.getId());
        dto.setGroupId(settlement.getGroup().getId());
        dto.setPayerId(settlement.getPayer().getId());
        dto.setPayeeId(settlement.getPayee().getId());
        dto.setAmount(settlement.getAmount());
        dto.setDescription(settlement.getDescription());
        dto.setSettlementDate(settlement.getSettlementDate());
        dto.setCreatedAt(settlement.getCreatedAt());
        return dto;
    }

}

