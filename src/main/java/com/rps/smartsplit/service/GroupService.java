package com.rps.smartsplit.service;

import com.rps.smartsplit.dto.GroupRequestDTO;
import com.rps.smartsplit.dto.GroupResponseDTO;
import com.rps.smartsplit.dto.UserResponseDTO;
import com.rps.smartsplit.model.Group;
import com.rps.smartsplit.model.Role;
import com.rps.smartsplit.model.User;
import com.rps.smartsplit.repository.GroupRepository;
import com.rps.smartsplit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public GroupResponseDTO createGroup(GroupRequestDTO groupRequestDTO) {
        User creator = userService.getLoggedInUser();
        if(creator == null){
            throw new IllegalArgumentException("User not found");
        }

        Group group = new Group();
        group.setName(groupRequestDTO.getName());
        group.setDescription(groupRequestDTO.getDescription());
        group.setProfileUrl(groupRequestDTO.getProfileUrl());
        group.setCreatedBy(creator.getId());
        group.setCreatedAt(Instant.now());
        group.setUpdatedAt(Instant.now());

        // Add creator to the group
        group.getUsers().add(creator);
        creator.getGroups().add(group);

        Group savedGroup = groupRepository.save(group);
        return groupToGroupDto(savedGroup);
    }

    public GroupResponseDTO getGroupById(UUID groupId) {
        return null;
    }

    @Transactional
    public GroupResponseDTO updateGroup(UUID groupId, GroupRequestDTO groupRequestDTO) {
        return null;
    }

    @Transactional
    public void deleteGroup(UUID groupId) {
        
    }

    @Transactional
    public GroupResponseDTO addMember(UUID groupId, String email) {
        return null;
    }

    @Transactional
    public GroupResponseDTO removeMember(UUID groupId, UUID userId) {
        return null;
    }

    @Transactional
    public void leaveGroup(UUID groupId, UUID userId) {
        
    }

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

        // Map members
        if (group.getUsers() != null) {
            List<UserResponseDTO> members = group.getUsers().stream()
                    .map(this::userToUserDto)
                    .collect(Collectors.toList());
            groupResponseDTO.setMembers(members);
        }

        return groupResponseDTO;
    }

    private UserResponseDTO userToUserDto(User user) {
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setId(user.getId());
        userResponseDTO.setName(user.getName());
        userResponseDTO.setAddress(user.getAddress());
        userResponseDTO.setAge(user.getAge());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setPhNumber(user.getPhNumber());
        userResponseDTO.setProfileUrl(user.getProfileUrl());
        userResponseDTO.setRole(user.getRole() != null ? user.getRole().name() : Role.USER.name());
        return userResponseDTO;
    }
}

