package com.rps.smartsplit.controller;

import com.rps.smartsplit.dto.request.GroupRequestDTO;
import com.rps.smartsplit.dto.response.GroupResponseDTO;
import com.rps.smartsplit.dto.response.UserResponseDTO;
import com.rps.smartsplit.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

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
        groupService.leaveGroup(id);
        return ResponseEntity.ok("Left group successfully");
    }

    /**
     * GET /api/groups
     * List all groups (for current user)
     */
    @GetMapping
    public ResponseEntity<?> getAllGroups() {
        try {
            List<GroupResponseDTO> groups = groupService.getAllGroups();

            if (groups.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

            return ResponseEntity.ok(groups);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch groups");
        }
    }


    /**
     * GET /api/groups/{id}/expenses
     * Get group expenses (list all expenses in group)
     */
    @GetMapping("/{id}/expenses")
    public ResponseEntity<?> getGroupExpenses(@PathVariable UUID id) {
        try {
            List<?> expenses = groupService.getExpensesByGroupId(id);

            if (expenses == null || expenses.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

            return ResponseEntity.ok(expenses);

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch group expenses");
        }
    }


    /**
     * GET /api/groups/{id}/balances
     * Get group balances (show who owes whom in this group)
     */
    @GetMapping("/{id}/balances")
    public ResponseEntity<?> getGroupBalances(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(
                    groupService.getGroupBalancesWithSettlements(id)
            );
    
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
    
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch group balances");
        }
    }
    

    /**
     * GET /api/groups/{id}/members
     * Get group members (list all members with their details)
     */
    @GetMapping("/{id}/members")
    public ResponseEntity<?> getGroupMembers(@PathVariable UUID id) {
        try {
            List<UserResponseDTO> members = groupService.getGroupMembers(id);

            if (members == null || members.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }

            return ResponseEntity.ok(members);

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Group not found");

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch group members");
        }
    }


    /**
     * GET /api/groups/{id}/settlements
     * Get group settlements (payment history within group)
     */
    @GetMapping("/{id}/settlements")
    public ResponseEntity<?> getGroupSettlements(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(
                    groupService.getSettlementsByGroupId(id)
            );

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch group settlements");
        }
    }


    /**
     * GET /api/groups/{id}/summary
     * Get group summary (total expenses, balances, recent activity)
     */
    @GetMapping("/{id}/summary")
    public ResponseEntity<?> getGroupSummary(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(groupService.getGroupSummary(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch group summary");
        }
    }

}
