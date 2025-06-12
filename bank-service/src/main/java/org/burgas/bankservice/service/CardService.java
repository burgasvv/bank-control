package org.burgas.bankservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.burgas.bankservice.dto.CardRequest;
import org.burgas.bankservice.dto.CardResponse;
import org.burgas.bankservice.exception.CardNotFoundException;
import org.burgas.bankservice.log.CardLogs;
import org.burgas.bankservice.mapper.CardMapper;
import org.burgas.bankservice.repository.CardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.burgas.bankservice.message.CardMessages.CARD_NOT_FOUND;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, propagation = SUPPORTS)
public class CardService {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;

    public CardResponse findByParameters(String number, LocalDate validTill, Long code) {

        return this.cardRepository.findCardByNumberAndValidTillAndCode(
                number == null ? "" : number.replaceAll("-", " "),
                        validTill == null ? LocalDate.of(1,1,1) : validTill, code == null ? 0L : code
        )
                .stream()
                .peek(card -> log.info(CardLogs.CARD_FOUND_BY_NUMBER_VALID_CODE.getLog(), card))
                .map(this.cardMapper::toResponse)
                .findFirst()
                .orElseThrow(
                        () -> new CardNotFoundException(CARD_NOT_FOUND.getMessage())
                );
    }

    @Transactional(
            isolation = Isolation.REPEATABLE_READ, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CardResponse createCard(final CardRequest cardRequest) {
        return this.cardMapper.toResponse(
                this.cardRepository.save(this.cardMapper.toEntity(cardRequest))
        );
    }
}
