package org.burgas.bankservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.burgas.bankservice.dto.IdentityRequest;
import org.burgas.bankservice.dto.IdentityResponse;
import org.burgas.bankservice.exception.EmptyIdentityPasswordException;
import org.burgas.bankservice.exception.IdentityNotFoundException;
import org.burgas.bankservice.mapper.IdentityMapper;
import org.burgas.bankservice.repository.IdentityRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.UUID.nameUUIDFromBytes;
import static org.burgas.bankservice.log.IdentityLogs.*;
import static org.burgas.bankservice.message.IdentityMessages.*;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, propagation = SUPPORTS)
public class IdentityService {

    private final IdentityRepository identityRepository;
    private final IdentityMapper identityMapper;
    private final PasswordEncoder passwordEncoder;

    public List<IdentityResponse> findAll() {
        return this.identityRepository.findAll()
                .parallelStream()
                .peek(identity -> log.info(IDENTITY_FOUND_ALL.getLog()))
                .map(this.identityMapper::toResponse)
                .collect(Collectors.toList());
    }

    public IdentityResponse findById(final UUID identityId) {
        return this.identityRepository.findById(identityId == null ? nameUUIDFromBytes("0".getBytes(UTF_8)) : identityId)
                .stream()
                .peek(identity -> log.info(IDENTITY_FOUND_BY_ID.getLog(), identity))
                .map(this.identityMapper::toResponse)
                .findFirst()
                .orElseThrow(
                        () -> new IdentityNotFoundException(IDENTITY_NOT_FOUND.getMessage())
                );
    }

    public IdentityResponse findByEmail(final String email) {
        return this.identityRepository.findIdentityByEmail(email == null ? "" : email)
                .stream()
                .peek(identity -> log.info(IDENTITY_FOUND_BY_EMAIL.getLog(), identity))
                .map(this.identityMapper::toResponse)
                .findFirst()
                .orElseThrow(
                        () -> new IdentityNotFoundException(IDENTITY_NOT_FOUND.getMessage())
                );
    }

    @Transactional(
            isolation = REPEATABLE_READ, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public IdentityResponse createOrUpdate(final IdentityRequest identityRequest) {
        return this.identityMapper.toResponse(
                this.identityRepository.save(this.identityMapper.toEntity(identityRequest))
        );
    }

    @Transactional(
            isolation = REPEATABLE_READ, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String deleteById(final UUID identityId) {
        return this.identityRepository.findById(identityId == null ? nameUUIDFromBytes("0".getBytes(UTF_8)) : identityId)
                .stream()
                .peek(identity -> log.info(IDENTITY_FOUND_BEFORE_DELETE.getLog()))
                .map(
                        identity -> {
                            this.identityRepository.deleteById(identity.getId());
                            return IDENTITY_DELETED.getMessage();
                        }
                )
                .findFirst()
                .orElseThrow(
                        () -> new IdentityNotFoundException(IDENTITY_NOT_FOUND.getMessage())
                );
    }

    @Transactional(
            isolation = REPEATABLE_READ, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String changePassword(final UUID identityId, final String newPassword) {
        return this.identityRepository.findById(identityId == null ? nameUUIDFromBytes("0".getBytes(UTF_8)) : identityId)
                .stream()
                .peek(identity -> log.info(IDENTITY_FOUND_BEFORE_CHANGE_PASSWORD.getLog(), identity))
                .map(
                        identity -> {
                            if (newPassword == null || newPassword.isBlank())
                                throw new EmptyIdentityPasswordException(IDENTITY_EMPTY_PATRONYMIC.getMessage());

                            identity.setPassword(this.passwordEncoder.encode(newPassword));
                            this.identityRepository.save(identity);
                            return IDENTITY_PASSWORD_CHANGED.getMessage();
                        }
                )
                .findFirst()
                .orElseThrow(
                        () -> new IdentityNotFoundException(IDENTITY_NOT_FOUND.getMessage())
                );
    }
}
