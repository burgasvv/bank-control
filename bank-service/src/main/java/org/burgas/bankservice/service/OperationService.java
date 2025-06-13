package org.burgas.bankservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.burgas.bankservice.dto.OperationResponse;
import org.burgas.bankservice.mapper.OperationMapper;
import org.burgas.bankservice.repository.CardRepository;
import org.burgas.bankservice.repository.OperationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.burgas.bankservice.log.OperationLogs.OPERATION_FOUND_ALL;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Slf4j
@Service
@Transactional(readOnly = true, propagation = SUPPORTS)
@RequiredArgsConstructor
public class OperationService {

    private final OperationRepository operationRepository;
    private final OperationMapper operationMapper;
    private final CardRepository cardRepository;

    public List<OperationResponse> findByCardId(final UUID cardId) {
        return this.operationRepository.findOperationsByCardId(cardId == null ? UUID.nameUUIDFromBytes("0".getBytes(UTF_8)) : cardId)
                .stream()
                .peek(operation -> log.info(OPERATION_FOUND_ALL.getLog(), operation))
                .map(this.operationMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<OperationResponse> findByIdentityId(final UUID identityId) {
        return this.cardRepository.findCardsByIdentityId(identityId == null ? UUID.nameUUIDFromBytes("0".getBytes(UTF_8)) : identityId)
                .parallelStream()
                .flatMap(card -> this.operationRepository.findOperationsByCardId(card.getId()).parallelStream())
                .map(this.operationMapper::toResponse)
                .collect(Collectors.toList());
    }
}
