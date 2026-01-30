package com.ccaBank.feedback.repositories;

import com.ccaBank.feedback.entities.Category;
import com.ccaBank.feedback.entities.ServiceBancaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceBancaireRepository extends JpaRepository<ServiceBancaire, Long> {

    List<ServiceBancaire> findByCategory(Category category);
    Optional<ServiceBancaire> findByServiceName(String serviceName);
}
