package org.burgas.bankservice.mapper;

import lombok.RequiredArgsConstructor;
import org.burgas.bankservice.dto.CardIdentity;
import org.burgas.bankservice.dto.TransferResponse;
import org.burgas.bankservice.entity.Card;
import org.burgas.bankservice.entity.Identity;
import org.burgas.bankservice.entity.Transfer;
import org.burgas.bankservice.exception.CardNotFoundException;
import org.burgas.bankservice.exception.IdentityNotFoundException;
import org.burgas.bankservice.repository.CardRepository;
import org.burgas.bankservice.repository.IdentityRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.burgas.bankservice.message.CardMessages.CARD_NOT_FOUND;
import static org.burgas.bankservice.message.IdentityMessages.IDENTITY_NOT_FOUND;

@Component
@RequiredArgsConstructor
public final class TransferMapper {

    private final CardRepository cardRepository;
    private final IdentityRepository identityRepository;

    public TransferResponse toResponse(final Transfer transfer) {
        Card senderCard = this.cardRepository.findById(
                transfer.getSenderId() == null ? UUID.nameUUIDFromBytes("0".getBytes(UTF_8)) : transfer.getSenderId()
        )
                .orElseThrow(
                        () -> new CardNotFoundException(CARD_NOT_FOUND.getMessage())
                );
        Card recipientCard = this.cardRepository.findById(
                        transfer.getRecipientId() == null ? UUID.nameUUIDFromBytes("0".getBytes(UTF_8)) : transfer.getRecipientId()
                )
                .orElseThrow(
                        () -> new CardNotFoundException(CARD_NOT_FOUND.getMessage())
                );
        Identity senderIdentity = this.identityRepository.findById(
                senderCard.getIdentityId() == null ? UUID.nameUUIDFromBytes("0".getBytes(UTF_8)) : senderCard.getIdentityId()
        )
                .orElseThrow(
                        () -> new IdentityNotFoundException(IDENTITY_NOT_FOUND.getMessage())
                );
        Identity recipientIdentity = this.identityRepository.findById(
                        recipientCard.getIdentityId() == null ? UUID.nameUUIDFromBytes("0".getBytes(UTF_8)) : recipientCard.getIdentityId()
                )
                .orElseThrow(
                        () -> new IdentityNotFoundException(IDENTITY_NOT_FOUND.getMessage())
                );

        return TransferResponse.builder()
                .id(transfer.getId())
                .sender(
                        CardIdentity.builder()
                                .id(senderIdentity.getId())
                                .name(senderIdentity.getName())
                                .surname(senderIdentity.getSurname())
                                .patronymic(senderIdentity.getPatronymic())
                                .build()
                )
                .recipient(
                        CardIdentity.builder()
                                .id(recipientIdentity.getId())
                                .name(recipientIdentity.getName())
                                .surname(recipientIdentity.getSurname())
                                .patronymic(recipientIdentity.getPatronymic())
                                .build()
                )
                .money(transfer.getMoney())
                .completedAt(transfer.getCompletedAt().format(ofPattern("dd.MM.yyyy, hh:mm:ss")))
                .build();
    }
}
