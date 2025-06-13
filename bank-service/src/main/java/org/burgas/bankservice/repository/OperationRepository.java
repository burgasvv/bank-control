package org.burgas.bankservice.repository;

import org.burgas.bankservice.entity.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OperationRepository extends JpaRepository<Operation, UUID> {

    List<Operation> findOperationsByCardId(UUID cardId);
}
