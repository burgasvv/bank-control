package org.burgas.bankservice.mapper;

import lombok.RequiredArgsConstructor;
import org.burgas.bankservice.dto.IdentityRequest;
import org.burgas.bankservice.dto.IdentityResponse;
import org.burgas.bankservice.entity.Identity;
import org.burgas.bankservice.repository.IdentityRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.UUID.nameUUIDFromBytes;
import static org.burgas.bankservice.message.IdentityMessages.*;

@Component
@RequiredArgsConstructor
public final class IdentityMapper implements EntityMapper<IdentityRequest, Identity, IdentityResponse> {

    private final IdentityRepository identityRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Identity toEntity(IdentityRequest identityRequest) {
        UUID identityId = this.handleData(
                identityRequest.getId(), nameUUIDFromBytes("0".getBytes(UTF_8))
        );
        String password = identityRequest.getPassword() == null || identityRequest.getPassword().isBlank() ? "" : identityRequest.getPassword();
        return this.identityRepository.findById(identityId)
                .map(
                        identity -> Identity.builder()
                                .id(identity.getId())
                                .authority(this.handleData(identityRequest.getAuthority(), identity.getAuthority()))
                                .name(this.handleData(identityRequest.getName(), identity.getName()))
                                .surname(this.handleData(identityRequest.getSurname(), identity.getSurname()))
                                .patronymic(this.handleData(identityRequest.getPatronymic(), identity.getPatronymic()))
                                .password(identity.getPassword())
                                .email(this.handleData(identityRequest.getEmail(), identity.getEmail()))
                                .phone(this.handleData(identityRequest.getPhone(), identity.getPhone()))
                                .passport(this.handleData(identityRequest.getPassport(), identity.getPassport()))
                                .enabled(this.handleData(identityRequest.getEnabled(), identity.isEnabled()))
                                .createdAt(identity.getCreatedAt())
                                .updatedAt(LocalDateTime.now())
                                .build()
                )
                .orElseGet(
                        () -> Identity.builder()
                                .authority(this.handleDataException(identityRequest.getAuthority(), IDENTITY_EMPTY_AUTHORITY.getMessage()))
                                .name(this.handleDataException(identityRequest.getName(), IDENTITY_EMPTY_NAME.getMessage()))
                                .surname(this.handleDataException(identityRequest.getSurname(), IDENTITY_EMPTY_SURNAME.getMessage()))
                                .patronymic(this.handleDataException(identityRequest.getPatronymic(), IDENTITY_EMPTY_PATRONYMIC.getMessage()))
                                .password(this.handleDataException(this.passwordEncoder.encode(password), IDENTITY_EMPTY_PASSWORD.getMessage()))
                                .email(this.handleDataException(identityRequest.getEmail(), IDENTITY_EMPTY_EMAIL.getMessage()))
                                .phone(this.handleDataException(identityRequest.getPhone(), IDENTITY_EMPTY_PHONE.getMessage()))
                                .passport(this.handleDataException(identityRequest.getPassport(), IDENTITY_EMPTY_PASSPORT.getMessage()))
                                .enabled(this.handleDataException(identityRequest.getEnabled(), IDENTITY_EMPTY_ENABLED.getMessage()))
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .build()
                );
    }

    @Override
    public IdentityResponse toResponse(Identity identity) {
        return IdentityResponse.builder()
                .id(identity.getId())
                .authority(identity.getAuthority())
                .name(identity.getName())
                .surname(identity.getSurname())
                .patronymic(identity.getPatronymic())
                .password(identity.getPassword())
                .email(identity.getEmail())
                .phone(identity.getPhone())
                .passport(identity.getPassport())
                .enabled(identity.isEnabled())
                .createdAt(identity.getCreatedAt().format(ofPattern("dd MMMM yyyy, hh:mm:ss")))
                .updatedAt(identity.getUpdatedAt().format(ofPattern("dd MMMM yyyy, hh:mm:ss")))
                .build();
    }
}
