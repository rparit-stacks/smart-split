package com.rps.smartsplit.service;

import com.rps.smartsplit.dto.balance.BalanceDTO;
import com.rps.smartsplit.dto.balance.GroupBalancesResponseDTO;
import com.rps.smartsplit.dto.balance.UserBalanceDTO;
import com.rps.smartsplit.dto.balance.GroupBalanceInfoDTO;
import com.rps.smartsplit.dto.balance.UserBalanceSummaryDTO;
import com.rps.smartsplit.dto.balance.UserBalancesResponseDTO;
import com.rps.smartsplit.dto.balance.SimplifiedBalancesResponseDTO;
import com.rps.smartsplit.dto.balance.SettlementSuggestionDTO;
import com.rps.smartsplit.dto.balance.BalanceBetweenUsersResponseDTO;
import com.rps.smartsplit.dto.balance.BalanceMatrixResponseDTO;
import com.rps.smartsplit.dto.balance.BalanceMatrixCellDTO;
import com.rps.smartsplit.model.Group;
import com.rps.smartsplit.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class BalanceService {

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    /**
     * Get all balances for a specific user across all their groups
     * Following SRP - BalanceService handles user balance aggregation
     * 
     * This method aggregates balances from all groups where the user is a member.
     * It shows:
     * - All individual balances (who owes whom)
     * - Summary totals (total owed, total owing, net balance)
     * - Breakdown by group
     */
    public UserBalancesResponseDTO getUserBalances(UUID userId) {
        // Get user
        User user = userService.getUserByUserId(userId);
        if (user == null) {
            throw new RuntimeException("User not found with id: " + userId);
        }

        // Get all groups user is part of
        List<Group> userGroups = new ArrayList<>(user.getGroups());

        // Aggregate all balances
        List<BalanceDTO> allBalances = new ArrayList<>();
        List<GroupBalanceInfoDTO> byGroup = new ArrayList<>();
        BigDecimal totalOwed = BigDecimal.ZERO;
        BigDecimal totalOwing = BigDecimal.ZERO;

        for (Group group : userGroups) {
            // Get group balances with settlements applied
            GroupBalancesResponseDTO groupBalances = groupService.getGroupBalancesWithSettlements(group.getId());

            // Filter balances where user is involved
            List<BalanceDTO> userBalancesInGroup = new ArrayList<>();
            if (groupBalances.getBalances() != null) {
                for (BalanceDTO balance : groupBalances.getBalances()) {
                    if (balance.getFromUserId().equals(userId) || balance.getToUserId().equals(userId)) {
                        userBalancesInGroup.add(balance);
                        allBalances.add(balance);
                    }
                }
            }

            // Get user's balance summary for this group
            UserBalanceDTO userBalanceInGroup = findUserBalance(groupBalances.getUserSummaries(), userId);

            if (userBalanceInGroup != null) {
                totalOwed = totalOwed.add(userBalanceInGroup.getTotalOwed());
                totalOwing = totalOwing.add(userBalanceInGroup.getTotalOwing());

                // Create group balance info
                GroupBalanceInfoDTO groupInfo = new GroupBalanceInfoDTO();
                groupInfo.setGroupId(group.getId());
                groupInfo.setGroupName(group.getName());
                groupInfo.setBalances(userBalancesInGroup);
                groupInfo.setUserBalance(userBalanceInGroup);
                byGroup.add(groupInfo);
            }
        }

        // Calculate net balance
        BigDecimal netBalance = totalOwed.subtract(totalOwing);

        // Create summary
        UserBalanceSummaryDTO summary = new UserBalanceSummaryDTO();
        summary.setUserId(userId);
        summary.setUserName(user.getName());
        summary.setTotalOwed(totalOwing); // User owes this amount
        summary.setTotalOwedToUser(totalOwed); // Others owe this to user
        summary.setNetBalance(netBalance);

        // Build response
        UserBalancesResponseDTO response = new UserBalancesResponseDTO();
        response.setUserId(userId);
        response.setUserName(user.getName());
        response.setBalances(allBalances);
        response.setSummary(summary);
        response.setByGroup(byGroup);

        return response;
    }

    /**
     * Get group balances (who owes whom in the group)
     * Following SRP - BalanceService handles group balance retrieval
     * 
     * This method returns all balances within a specific group, showing:
     * - All individual balances between members (who owes whom, with amounts)
     * - Group summary (total owed, total owing, net balance for the group)
     * - Per-user summaries (each member's total owed, total owing, net balance)
     * 
     * Balances include settlements - so if someone paid, it's already reflected.
     */
    public GroupBalancesResponseDTO getGroupBalances(UUID groupId) {
        // Use GroupService to get balances with settlements applied
        return groupService.getGroupBalancesWithSettlements(groupId);
    }

    /**
     * Get simplified balances with optimized settlement suggestions
     * Following SRP - BalanceService handles debt simplification
     * 
     * This method provides an optimized view of balances with settlement suggestions
     * that minimize the number of transactions needed to settle all debts.
     * 
     * Algorithm:
     * 1. Calculate net balance for each user (what they owe minus what they're owed)
     * 2. Identify creditors (positive net) and debtors (negative net)
     * 3. Match them using a greedy algorithm to minimize transactions
     * 4. Generate settlement suggestions
     */
    public SimplifiedBalancesResponseDTO getSimplifiedBalances(UUID groupId) {
        // Get current balances (already simplified by GroupService)
        GroupBalancesResponseDTO groupBalances = groupService.getGroupBalancesWithSettlements(groupId);
        
        // Get group info
        Group group = groupService.getAGroupById(groupId);
        
        // Calculate net balances for each user
        List<UserBalanceDTO> userSummaries = groupBalances.getUserSummaries();
        List<NetBalanceInfo> netBalances = new ArrayList<>();
        
        for (UserBalanceDTO userBalance : userSummaries) {
            NetBalanceInfo netInfo = new NetBalanceInfo();
            netInfo.userId = userBalance.getUserId();
            netInfo.userName = userBalance.getUserName();
            netInfo.netBalance = userBalance.getNetBalance();
            netBalances.add(netInfo);
        }
        
        // Sort: creditors first (positive net, descending), then debtors (negative net, ascending)
        Collections.sort(netBalances, (a, b) -> {
            // Positive balances (creditors) first, sorted descending
            if (a.netBalance.compareTo(BigDecimal.ZERO) >= 0 && b.netBalance.compareTo(BigDecimal.ZERO) < 0) {
                return -1;
            }
            if (a.netBalance.compareTo(BigDecimal.ZERO) < 0 && b.netBalance.compareTo(BigDecimal.ZERO) >= 0) {
                return 1;
            }
            // Both same type, sort by absolute value descending
            return b.netBalance.abs().compareTo(a.netBalance.abs());
        });
        
        // Generate settlement suggestions using greedy matching
        List<SettlementSuggestionDTO> suggestions = generateSettlementSuggestions(netBalances);
        
        // Build response
        SimplifiedBalancesResponseDTO response = new SimplifiedBalancesResponseDTO();
        response.setGroupId(groupId);
        response.setGroupName(group.getName());
        response.setSimplifiedBalances(groupBalances.getBalances());
        response.setSettlementSuggestions(suggestions);
        response.setSummary(groupBalances.getSummary());
        
        return response;
    }
    
    /**
     * Generate settlement suggestions using greedy algorithm
     * Matches creditors with debtors to minimize transactions
     */
    private List<SettlementSuggestionDTO> generateSettlementSuggestions(List<NetBalanceInfo> netBalances) {
        List<SettlementSuggestionDTO> suggestions = new ArrayList<>();
        List<NetBalanceInfo> creditors = new ArrayList<>();
        List<NetBalanceInfo> debtors = new ArrayList<>();
        
        // Separate creditors and debtors
        for (NetBalanceInfo netInfo : netBalances) {
            if (netInfo.netBalance.compareTo(BigDecimal.ZERO) > 0) {
                creditors.add(netInfo);
            } else if (netInfo.netBalance.compareTo(BigDecimal.ZERO) < 0) {
                debtors.add(netInfo);
            }
        }
        
        // Greedy matching: match largest creditor with largest debtor
        int creditorIndex = 0;
        int debtorIndex = 0;
        
        while (creditorIndex < creditors.size() && debtorIndex < debtors.size()) {
            NetBalanceInfo creditor = creditors.get(creditorIndex);
            NetBalanceInfo debtor = debtors.get(debtorIndex);
            
            BigDecimal creditorAmount = creditor.netBalance;
            BigDecimal debtorAmount = debtor.netBalance.abs();
            
            BigDecimal settlementAmount = creditorAmount.min(debtorAmount);
            
            if (settlementAmount.compareTo(BigDecimal.ZERO) > 0) {
                SettlementSuggestionDTO suggestion = new SettlementSuggestionDTO();
                suggestion.setFromUserId(debtor.userId);
                suggestion.setFromUserName(debtor.userName);
                suggestion.setToUserId(creditor.userId);
                suggestion.setToUserName(creditor.userName);
                suggestion.setAmount(settlementAmount);
                suggestion.setReason("Minimizes total transactions needed to settle all debts");
                suggestions.add(suggestion);
                
                // Update remaining balances
                creditor.netBalance = creditor.netBalance.subtract(settlementAmount);
                debtor.netBalance = debtor.netBalance.add(settlementAmount);
                
                // Move to next if balance is zero
                if (creditor.netBalance.compareTo(BigDecimal.ZERO) == 0) {
                    creditorIndex++;
                }
                if (debtor.netBalance.compareTo(BigDecimal.ZERO) == 0) {
                    debtorIndex++;
                }
            } else {
                break;
            }
        }
        
        return suggestions;
    }
    
    /**
     * Helper class to track net balance info
     */
    private static class NetBalanceInfo {
        UUID userId;
        String userName;
        BigDecimal netBalance;
    }

    /**
     * Get balance between two specific users across all their common groups
     * Following SRP - BalanceService handles balance calculation between users
     * 
     * This method calculates the net balance between two users across all groups
     * where both users are members. It shows:
     * - All individual balances between the two users
     * - Net balance (final amount one owes to the other)
     * - Direction (who owes whom)
     */
    public BalanceBetweenUsersResponseDTO getBalanceBetweenUsers(UUID userId1, UUID userId2) {
        // Get both users
        User user1 = userService.getUserByUserId(userId1);
        User user2 = userService.getUserByUserId(userId2);
        
        if (user1 == null) {
            throw new RuntimeException("User not found with id: " + userId1);
        }
        if (user2 == null) {
            throw new RuntimeException("User not found with id: " + userId2);
        }
        
        // Get common groups (groups where both users are members)
        List<Group> user1Groups = new ArrayList<>(user1.getGroups());
        List<Group> commonGroups = new ArrayList<>();
        
        for (Group group : user1Groups) {
            if (group.getUsers().contains(user2)) {
                commonGroups.add(group);
            }
        }
        
        // Collect all balances between these two users
        List<BalanceDTO> individualBalances = new ArrayList<>();
        BigDecimal totalFromUser1ToUser2 = BigDecimal.ZERO;
        BigDecimal totalFromUser2ToUser1 = BigDecimal.ZERO;
        
        for (Group group : commonGroups) {
            GroupBalancesResponseDTO groupBalances = groupService.getGroupBalancesWithSettlements(group.getId());
            
            if (groupBalances.getBalances() != null) {
                for (BalanceDTO balance : groupBalances.getBalances()) {
                    boolean isUser1ToUser2 = balance.getFromUserId().equals(userId1) && 
                                           balance.getToUserId().equals(userId2);
                    boolean isUser2ToUser1 = balance.getFromUserId().equals(userId2) && 
                                           balance.getToUserId().equals(userId1);
                    
                    if (isUser1ToUser2) {
                        individualBalances.add(balance);
                        totalFromUser1ToUser2 = totalFromUser1ToUser2.add(balance.getAmount());
                    } else if (isUser2ToUser1) {
                        individualBalances.add(balance);
                        totalFromUser2ToUser1 = totalFromUser2ToUser1.add(balance.getAmount());
                    }
                }
            }
        }
        
        // Calculate net balance
        BigDecimal netBalance = totalFromUser2ToUser1.subtract(totalFromUser1ToUser2);
        
        // Determine direction
        String direction;
        if (netBalance.compareTo(BigDecimal.ZERO) > 0) {
            direction = "userId1_owes_userId2"; // User1 owes User2
        } else if (netBalance.compareTo(BigDecimal.ZERO) < 0) {
            direction = "userId2_owes_userId1"; // User2 owes User1
        } else {
            direction = "settled"; // All settled
        }
        
        // Build response
        BalanceBetweenUsersResponseDTO response = new BalanceBetweenUsersResponseDTO();
        response.setUserId1(userId1);
        response.setUserName1(user1.getName());
        response.setUserId2(userId2);
        response.setUserName2(user2.getName());
        response.setNetBalance(netBalance.abs());
        response.setDirection(direction);
        response.setIndividualBalances(individualBalances);
        
        return response;
    }

    /**
     * Get balance matrix (visual balance representation)
     * Following SRP - BalanceService handles balance matrix generation
     * 
     * This method creates a 2D matrix showing balances between all users in a group.
     * Matrix structure:
     * - Rows represent "from" users (who owes)
     * - Columns represent "to" users (who is owed)
     * - Each cell [i][j] shows amount from user i to user j
     * 
     * Useful for visual representation of balances in tables/grids.
     */
    public BalanceMatrixResponseDTO getBalanceMatrix(UUID groupId) {
        // Get group balances
        GroupBalancesResponseDTO groupBalances = groupService.getGroupBalancesWithSettlements(groupId);
        Group group = groupService.getAGroupById(groupId);
        
        // Get all users in the group
        List<User> groupUsers = new ArrayList<>(group.getUsers());
        List<String> userIds = new ArrayList<>();
        List<String> userNames = new ArrayList<>();
        
        for (User user : groupUsers) {
            userIds.add(user.getId().toString());
            userNames.add(user.getName());
        }
        
        // Create matrix (rows = from users, columns = to users)
        List<List<BalanceMatrixCellDTO>> matrix = new ArrayList<>();
        
        for (int i = 0; i < groupUsers.size(); i++) {
            List<BalanceMatrixCellDTO> row = new ArrayList<>();
            UUID fromUserId = groupUsers.get(i).getId();
            
            for (int j = 0; j < groupUsers.size(); j++) {
                UUID toUserId = groupUsers.get(j).getId();
                
                if (i == j) {
                    // Same user - no balance (diagonal)
                    row.add(new BalanceMatrixCellDTO(BigDecimal.ZERO, "zero"));
                } else {
                    // Find balance from fromUserId to toUserId
                    BigDecimal amount = findBalanceAmount(groupBalances.getBalances(), fromUserId, toUserId);
                    
                    if (amount.compareTo(BigDecimal.ZERO) > 0) {
                        // fromUserId owes toUserId
                        row.add(new BalanceMatrixCellDTO(amount, "from_to"));
                    } else {
                        // No balance in this direction
                        row.add(new BalanceMatrixCellDTO(BigDecimal.ZERO, "zero"));
                    }
                }
            }
            matrix.add(row);
        }
        
        // Build response
        BalanceMatrixResponseDTO response = new BalanceMatrixResponseDTO();
        response.setGroupId(groupId);
        response.setGroupName(group.getName());
        response.setUserIds(userIds);
        response.setUserNames(userNames);
        response.setMatrix(matrix);
        
        return response;
    }
    
    /**
     * Helper method to find balance amount between two users
     */
    private BigDecimal findBalanceAmount(List<BalanceDTO> balances, UUID fromUserId, UUID toUserId) {
        if (balances == null) {
            return BigDecimal.ZERO;
        }
        
        for (BalanceDTO balance : balances) {
            if (balance.getFromUserId().equals(fromUserId) && 
                balance.getToUserId().equals(toUserId)) {
                return balance.getAmount();
            }
        }
        return BigDecimal.ZERO;
    }

    /**
     * Get balance summary for a user (total owe, owed, net)
     * Following SRP - BalanceService handles user balance summary
     * 
     * This method returns a summary of user's balances across all groups:
     * - Total owed (how much user owes to others)
     * - Total owed to user (how much others owe to user)
     * - Net balance (difference)
     * 
     * This is a simplified version of getUserBalances that only returns summary totals.
     */
    public UserBalanceSummaryDTO getUserBalanceSummary(UUID userId) {
        // Get user balances (which includes summary)
        UserBalancesResponseDTO userBalances = getUserBalances(userId);
        
        // Return just the summary
        return userBalances.getSummary();
    }

    /**
     * Helper method to find user balance in user summaries
     */
    private UserBalanceDTO findUserBalance(List<UserBalanceDTO> userSummaries, UUID userId) {
        if (userSummaries == null) {
            return null;
        }
        for (UserBalanceDTO userBalance : userSummaries) {
            if (userBalance.getUserId().equals(userId)) {
                return userBalance;
            }
        }
        return null;
    }
}

