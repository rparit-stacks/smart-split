package com.rps.smartsplit.controller;

import com.rps.smartsplit.config.CustomUserDetail;
import com.rps.smartsplit.dto.GroupRequestDTO;
import com.rps.smartsplit.dto.GroupResponseDTO;
import com.rps.smartsplit.dto.UserResponseDTO;
import com.rps.smartsplit.model.Group;
import com.rps.smartsplit.service.GroupService;
import com.rps.smartsplit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @Autowired
    private UserService userService;

    /**
     * -------------------------------------------------------------------
     * POST /api/groups
     * -------------------------------------------------------------------
     * Creates a new group.
     * - Accepts group name and description.
     * - The logged-in user is automatically added as the first admin/member.
     **/

    @PostMapping
    public ResponseEntity<GroupResponseDTO> createGroup(@RequestBody GroupRequestDTO groupRequestDTO){
        
        GroupResponseDTO createdGroup = groupService.createGroup(groupRequestDTO);
        return ResponseEntity.ok(createdGroup);
    }

    /**
     * -------------------------------------------------------------------
     * GET /api/groups/{id}
     * -------------------------------------------------------------------
     * Fetches complete group details.
     * - Returns group information such as name, image, and description.
     * - Includes the list of all members belonging to the group.
     */
    @GetMapping("/{id}")
    public ResponseEntity<GroupResponseDTO> getGroup(@PathVariable UUID id) {
        return ResponseEntity.ok(groupService.getGroupById(id));
    }

    /**
     * -------------------------------------------------------------------
     * PUT /api/groups/{id}
     * -------------------------------------------------------------------
     * Updates existing group details.
     * - Allows editing of group name, description, or group icon.
     */
    @PutMapping("/{id}")
    public ResponseEntity<GroupResponseDTO> updateGroup(@PathVariable UUID id, @RequestBody GroupRequestDTO groupRequestDTO) {
        return ResponseEntity.ok(groupService.updateGroup(id, groupRequestDTO));
    }

    /**
     * -------------------------------------------------------------------
     * DELETE /api/groups/{id}
     * -------------------------------------------------------------------
     * Permanently deletes the group.
     * - Allowed only when there are no unpaid or pending expenses in the group.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGroup(@PathVariable UUID id) {
        groupService.deleteGroup(id);
        return ResponseEntity.ok("Group deleted successfully");
    }

    /**
     * -------------------------------------------------------------------
     * POST /api/groups/{id}/members
     * -------------------------------------------------------------------
     * Adds a new member to the group.
     * - Member is added using their registered email address.
     */
    @PostMapping("/{id}/members")
    public ResponseEntity<GroupResponseDTO> addMember(@PathVariable UUID id, @RequestParam String email) {
        return ResponseEntity.ok(groupService.addMember(id, email));
    }

    /**
     * -------------------------------------------------------------------
     * DELETE /api/groups/{id}/members/{userId}
     * -------------------------------------------------------------------
     * Removes a member from the group.
     * - Removal is allowed only if the user has settled all outstanding debts.
     */
    @DeleteMapping("/{id}/members/{userId}")
    public ResponseEntity<GroupResponseDTO> removeMember(@PathVariable UUID id, @PathVariable UUID userId) {
        return ResponseEntity.ok(groupService.removeMember(id, userId));
    }

    /**
     * -------------------------------------------------------------------
     * POST /api/groups/{id}/leave
     * -------------------------------------------------------------------
     * Allows the currently authenticated user to leave the group voluntarily.
     * - User can leave only after clearing their pending balances.
     */
    @PostMapping("/{id}/leave")
    public ResponseEntity<String> leaveGroup(@PathVariable UUID id) {
        UUID currentUserId = userService.getLoggedInUser().getId();
        groupService.leaveGroup(id, currentUserId);
        return ResponseEntity.ok("Left group successfully");
    }



























}
