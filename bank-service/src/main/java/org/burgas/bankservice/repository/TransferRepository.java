package org.burgas.bankservice.repository;

import org.burgas.bankservice.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, UUID> {

    List<Transfer> findTransfersBySenderId(UUID senderId);

    List<Transfer> findTransfersByRecipientId(UUID recipientId);
}
