package com.ccaBank.feedback.repositories;

import com.ccaBank.feedback.entities.Client;
import com.ccaBank.feedback.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByFirstName(String firstName);

    Optional<Client> findByUserUsername(String username);

    Optional<Client> findByUser(User user);
}
