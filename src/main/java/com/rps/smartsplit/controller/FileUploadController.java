package com.rps.smartsplit.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/upload")
public class FileUploadController {

    /**
     * POST /api/upload/profile-picture
     * Upload profile picture
     */
    @PostMapping("/profile-picture")
    public ResponseEntity<String> uploadProfilePicture(@RequestParam("file") MultipartFile file) {
        // TODO: Implement profile picture upload
        return null;
    }

    /**
     * POST /api/upload/group-image
     * Upload group image
     */
    @PostMapping("/group-image")
    public ResponseEntity<String> uploadGroupImage(@RequestParam("file") MultipartFile file) {
        // TODO: Implement group image upload
        return null;
    }

    /**
     * POST /api/upload/expense-receipt
     * Upload expense receipt
     */
    @PostMapping("/expense-receipt")
    public ResponseEntity<String> uploadExpenseReceipt(@RequestParam("file") MultipartFile file) {
        // TODO: Implement expense receipt upload
        return null;
    }

    /**
     * POST /api/upload/document
     * Upload general document
     */
    @PostMapping("/document")
    public ResponseEntity<String> uploadDocument(@RequestParam("file") MultipartFile file) {
        // TODO: Implement document upload
        return null;
    }

    /**
     * GET /api/upload/{fileId}
     * Get uploaded file
     */
    @GetMapping("/{fileId}")
    public ResponseEntity<byte[]> getUploadedFile(@PathVariable UUID fileId) {
        // TODO: Implement get file
        return null;
    }

    /**
     * DELETE /api/upload/{fileId}
     * Delete uploaded file
     */
    @DeleteMapping("/{fileId}")
    public ResponseEntity<String> deleteUploadedFile(@PathVariable UUID fileId) {
        // TODO: Implement delete file
        return null;
    }
}

