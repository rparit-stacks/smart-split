package com.rps.smartsplit.repository;

import com.rps.smartsplit.model.Role;
import com.rps.smartsplit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {


    Optional<User> findByEmail(String email);

    Optional<User> findByPhNumber(String phNumber);

    List<User> findByRole(Role role);

    @Query(value = "SELECT u.* FROM users u JOIN user_group ug ON u.id = ug.user_id WHERE ug.group_id = :groupId", nativeQuery = true)
    List<User> findUsersByGroupId(UUID groupId);


}