package org.burgas.bankservice.controller;

import lombok.RequiredArgsConstructor;
import org.burgas.bankservice.dto.IdentityRequest;
import org.burgas.bankservice.dto.IdentityResponse;
import org.burgas.bankservice.service.IdentityService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/identities")
public class IdentityController {

    private final IdentityService identityService;

    @GetMapping
    public ResponseEntity<List<IdentityResponse>> getAllIdentities() {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.identityService.findAll());
    }

    @GetMapping(value = "/by-id")
    public ResponseEntity<IdentityResponse> getIdentityById(@RequestParam UUID identityId) {
        IdentityResponse identityResponse = this.identityService.findById(identityId);
        HttpHeaders httpHeaders = new HttpHeaders(
                MultiValueMap.fromSingleValue(Map.of(CONTENT_TYPE, APPLICATION_JSON_VALUE))
        );
        return new ResponseEntity<>(identityResponse, httpHeaders, OK);
    }

    @GetMapping(value = "/by-email")
    public ResponseEntity<IdentityResponse> getIdentityByEmail(@RequestParam String email) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.identityService.findByEmail(email));
    }

    @PostMapping(value = "/create")
    public ResponseEntity<IdentityResponse> createIdentity(@RequestBody IdentityRequest identityRequest) {
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(this.identityService.createOrUpdate(identityRequest));
    }

    @PutMapping(value = "/update")
    public ResponseEntity<IdentityResponse> updateIdentity(@RequestBody IdentityRequest identityRequest, @RequestParam UUID identityId) {
        identityRequest.setId(identityId);
        IdentityResponse identityResponse = this.identityService.createOrUpdate(identityRequest);
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(identityResponse);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<String> deleteIdentityById(@RequestParam UUID identityId) {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(this.identityService.deleteById(identityId));
    }

    @PatchMapping(value = "/change-password")
    public ResponseEntity<String> changePassword(@RequestParam UUID identityId, @RequestParam String password) {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(this.identityService.changePassword(identityId, password));
    }

    @PatchMapping(value = "/enable-disable")
    public ResponseEntity<String> enableOrDisableIdentity(@RequestParam UUID identityId, @RequestParam Boolean enabled) {
        return ResponseEntity
                .status(OK)
                .contentType(new MediaType(TEXT_PLAIN, UTF_8))
                .body(this.identityService.enableOrDisable(identityId, enabled));
    }
}
