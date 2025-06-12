package org.burgas.bankservice.repository;

import org.burgas.bankservice.entity.Identity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IdentityRepository extends JpaRepository<Identity, UUID> {

    Optional<Identity> findIdentityByEmail(String email);
}
