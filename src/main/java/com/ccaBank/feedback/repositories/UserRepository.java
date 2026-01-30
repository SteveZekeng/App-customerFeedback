package com.ccaBank.feedback.repositories;

import com.ccaBank.feedback.entities.Role;
import com.ccaBank.feedback.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByUsername(String username);

    List<User> findByRole(Role role);


}
