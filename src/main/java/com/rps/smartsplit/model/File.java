package com.rps.smartsplit.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "files")
public class File extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String originalFileName;

    @Column(nullable = false)
    private String fileUrl;

    @Column(nullable = false)
    private String cloudinaryPublicId;

    @Column(nullable = false)
    private String fileType; // profile-picture, group-image, expense-receipt, document

    @Column(nullable = false)
    private String mimeType;

    @Column(nullable = false)
    private Long fileSize; // in bytes

    @Column(nullable = false)
    private String resourceType; // image, raw, auto

    // Optional: Link to specific resource
    private UUID linkedResourceId; // Can be userId, groupId, expenseId, etc.

    public File() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getCloudinaryPublicId() {
        return cloudinaryPublicId;
    }

    public void setCloudinaryPublicId(String cloudinaryPublicId) {
        this.cloudinaryPublicId = cloudinaryPublicId;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public UUID getLinkedResourceId() {
        return linkedResourceId;
    }

    public void setLinkedResourceId(UUID linkedResourceId) {
        this.linkedResourceId = linkedResourceId;
    }
}

