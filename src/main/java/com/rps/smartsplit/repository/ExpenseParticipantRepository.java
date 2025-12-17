package com.rps.smartsplit.repository;

import com.rps.smartsplit.model.ExpenseParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExpenseParticipantRepository extends JpaRepository<ExpenseParticipant, UUID> {
}

