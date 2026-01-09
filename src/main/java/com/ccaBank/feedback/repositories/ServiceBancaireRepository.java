package com.ccaBank.feedback.repositories;

import com.ccaBank.feedback.entities.ServiceBancaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceBancaireRepository extends JpaRepository<ServiceBancaire, Long> {
}
