package com.rps.smartsplit.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.rps.smartsplit.dto.response.FileUploadResponseDTO;
import com.rps.smartsplit.model.File;
import com.rps.smartsplit.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadService {

    private final Cloudinary cloudinary;
    private final FileRepository fileRepository;

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    private static final String[] ALLOWED_IMAGE_TYPES = {"image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp"};
    private static final String[] ALLOWED_DOCUMENT_TYPES = {"application/pdf", "image/jpeg", "image/jpg", "image/png"};

    @Transactional
    public FileUploadResponseDTO uploadFile(MultipartFile multipartFile, String fileType, UUID linkedResourceId) {
        try {
            // Validate file
            validateFile(multipartFile, fileType);

            // Determine resource type based on file type
            String resourceType = determineResourceType(fileType, multipartFile.getContentType());

            // Upload to Cloudinary
            @SuppressWarnings("unchecked")
            Map<String, Object> uploadParams = ObjectUtils.asMap(
                    "folder", "smartsplit/" + fileType,
                    "resource_type", resourceType,
                    "public_id", UUID.randomUUID().toString()
            );

            @SuppressWarnings("unchecked")
            Map<String, Object> uploadResult = cloudinary.uploader().upload(
                    multipartFile.getBytes(),
                    uploadParams
            );

            // Extract Cloudinary response data
            String fileUrl = (String) uploadResult.get("secure_url");
            String publicId = (String) uploadResult.get("public_id");
            Long fileSize = ((Number) uploadResult.get("bytes")).longValue();
            String mimeType = multipartFile.getContentType();

            // Save file metadata to database
            File file = new File();
            file.setFileName(publicId);
            file.setOriginalFileName(multipartFile.getOriginalFilename());
            file.setFileUrl(fileUrl);
            file.setCloudinaryPublicId(publicId);
            file.setFileType(fileType);
            file.setMimeType(mimeType != null ? mimeType : "application/octet-stream");
            file.setFileSize(fileSize);
            file.setResourceType(resourceType);
            file.setLinkedResourceId(linkedResourceId);

            File savedFile = fileRepository.save(file);

            return new FileUploadResponseDTO(
                    savedFile.getId(),
                    fileUrl,
                    savedFile.getOriginalFileName(),
                    fileType,
                    fileSize,
                    "File uploaded successfully"
            );

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file: " + e.getMessage(), e);
        }
    }

    public ResponseEntity<byte[]> getFile(UUID fileId) {
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found with id: " + fileId));

        try {
            // Download file from Cloudinary URL
            URI uri = URI.create(file.getFileUrl());
            URL url = uri.toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new RuntimeException("Failed to retrieve file from Cloudinary. Response code: " + connection.getResponseCode());
            }

            InputStream inputStream = connection.getInputStream();
            byte[] fileBytes = inputStream.readAllBytes();
            inputStream.close();
            connection.disconnect();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(file.getMimeType()));
            headers.setContentDispositionFormData("attachment", file.getOriginalFileName());
            headers.setContentLength(fileBytes.length);

            return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve file: " + e.getMessage(), e);
        }
    }

    @Transactional
    public String deleteFile(UUID fileId) {
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found with id: " + fileId));

        try {
            // Delete from Cloudinary
            @SuppressWarnings("unchecked")
            Map<String, Object> deleteParams = ObjectUtils.asMap(
                    "resource_type", file.getResourceType()
            );
            cloudinary.uploader().destroy(file.getCloudinaryPublicId(), deleteParams);

            // Delete from database
            fileRepository.delete(file);

            return "File deleted successfully";

        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file: " + e.getMessage(), e);
        }
    }

    private void validateFile(MultipartFile file, String fileType) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new RuntimeException("File size exceeds maximum limit of 10MB");
        }

        String contentType = file.getContentType();
        if (contentType == null) {
            throw new RuntimeException("File content type is not recognized");
        }

        // Validate based on file type
        if (fileType.equals("profile-picture") || fileType.equals("group-image")) {
            boolean isValidImage = false;
            for (String allowedType : ALLOWED_IMAGE_TYPES) {
                if (contentType.equals(allowedType)) {
                    isValidImage = true;
                    break;
                }
            }
            if (!isValidImage) {
                throw new RuntimeException("Invalid image type. Allowed types: JPEG, JPG, PNG, GIF, WEBP");
            }
        } else if (fileType.equals("expense-receipt") || fileType.equals("document")) {
            boolean isValidDocument = false;
            for (String allowedType : ALLOWED_DOCUMENT_TYPES) {
                if (contentType.equals(allowedType)) {
                    isValidDocument = true;
                    break;
                }
            }
            if (!isValidDocument) {
                throw new RuntimeException("Invalid document type. Allowed types: PDF, JPEG, JPG, PNG");
            }
        }
    }

    private String determineResourceType(String fileType, String contentType) {
        if (contentType != null && contentType.startsWith("image/")) {
            return "image";
        }
        return "raw";
    }
}

