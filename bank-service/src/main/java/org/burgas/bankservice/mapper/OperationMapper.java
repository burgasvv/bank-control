package org.burgas.bankservice.mapper;

import lombok.RequiredArgsConstructor;
import org.burgas.bankservice.dto.OperationResponse;
import org.burgas.bankservice.entity.Card;
import org.burgas.bankservice.entity.Operation;
import org.burgas.bankservice.exception.CardNotFoundException;
import org.burgas.bankservice.repository.CardRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.burgas.bankservice.message.CardMessages.CARD_NOT_FOUND;

@Component
@RequiredArgsConstructor
public final class OperationMapper {

    private final CardRepository cardRepository;

    public OperationResponse toResponse(final Operation operation) {
        Card card = this.cardRepository.findById(operation.getCardId() == null ? UUID.nameUUIDFromBytes("0".getBytes(UTF_8)) : operation.getCardId())
                .orElseThrow(
                        () -> new CardNotFoundException(CARD_NOT_FOUND.getMessage())
                );
        return OperationResponse.builder()
                .id(operation.getId())
                .card(card.getPaymentSystem().name() + " " + card.getNumber())
                .operationType(operation.getOperationType())
                .money(operation.getMoney())
                .completedAt(operation.getCompletedAt().format(ofPattern("dd.MM.yyyy, hh:mm:ss")))
                .build();
    }
}
