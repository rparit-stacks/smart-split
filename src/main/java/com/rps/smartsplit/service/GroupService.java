package com.rps.smartsplit.service;

import com.rps.smartsplit.dto.*;
import com.rps.smartsplit.model.Expense;
import com.rps.smartsplit.model.ExpenseParticipant;
import com.rps.smartsplit.model.Group;
import com.rps.smartsplit.model.Settlement;
import com.rps.smartsplit.model.User;
import com.rps.smartsplit.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserService userService;
    
    @Autowired
    private ExpenseService expenseService;
    
    @Autowired
    private SettlementService settlementService;

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

        group.addUser(creator);
        return saveGroupAndUser(group, creator);
    }

    @Transactional
    public GroupResponseDTO updateGroup(UUID groupId, GroupRequestDTO groupRequestDTO) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException("Group not found"));
        group.setName(groupRequestDTO.getName());
        group.setDescription(groupRequestDTO.getDescription());
        group.setProfileUrl(groupRequestDTO.getProfileUrl());
        Group updatedGroup = groupRepository.save(group);
        return groupToGroupDto(updatedGroup);
    }

    @Transactional
    public void deleteGroup(UUID groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException("Group not found"));
        groupRepository.delete(group);
    }

    @Transactional
    public GroupResponseDTO addMember(UUID groupId, String email) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException("Group not found"));
        User user = userService.getUserByEmail(email);
        if(user == null){
            throw new IllegalArgumentException("User not found");
        }

        // Check if user is already a member
        if (group.getUsers().contains(user)) {
            throw new IllegalArgumentException("User is already a member of the group");
        }

        group.addUser(user);
        return saveGroupAndUser(group, user);
    }

    @Transactional
    public GroupResponseDTO removeMember(UUID groupId, UUID userId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException("Group not found"));

        User user = userService.getUserByUserId(userId);
        if(user == null){
            throw new IllegalArgumentException("User not found");
        }

        // Check if user is a member of the group
        if (!group.getUsers().contains(user)) {
            throw new IllegalArgumentException("User is not a member of the group");
        }

        group.removeUser(user);
        return saveGroupAndUser(group, user);
    }

    @Transactional
    public void leaveGroup(UUID groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException("Group not found"));
        User user = userService.getLoggedInUser();

        if(user == null){
            throw new IllegalArgumentException("User not found");
        }

//        //if logged-in user same as the param user id
//        if(!user.getId().equals(userId)){
//            SecurityContextHolder.clearContext();
//            throw new IllegalArgumentException("You are not the logged in user, please login again");
//        }

        if (!group.getUsers().contains(user)) {
            throw new IllegalArgumentException("User is not a member of the group");
        }
        group.removeUser(user);
        saveGroupAndUser(group, user);
    }

    /**
     * Converts Group entity to GroupResponseDTO
     * Following Single Responsibility Principle - GroupService handles Group-related conversions
     */
    public GroupResponseDTO groupToGroupDto(Group group) {
        if (group == null) {
            return null;
        }

        GroupResponseDTO dto = new GroupResponseDTO();
        dto.setId(group.getId());
        dto.setName(group.getName());
        dto.setCreatedBy(group.getCreatedUser() != null ? group.getCreatedUser().getId() : null);
        dto.setDescription(group.getDescription());
        dto.setProfileUrl(group.getProfileUrl());
        dto.setCreatedAt(group.getCreatedAt());
        dto.setUpdatedAt(group.getUpdatedAt());

        // 🔥 users via service (NO stream, NO lazy issue)
        dto.setMembers(userService.getUserListByGroupId(group.getId()));
        return dto;
    }

    public GroupResponseDTO getGroupById(UUID groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException("Group not found"));
        return groupToGroupDto(group);
    }

    private GroupResponseDTO saveGroupAndUser(Group group, User user) {
        Group savedGroup = groupRepository.save(group);
        userService.saveUser(user);
        return groupToGroupDto(savedGroup);
    }

    public List<GroupResponseDTO> getAllGroups() {
        List<Group> group = groupRepository.findAll();
        List<GroupResponseDTO> allGroups = new ArrayList<>();

        for(Group g : group){
            allGroups.add(groupToGroupDto(g));
        }
      return allGroups;
    }

    public List<?> getExpensesByGroupId(UUID id) {
        Group group = groupRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Group not found"));

        if (group.getExpenses().isEmpty()){
            return new ArrayList<>();
        }
        List<ExpenseResponseDTO> expenseList = new ArrayList<>();

        for(Expense e : group.getExpenses()){
            expenseList.add(expenseService.expenseToExpenseDto(e));
        }
        return expenseList;
    }

    /**
     * Get group balances (expense-based only)
     * Following SRP - GroupService handles group balance calculations
     */
    public GroupBalancesResponseDTO getGroupBalances(UUID groupId) {
        Group group = groupRepository.findById(groupId)
            .orElseThrow(() -> new IllegalArgumentException("Group not found"));

        // Get expenses via service
        List<Expense> expenses = expenseService.getExpensesByGroupId(groupId);

        // Calculate balances from expenses
        List<BalanceDTO> balances = calculateExpenseBalances(expenses);

        // Simplify balances (net out)
        List<BalanceDTO> simplified = simplifyBalances(balances);

        // Calculate summary
        GroupBalanceSummaryDTO summary = calculateSummary(simplified);

        // Calculate per-user summaries
        List<UserBalanceDTO> userSummaries = calculateUserSummaries(simplified);

        // Build response
        GroupBalancesResponseDTO response = new GroupBalancesResponseDTO();
        response.setGroupId(groupId);
        response.setGroupName(group.getName());
        response.setBalances(simplified);
        response.setSummary(summary);
        response.setUserSummaries(userSummaries);

        return response;
    }

    /**
     * Get group balances with settlements applied (current net balances)
     */
    public GroupBalancesResponseDTO getGroupBalancesWithSettlements(UUID groupId) {
        Group group = groupRepository.findById(groupId)
            .orElseThrow(() -> new IllegalArgumentException("Group not found"));

        // Get expenses via service
        List<Expense> expenses = expenseService.getExpensesByGroupId(groupId);
        List<BalanceDTO> expenseBalances = calculateExpenseBalances(expenses);

        // Get settlements via service
        List<Settlement> settlements = settlementService.getSettlementsByGroupId(groupId, Settlement.class);

        // Apply settlements
        List<BalanceDTO> adjustedBalances = applySettlements(expenseBalances, settlements);

        // Simplify
        List<BalanceDTO> simplified = simplifyBalances(adjustedBalances);

        // Calculate summary
        GroupBalanceSummaryDTO summary = calculateSummary(simplified);

        // Calculate per-user summaries
        List<UserBalanceDTO> userSummaries = calculateUserSummaries(simplified);

        // Build response
        GroupBalancesResponseDTO response = new GroupBalancesResponseDTO();
        response.setGroupId(groupId);
        response.setGroupName(group.getName());
        response.setBalances(simplified);
        response.setSummary(summary);
        response.setUserSummaries(userSummaries);

        return response;
    }   

    /**
     * Calculate balances from expenses
     * Following SRP - Single responsibility: calculate expense-based balances
     */
    private List<BalanceDTO> calculateExpenseBalances(List<Expense> expenses) {
        List<BalanceDTO> balances = new ArrayList<>();

        for (Expense expense : expenses) {
            User paidBy = expense.getPaidBy();
            List<ExpenseParticipant> participants = expense.getExpenseParticipants();

            // If participants are null or empty, skip
            if (participants == null || participants.isEmpty()) {
                continue;
            }

            for (ExpenseParticipant participant : participants) {
                User participantUser = participant.getUser();
                BigDecimal shareAmount = participant.getAmount();

                // If participant is NOT the one who paid
                if (participantUser != null && paidBy != null && 
                    !participantUser.getId().equals(paidBy.getId())) {
                    
                    BalanceDTO balance = new BalanceDTO();
                    balance.setFromUserId(participantUser.getId());
                    balance.setFromUserName(participantUser.getName());
                    balance.setToUserId(paidBy.getId());
                    balance.setToUserName(paidBy.getName());
                    balance.setAmount(shareAmount);

                    balances.add(balance);
                }
            }
        }

        return balances;
    }

    /**
     * Apply settlements to balances
     * Following SRP - Single responsibility: adjust balances with settlements
     */
    private List<BalanceDTO> applySettlements(
            List<BalanceDTO> balances,
            List<Settlement> settlements) {

        List<BalanceDTO> adjustedBalances = new ArrayList<>(balances);

        for (Settlement settlement : settlements) {
            User payer = settlement.getPayer();
            User payee = settlement.getPayee();
            BigDecimal settlementAmount = settlement.getAmount();

            if (payer == null || payee == null || settlementAmount == null) {
                continue;
            }

            // Find existing balance from payer to payee
            BalanceDTO existingBalance = findBalance(
                adjustedBalances,
                payer.getId(),
                payee.getId()
            );

            if (existingBalance != null) {
                // Reduce the debt
                BigDecimal newAmount = existingBalance.getAmount().subtract(settlementAmount);

                if (newAmount.compareTo(BigDecimal.ZERO) > 0) {
                    // Still owes something
                    existingBalance.setAmount(newAmount);
                } else if (newAmount.compareTo(BigDecimal.ZERO) < 0) {
                    // Overpaid - reverse direction
                    adjustedBalances.remove(existingBalance);

                    BalanceDTO reverseBalance = new BalanceDTO();
                    reverseBalance.setFromUserId(payee.getId());
                    reverseBalance.setFromUserName(payee.getName());
                    reverseBalance.setToUserId(payer.getId());
                    reverseBalance.setToUserName(payer.getName());
                    reverseBalance.setAmount(newAmount.abs());
                    adjustedBalances.add(reverseBalance);
                } else {
                    // Fully paid - remove
                    adjustedBalances.remove(existingBalance);
                }
            } else {
                // No existing balance - settlement creates reverse balance
                BalanceDTO reverseBalance = new BalanceDTO();
                reverseBalance.setFromUserId(payee.getId());
                reverseBalance.setFromUserName(payee.getName());
                reverseBalance.setToUserId(payer.getId());
                reverseBalance.setToUserName(payer.getName());
                reverseBalance.setAmount(settlementAmount);
                adjustedBalances.add(reverseBalance);
            }
        }

        return adjustedBalances;
    }

    /**
     * Simplify balances by netting out opposite directions
     * Following SRP - Single responsibility: simplify balance matrix
     */
    private List<BalanceDTO> simplifyBalances(List<BalanceDTO> balances) {
        List<BalanceDTO> simplified = new ArrayList<>();
        Set<String> processedPairs = new HashSet<>();

        for (BalanceDTO balance : balances) {
            String pairKey1 = balance.getFromUserId() + "-" + balance.getToUserId();
            String pairKey2 = balance.getToUserId() + "-" + balance.getFromUserId();

            if (processedPairs.contains(pairKey1) || processedPairs.contains(pairKey2)) {
                continue;
            }

            // Find opposite direction balance
            BalanceDTO opposite = findBalance(
                balances,
                balance.getToUserId(),
                balance.getFromUserId()
            );

            if (opposite != null) {
                // Net out
                BigDecimal net = balance.getAmount().subtract(opposite.getAmount());

                if (net.compareTo(BigDecimal.ZERO) > 0) {
                    // Original direction still owes
                    balance.setAmount(net);
                    simplified.add(balance);
                } else if (net.compareTo(BigDecimal.ZERO) < 0) {
                    // Reverse direction
                    BalanceDTO netBalance = new BalanceDTO();
                    netBalance.setFromUserId(balance.getToUserId());
                    netBalance.setFromUserName(balance.getToUserName());
                    netBalance.setToUserId(balance.getFromUserId());
                    netBalance.setToUserName(balance.getFromUserName());
                    netBalance.setAmount(net.abs());
                    simplified.add(netBalance);
                }
                // If net is zero, don't add (fully settled)

                processedPairs.add(pairKey1);
                processedPairs.add(pairKey2);
            } else {
                // No opposite, add as is
                simplified.add(balance);
                processedPairs.add(pairKey1);
            }
        }

        // Remove zero balances
        List<BalanceDTO> finalList = new ArrayList<>();
        for (BalanceDTO balance : simplified) {
            if (balance.getAmount().compareTo(BigDecimal.ZERO) != 0) {
                finalList.add(balance);
            }
        }

        return finalList;
    }

    /**
     * Calculate summary totals
     * Following SRP - Single responsibility: calculate summary statistics
     */
    private GroupBalanceSummaryDTO calculateSummary(List<BalanceDTO> balances) {
        BigDecimal totalOwing = BigDecimal.ZERO;

        for (BalanceDTO balance : balances) {
            totalOwing = totalOwing.add(balance.getAmount());
        }

        // Total owed is same as total owing (money is conserved)
        BigDecimal totalOwed = totalOwing;
        BigDecimal netBalance = totalOwed.subtract(totalOwing);

        GroupBalanceSummaryDTO summary = new GroupBalanceSummaryDTO();
        summary.setTotalOwed(totalOwed);
        summary.setTotalOwing(totalOwing);
        summary.setNetBalance(netBalance);

        return summary;
    }

    /**
     * Calculate per-user summaries
     * Following SRP - Single responsibility: calculate user-level summaries
     */
    private List<UserBalanceDTO> calculateUserSummaries(List<BalanceDTO> balances) {
        List<UserBalanceDTO> userSummaries = new ArrayList<>();

        // Get all unique user IDs
        Set<UUID> userIds = new HashSet<>();
        for (BalanceDTO balance : balances) {
            userIds.add(balance.getFromUserId());
            userIds.add(balance.getToUserId());
        }

        // Calculate totals for each user
        for (UUID userId : userIds) {
            UserBalanceDTO userBalance = new UserBalanceDTO();
            userBalance.setUserId(userId);
            userBalance.setTotalOwed(BigDecimal.ZERO);
            userBalance.setTotalOwing(BigDecimal.ZERO);

            // Find user name
            User user = userService.getUserByUserId(userId);
            if (user != null) {
                userBalance.setUserName(user.getName());
            }

            // Calculate totals
            for (BalanceDTO balance : balances) {
                if (balance.getFromUserId().equals(userId)) {
                    // User is owing
                    userBalance.setTotalOwing(
                        userBalance.getTotalOwing().add(balance.getAmount())
                    );
                }
                if (balance.getToUserId().equals(userId)) {
                    // User is owed
                    userBalance.setTotalOwed(
                        userBalance.getTotalOwed().add(balance.getAmount())
                    );
                }
            }

            // Calculate net balance
            BigDecimal net = userBalance.getTotalOwed().subtract(userBalance.getTotalOwing());
            userBalance.setNetBalance(net);

            userSummaries.add(userBalance);
        }

        return userSummaries;
    }

    /**
     * Helper method to find balance between two users
     */
    private BalanceDTO findBalance(List<BalanceDTO> balances, UUID fromUserId, UUID toUserId) {
        for (BalanceDTO balance : balances) {
            if (balance.getFromUserId().equals(fromUserId) &&
                balance.getToUserId().equals(toUserId)) {
                return balance;
            }
        }
        return null;
    }

    public List<UserResponseDTO> getGroupMembers(UUID id) {
        Group group = groupRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Group not found"));
        return userService.getUserListByGroupId(group.getId());
    }

    public List<SettlementResponseDTO> getSettlementsByGroupId(UUID groupId) {
        List<SettlementResponseDTO> settlementDtos =
                settlementService.getSettlementsByGroupId(groupId, SettlementResponseDTO.class);
        return settlementDtos;
    }

    /**
     * Get group summary (total expenses, balances, recent activity)
     * Following SRP - GroupService handles group summary aggregation
     */
    public GroupSummaryDTO getGroupSummary(UUID groupId) {
        Group group = groupRepository.findById(groupId)
            .orElseThrow(() -> new IllegalArgumentException("Group not found"));

        GroupSummaryDTO summary = new GroupSummaryDTO();
        summary.setGroupId(groupId);
        summary.setGroupName(group.getName());
        summary.setDescription(group.getDescription());

        // Get member count via service
        List<UserResponseDTO> members = userService.getUserListByGroupId(groupId);
        summary.setMemberCount(members.size());

        // Get expenses via service
        List<Expense> expenses = expenseService.getExpensesByGroupId(groupId);
        summary.setExpenseCount(expenses.size());

        // Calculate total expense amount
        BigDecimal totalExpenseAmount = BigDecimal.ZERO;
        for (Expense expense : expenses) {
            if (expense.getAmount() != null) {
                totalExpenseAmount = totalExpenseAmount.add(expense.getAmount());
            }
        }
        summary.setTotalExpenseAmount(totalExpenseAmount);

        // Get settlements via service
        List<Settlement> settlements = settlementService.getSettlementsByGroupId(groupId, Settlement.class);
        
        // Calculate total settled amount
        BigDecimal totalSettledAmount = BigDecimal.ZERO;
        for (Settlement settlement : settlements) {
            if (settlement.getAmount() != null) {
                totalSettledAmount = totalSettledAmount.add(settlement.getAmount());
            }
        }
        summary.setTotalSettledAmount(totalSettledAmount);

        // Get balance summary
        GroupBalancesResponseDTO balances = getGroupBalances(groupId);
        summary.setBalanceSummary(balances.getSummary());

        // Get recent expenses (last 5)
        List<ExpenseResponseDTO> recentExpenses = getRecentExpenses(expenses, 5);
        summary.setRecentExpenses(recentExpenses);

        // Get recent settlements (last 5)
        List<SettlementResponseDTO> recentSettlements = getRecentSettlements(settlements, 5);
        summary.setRecentSettlements(recentSettlements);

        return summary;
    }

    /**
     * Get recent expenses (sorted by date, newest first)
     * Following SRP - Single responsibility: get recent expenses
     */
    private List<ExpenseResponseDTO> getRecentExpenses(List<Expense> expenses, int limit) {
        List<ExpenseResponseDTO> recentExpenses = new ArrayList<>();

        // Sort by createdAt (newest first) - simple bubble sort
        List<Expense> sortedExpenses = new ArrayList<>(expenses);
        for (int i = 0; i < sortedExpenses.size() - 1; i++) {
            for (int j = 0; j < sortedExpenses.size() - i - 1; j++) {
                Expense exp1 = sortedExpenses.get(j);
                Expense exp2 = sortedExpenses.get(j + 1);
                
                Instant date1 = exp1.getCreatedAt() != null ? exp1.getCreatedAt() : exp1.getExpenseDate();
                Instant date2 = exp2.getCreatedAt() != null ? exp2.getCreatedAt() : exp2.getExpenseDate();
                
                if (date1 != null && date2 != null && date1.isBefore(date2)) {
                    sortedExpenses.set(j, exp2);
                    sortedExpenses.set(j + 1, exp1);
                }
            }
        }

        // Get first N expenses
        int count = Math.min(limit, sortedExpenses.size());
        for (int i = 0; i < count; i++) {
            ExpenseResponseDTO dto = expenseService.expenseToExpenseDto(sortedExpenses.get(i));
            recentExpenses.add(dto);
        }

        return recentExpenses;
    }

    /**
     * Get recent settlements (sorted by date, newest first)
     * Following SRP - Single responsibility: get recent settlements
     */
    private List<SettlementResponseDTO> getRecentSettlements(List<Settlement> settlements, int limit) {
        List<SettlementResponseDTO> recentSettlements = new ArrayList<>();

        // Sort by createdAt (newest first) - simple bubble sort
        List<Settlement> sortedSettlements = new ArrayList<>(settlements);
        for (int i = 0; i < sortedSettlements.size() - 1; i++) {
            for (int j = 0; j < sortedSettlements.size() - i - 1; j++) {
                Settlement set1 = sortedSettlements.get(j);
                Settlement set2 = sortedSettlements.get(j + 1);
                
                Instant date1 = set1.getCreatedAt() != null ? set1.getCreatedAt() : set1.getSettlementDate();
                Instant date2 = set2.getCreatedAt() != null ? set2.getCreatedAt() : set2.getSettlementDate();
                
                if (date1 != null && date2 != null && date1.isBefore(date2)) {
                    sortedSettlements.set(j, set2);
                    sortedSettlements.set(j + 1, set1);
                }
            }
        }

        // Get first N settlements
        int count = Math.min(limit, sortedSettlements.size());
        for (int i = 0; i < count; i++) {
            SettlementResponseDTO dto = settlementService.settlementToSettlementDto(sortedSettlements.get(i));
            recentSettlements.add(dto);
        }

        return recentSettlements;
    }
}

