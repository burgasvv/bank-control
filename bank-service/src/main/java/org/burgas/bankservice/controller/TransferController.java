package org.burgas.bankservice.controller;

import lombok.RequiredArgsConstructor;
import org.burgas.bankservice.dto.TransferResponse;
import org.burgas.bankservice.service.TransferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/transfers")
public class TransferController {

    private final TransferService transferService;

    @GetMapping(value = "/by-sender-card")
    public ResponseEntity<List<TransferResponse>> getTransfersBySenderId(@RequestParam UUID senderCardId) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.transferService.findBySenderCardId(senderCardId));
    }

    @GetMapping(value = "/by-identity")
    public ResponseEntity<List<TransferResponse>> getTransfersByIdentity(@RequestParam UUID identityId) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.transferService.findByIdentityId(identityId));
    }
}
