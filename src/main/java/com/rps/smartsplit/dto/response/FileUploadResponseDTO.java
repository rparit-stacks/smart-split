package com.rps.smartsplit.dto.response;

import java.util.UUID;

public class FileUploadResponseDTO {
    private UUID fileId;
    private String fileUrl;
    private String fileName;
    private String fileType;
    private Long fileSize;
    private String message;

    public FileUploadResponseDTO() {
    }

    public FileUploadResponseDTO(UUID fileId, String fileUrl, String fileName, String fileType, Long fileSize, String message) {
        this.fileId = fileId;
        this.fileUrl = fileUrl;
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.message = message;
    }

    public UUID getFileId() {
        return fileId;
    }

    public void setFileId(UUID fileId) {
        this.fileId = fileId;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

