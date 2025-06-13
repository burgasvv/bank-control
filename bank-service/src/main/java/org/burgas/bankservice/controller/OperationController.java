package org.burgas.bankservice.controller;

import lombok.RequiredArgsConstructor;
import org.burgas.bankservice.dto.OperationResponse;
import org.burgas.bankservice.service.OperationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/operations")
public class OperationController {

    private final OperationService operationService;

    @GetMapping(value = "/by-card")
    public ResponseEntity<List<OperationResponse>> getOperationsByCardId(@RequestParam UUID cardId) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.operationService.findByCardId(cardId));
    }

    @GetMapping(value = "/by-identity")
    public ResponseEntity<List<OperationResponse>> getOperationsByIdentityId(@RequestParam UUID identityId) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.operationService.findByIdentityId(identityId));
    }
}
