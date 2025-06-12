package org.burgas.bankservice.controller;

import lombok.RequiredArgsConstructor;
import org.burgas.bankservice.dto.CardRequest;
import org.burgas.bankservice.dto.CardResponse;
import org.burgas.bankservice.service.CardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/cards")
public class CardController {

    private final CardService cardService;

    @GetMapping(value = "/by-parameters")
    public ResponseEntity<CardResponse> findCardByParameters(
            @RequestParam String number, @RequestParam LocalDate validTill, @RequestParam Long code
    ) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.cardService.findByParameters(number, validTill, code));
    }

    @PostMapping(value = "/create")
    public ResponseEntity<CardResponse> createCard(@RequestBody CardRequest cardRequest) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.cardService.createCard(cardRequest));
    }
}
