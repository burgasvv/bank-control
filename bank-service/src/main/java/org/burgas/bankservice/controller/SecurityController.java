package org.burgas.bankservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/security")
public class SecurityController {

    @GetMapping(value = "/csrf-token")
    public ResponseEntity<CsrfToken> getCsrfToken(@RequestAttribute(name = "_csrf") CsrfToken csrfToken) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(csrfToken);
    }
}
