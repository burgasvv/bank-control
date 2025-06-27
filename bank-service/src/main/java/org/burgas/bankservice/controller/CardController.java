package org.burgas.bankservice.controller;

import lombok.RequiredArgsConstructor;
import org.burgas.bankservice.dto.*;
import org.burgas.bankservice.service.CardService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_PLAIN;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/cards")
public class CardController {

    private final CardService cardService;

    @GetMapping(value = "/by-identity")
    public ResponseEntity<List<CardResponse>> getAllCardsByIdentity(@RequestParam UUID identityId) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.cardService.findByIdentityId(identityId));
    }

    @GetMapping(value = "/by-parameters")
    public ResponseEntity<CardResponse> findCardByParameters(@RequestBody CardInit cardInit) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.cardService.findByParameters(cardInit));
    }

    @PostMapping(value = "/create")
    public ResponseEntity<CardResponse> createCard(@RequestBody CardRequest cardRequest) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.cardService.createCard(cardRequest));
    }

    @PutMapping(value = "/deposit")
    public ResponseEntity<OperationResponse> cardDeposit(@RequestBody CardInit cardInit, @RequestParam BigDecimal amount) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.cardService.deposit(cardInit, amount));
    }

    @PutMapping(value = "/withdraw")
    public ResponseEntity<OperationResponse> cardWithdraw(@RequestBody CardInit cardInit, @RequestParam BigDecimal amount) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.cardService.withdraw(cardInit, amount));
    }

    @PutMapping(value = "/transfer")
    public ResponseEntity<TransferResponse> cardTransfer(@RequestParam UUID fromCardId, @RequestParam UUID toCardId, @RequestParam BigDecimal amount) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.cardService.transfer(fromCardId, toCardId, amount));
    }

    @PutMapping(value = "/activate-deactivate")
    public ResponseEntity<String> activateDeactivateCard(@RequestParam UUID cardId, @RequestParam Boolean enabled) {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(this.cardService.activateDeactivate(cardId, enabled));
    }
}
