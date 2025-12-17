package com.rps.smartsplit.controller;

import com.rps.smartsplit.dto.*;
import com.rps.smartsplit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

//    @GetMapping("/by-role/{role}")
//    private ResponseEntity<List<UserResponseDTO>> getUsersByRole(@PathVariable String role){
//        return userService.getUsersByRole(role);
//    }
}
