package com.rps.smartsplit.repository;

import com.rps.smartsplit.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FileRepository extends JpaRepository<File, UUID> {
    List<File> findByFileType(String fileType);
    List<File> findByLinkedResourceId(UUID linkedResourceId);
    List<File> findByFileTypeAndLinkedResourceId(String fileType, UUID linkedResourceId);
}

