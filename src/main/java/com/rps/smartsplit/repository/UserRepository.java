package com.rps.smartsplit.repository;

import com.rps.smartsplit.model.Role;
import com.rps.smartsplit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {


    Optional<User> findByEmail(String email);

    Optional<User> findByPhNumber(String phNumber);

    List<User> findByRole(Role role);
}

