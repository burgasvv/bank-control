package org.burgas.bankservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.burgas.bankservice.decoder.EncodeHandler;
import org.burgas.bankservice.dto.CardInit;
import org.burgas.bankservice.dto.CardRequest;
import org.burgas.bankservice.dto.CardResponse;
import org.burgas.bankservice.exception.CardNotFoundException;
import org.burgas.bankservice.exception.WrongPinCodeException;
import org.burgas.bankservice.mapper.CardMapper;
import org.burgas.bankservice.repository.CardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.burgas.bankservice.log.CardLogs.CARD_FOUND_BY_NUMBER_VALID_CODE;
import static org.burgas.bankservice.message.CardMessages.*;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, propagation = SUPPORTS)
public class CardService {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;
    private final EncodeHandler encodeHandler;

    public CardResponse findByParameters(final CardInit cardInit) {
        return this.cardRepository.findCardByNumberAndValidTillAndCode(
                cardInit.getNumber() == null ? "" : cardInit.getNumber(),
                        cardInit.getValidTill() == null ? LocalDate.of(1,1,1) : cardInit.getValidTill(),
                        cardInit.getCode() == null ? 0L : cardInit.getCode()
        )
                .stream()
                .peek(card -> log.info(CARD_FOUND_BY_NUMBER_VALID_CODE.getLog(), card))
                .map(this.cardMapper::toResponse)
                .findFirst()
                .orElseThrow(
                        () -> new CardNotFoundException(CARD_NOT_FOUND.getMessage())
                );
    }

    @Transactional(
            isolation = REPEATABLE_READ, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public CardResponse createCard(final CardRequest cardRequest) {
        if (String.valueOf(cardRequest.getPin()).length() != 4)
            throw new WrongPinCodeException(WRONG_PIN_CODE.getMessage());
        return this.cardMapper.toResponse(
                this.cardRepository.save(this.cardMapper.toEntity(cardRequest))
        );
    }

    @Transactional(
            isolation = REPEATABLE_READ, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String deposit(final CardInit cardInit, final Double amount) {
        return this.cardRepository.findCardByNumberAndValidTillAndCode(
                cardInit.getNumber() == null ? "" : cardInit.getNumber(),
                cardInit.getValidTill() == null ? LocalDate.of(1,1,1) : cardInit.getValidTill(),
                cardInit.getCode() == null ? 0L : cardInit.getCode()
        )
                .map(
                        card -> {
                            String decodedPinString = this.encodeHandler.decode(card.getPin());
                            Long decodedPin = Long.parseLong(decodedPinString);

                            if (decodedPin.equals(cardInit.getPin())) {
                                BigDecimal added = card.getMoney().add(BigDecimal.valueOf(amount));
                                card.setMoney(added);
                                card.setUpdatedAt(LocalDateTime.now());
                                this.cardRepository.save(card);
                                return DEPOSIT_SUCCESS.getMessage();

                            } else {
                                throw new WrongPinCodeException(WRONG_PIN.getMessage());
                            }
                        }
                )
                .orElseThrow(
                        () -> new CardNotFoundException(CARD_NOT_FOUND.getMessage())
                );
    }
}
