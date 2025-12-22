package com.rps.smartsplit.controller;

import com.rps.smartsplit.dto.response.FileUploadResponseDTO;
import com.rps.smartsplit.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileUploadService fileUploadService;

    /**
     * POST /api/upload/profile-picture
     * Upload profile picture
     * Optional: userId query parameter to link the file to a user
     */
    @PostMapping("/profile-picture")
    public ResponseEntity<FileUploadResponseDTO> uploadProfilePicture(
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) UUID userId) {
        try {
            FileUploadResponseDTO response = fileUploadService.uploadFile(file, "profile-picture", userId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * POST /api/upload/group-image
     * Upload group image
     * Optional: groupId query parameter to link the file to a group
     */
    @PostMapping("/group-image")
    public ResponseEntity<FileUploadResponseDTO> uploadGroupImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) UUID groupId) {
        try {
            FileUploadResponseDTO response = fileUploadService.uploadFile(file, "group-image", groupId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * POST /api/upload/expense-receipt
     * Upload expense receipt
     * Optional: expenseId query parameter to link the file to an expense
     */
    @PostMapping("/expense-receipt")
    public ResponseEntity<FileUploadResponseDTO> uploadExpenseReceipt(
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) UUID expenseId) {
        try {
            FileUploadResponseDTO response = fileUploadService.uploadFile(file, "expense-receipt", expenseId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * POST /api/upload/document
     * Upload general document
     */
    @PostMapping("/document")
    public ResponseEntity<FileUploadResponseDTO> uploadDocument(@RequestParam("file") MultipartFile file) {
        try {
            FileUploadResponseDTO response = fileUploadService.uploadFile(file, "document", null);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * GET /api/upload/{fileId}
     * Get uploaded file
     */
    @GetMapping("/{fileId}")
    public ResponseEntity<byte[]> getUploadedFile(@PathVariable UUID fileId) {
        try {
            return fileUploadService.getFile(fileId);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * DELETE /api/upload/{fileId}
     * Delete uploaded file
     */
    @DeleteMapping("/{fileId}")
    public ResponseEntity<String> deleteUploadedFile(@PathVariable UUID fileId) {
        try {
            String message = fileUploadService.deleteFile(fileId);
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}

