package org.burgas.bankservice.mapper;

import lombok.RequiredArgsConstructor;
import org.burgas.bankservice.dto.CardIdentity;
import org.burgas.bankservice.dto.CardRequest;
import org.burgas.bankservice.dto.CardResponse;
import org.burgas.bankservice.entity.Card;
import org.burgas.bankservice.exception.IdentityNotFoundException;
import org.burgas.bankservice.repository.IdentityRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.random.RandomGenerator;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.UUID.nameUUIDFromBytes;
import static org.burgas.bankservice.message.CardMessages.*;
import static org.burgas.bankservice.message.IdentityMessages.IDENTITY_NOT_FOUND;

@Component
@RequiredArgsConstructor
public final class CardMapper implements EntityMapper<CardRequest, Card, CardResponse> {

    private final IdentityRepository identityRepository;

    @Override
    public Card toEntity(CardRequest cardRequest) {
        return this.identityRepository.findById(
                cardRequest.getIdentityId() == null ? nameUUIDFromBytes("0".getBytes(UTF_8)) : cardRequest.getIdentityId()
        )
                .map(
                        identity -> Card.builder()
                                .identityId(identity.getId())
                                .cardType(this.handleDataException(cardRequest.getCardType(), CARD_TYPE_EMPTY.getMessage()))
                                .paymentSystem(this.handleDataException(cardRequest.getPaymentSystem(), PAYMENT_SYSTEM_EMPTY.getMessage()))
                                .number(this.getNumber())
                                .validTill(LocalDate.now().plusYears(this.handleDataException(cardRequest.getYears(), YEARS_EMPTY.getMessage())))
                                .code(this.getCode())
                                .money(new BigDecimal(0))
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .build()
                )
                .orElseThrow(
                        () -> new IdentityNotFoundException(IDENTITY_NOT_FOUND.getMessage())
                );
    }

    @Override
    public CardResponse toResponse(Card card) {
        return this.identityRepository.findById(
                card.getIdentityId() == null ? nameUUIDFromBytes("0".getBytes(UTF_8)) : card.getIdentityId()
        )
                .map(
                        identity -> CardResponse.builder()
                                .id(card.getId())
                                .cardIdentity(
                                        CardIdentity.builder()
                                                .id(identity.getId())
                                                .name(identity.getName())
                                                .surname(identity.getSurname())
                                                .patronymic(identity.getPatronymic())
                                                .build()
                                )
                                .cardType(card.getCardType())
                                .paymentSystem(card.getPaymentSystem())
                                .number(card.getNumber())
                                .validTill(card.getValidTill().format(ofPattern("dd MMMM yyyy")))
                                .code(card.getCode())
                                .money(card.getMoney())
                                .createdAt(card.getCreatedAt().format(ofPattern("dd MMMM yyyy")))
                                .updatedAt(card.getUpdatedAt().format(ofPattern("dd MMMM yyyy")))
                                .build()
                )
                .orElseThrow(
                        () -> new IdentityNotFoundException(IDENTITY_NOT_FOUND.getMessage())
                );
    }

    public String getNumber() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i <= 19; i++) {
            if (i == 5 || i == 10 || i == 15) {
                stringBuilder.append(" ");

            } else {
                stringBuilder.append(RandomGenerator.getDefault().nextInt(0, 9));
            }
        }
        return stringBuilder.toString();
    }

    public Long getCode() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i <= 3; i++)
            stringBuilder.append(RandomGenerator.getDefault().nextInt(0, 9));
        return Long.parseLong(stringBuilder.toString());
    }
}
