package org.burgas.bankservice.repository;

import org.burgas.bankservice.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CardRepository extends JpaRepository<Card, UUID> {

    Optional<Card> findCardByNumberAndValidTillAndCode(String number, LocalDate validTill, Long code);

    List<Card> findCardsByIdentityId(UUID identityId);
}
