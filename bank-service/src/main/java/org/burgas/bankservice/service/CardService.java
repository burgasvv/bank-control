package org.burgas.bankservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.burgas.bankservice.dto.CardInit;
import org.burgas.bankservice.dto.CardRequest;
import org.burgas.bankservice.dto.CardResponse;
import org.burgas.bankservice.entity.Card;
import org.burgas.bankservice.exception.CardNotFoundException;
import org.burgas.bankservice.exception.NotEnoughMoneyException;
import org.burgas.bankservice.exception.NullMoneyAmountOperationException;
import org.burgas.bankservice.exception.WrongPinCodeException;
import org.burgas.bankservice.mapper.CardMapper;
import org.burgas.bankservice.repository.CardRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.UUID.nameUUIDFromBytes;
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
    private final PasswordEncoder passwordEncoder;

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
    public String deposit(final CardInit cardInit, final BigDecimal amount) {
        if (amount == null || amount.doubleValue() == 0.0)
            throw new NullMoneyAmountOperationException(NULL_MONEY_AMOUNT.getMessage());

        return this.cardRepository.findCardByNumberAndValidTillAndCode(
                cardInit.getNumber() == null ? "" : cardInit.getNumber(),
                cardInit.getValidTill() == null ? LocalDate.of(1,1,1) : cardInit.getValidTill(),
                cardInit.getCode() == null ? 0L : cardInit.getCode()
        )
                .map(
                        card -> {

                            if (this.passwordEncoder.matches(String.valueOf(cardInit.getPin()), card.getPin())) {
                                BigDecimal added = card.getMoney().add(amount);
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

    @Transactional(
            isolation = REPEATABLE_READ, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String withdraw(final CardInit cardInit, final BigDecimal amount) {
        if (amount == null || amount.doubleValue() == 0.0)
            throw new NullMoneyAmountOperationException(NULL_MONEY_AMOUNT.getMessage());

        return this.cardRepository.findCardByNumberAndValidTillAndCode(
                        cardInit.getNumber() == null ? "" : cardInit.getNumber(),
                        cardInit.getValidTill() == null ? LocalDate.of(1,1,1) : cardInit.getValidTill(),
                        cardInit.getCode() == null ? 0L : cardInit.getCode()
                )
                .map(
                        card -> {

                            if (card.getMoney().doubleValue() < amount.doubleValue() && !card.getCardType().name().equals("OVERDRAFT"))
                                throw new NotEnoughMoneyException(NOT_ENOUGH_MONEY.getMessage());

                            if (this.passwordEncoder.matches(String.valueOf(cardInit.getPin()), card.getPin())) {
                                BigDecimal added = card.getMoney().subtract(amount);
                                card.setMoney(added);
                                card.setUpdatedAt(LocalDateTime.now());
                                this.cardRepository.save(card);
                                return WITHDRAW_SUCCESS.getMessage();

                            } else {
                                throw new WrongPinCodeException(WRONG_PIN.getMessage());
                            }
                        }
                )
                .orElseThrow(
                        () -> new CardNotFoundException(CARD_NOT_FOUND.getMessage())
                );
    }

    @Transactional(
            isolation = REPEATABLE_READ, propagation = REQUIRED,
            rollbackFor = Exception.class
    )
    public String transfer(final UUID fromCardId, final UUID toCardId, final BigDecimal amount) {
        if (amount == null || amount.doubleValue() == 0.0)
            throw new NullMoneyAmountOperationException(NULL_MONEY_AMOUNT.getMessage());

        Card fromCard = this.cardRepository.findById(fromCardId == null ? nameUUIDFromBytes("0".getBytes(UTF_8)) : fromCardId)
                .orElseThrow(
                        () -> new CardNotFoundException(CARD_NOT_FOUND.getMessage())
                );
        Card toCard = this.cardRepository.findById(toCardId == null ? nameUUIDFromBytes("0".getBytes(UTF_8)) : toCardId)
                .orElseThrow(
                        () -> new CardNotFoundException(CARD_NOT_FOUND.getMessage())
                );

        if (fromCard.getMoney().doubleValue() < amount.doubleValue() && !fromCard.getCardType().name().equals("OVERDRAFT"))
            throw new NotEnoughMoneyException(NOT_ENOUGH_MONEY.getMessage());

        BigDecimal subtractFrom = fromCard.getMoney().subtract(amount);
        fromCard.setMoney(subtractFrom);
        fromCard.setUpdatedAt(LocalDateTime.now());
        this.cardRepository.save(fromCard);

        BigDecimal addedTo = toCard.getMoney().add(amount);
        toCard.setMoney(addedTo);
        toCard.setUpdatedAt(LocalDateTime.now());
        this.cardRepository.save(toCard);

        return TRANSFER_OPERATION_SUCCESSFUL.getMessage();
    }
}
