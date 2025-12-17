package com.rps.smartsplit.service;

import com.rps.smartsplit.dto.GroupResponseDTO;
import com.rps.smartsplit.model.Group;
import org.springframework.stereotype.Service;

@Service
public class GroupService {

    /**
     * Converts Group entity to GroupResponseDTO
     * Following Single Responsibility Principle - GroupService handles Group-related conversions
     */
    public GroupResponseDTO groupToGroupDto(Group group) {
        if (group == null) {
            return null;
        }

        GroupResponseDTO groupResponseDTO = new GroupResponseDTO();
        groupResponseDTO.setId(group.getId());
        groupResponseDTO.setName(group.getName());
        groupResponseDTO.setCreatedBy(group.getCreatedBy());
        groupResponseDTO.setDescription(group.getDescription());
        groupResponseDTO.setProfileUrl(group.getProfileUrl());
        groupResponseDTO.setCreatedAt(group.getCreatedAt());
        groupResponseDTO.setUpdatedAt(group.getUpdatedAt());

        return groupResponseDTO;
    }
}

