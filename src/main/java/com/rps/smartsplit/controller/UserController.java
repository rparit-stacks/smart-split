package com.rps.smartsplit.controller;

import com.rps.smartsplit.dto.request.UserRequestDTO;
import com.rps.smartsplit.dto.response.UserResponseDTO;
import com.rps.smartsplit.dto.response.GroupResponseDTO;
import com.rps.smartsplit.service.UserService;
import com.rps.smartsplit.config.CustomUserDetail;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping()
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/get-user")
    public ResponseEntity<List<UserResponseDTO>> getUser(@RequestParam UUID id){
        return userService.getUser(id);
    }

    @PostMapping("/create-user")
    private ResponseEntity<?> createUser(@RequestBody UserRequestDTO userRequestDTO){
        return userService.createUser(userRequestDTO);
    }

    @PutMapping("/{id}")
    private ResponseEntity<UserResponseDTO> updateUser(@PathVariable UUID id, @RequestBody UserRequestDTO userRequestDTO){
        return userService.updateUser(id, userRequestDTO);
    }

    @DeleteMapping("/delete-user")
    private ResponseEntity<UserResponseDTO> deleteUser(@RequestParam UUID id){
        return userService.deleteUser(id);
    }

    @GetMapping("/get-user-groups")
    private ResponseEntity<List<GroupResponseDTO>> getUserGroups(@RequestParam UUID id){
        return userService.getUserGroups(id);
    }

    @GetMapping("/get-user-balance-summary")
    private ResponseEntity<UserResponseDTO> getUserBalanceSummary(@RequestParam UUID id){
        return userService.getUserBalanceSummary(id);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUser(){
        System.out.println("🔵 /me endpoint called");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("🔍 Authentication: " + (authentication != null ? authentication.getName() : "null"));
        
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetail)) {
            System.out.println("❌ Authentication failed or invalid principal");
            return ResponseEntity.badRequest().build();
        }
        
        CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();
        String email = userDetails.getUsername();
        System.out.println("✅ Getting user data for: " + email);
        
        return userService.getCurrentUser(email);
    }

//    @GetMapping("/by-role/{role}")
//    private ResponseEntity<List<UserResponseDTO>> getUsersByRole(@PathVariable String role){
//        return userService.getUsersByRole(role);
//    }
}
