package org.burgas.bankservice.service;

import lombok.RequiredArgsConstructor;
import org.burgas.bankservice.dto.TransferResponse;
import org.burgas.bankservice.entity.Transfer;
import org.burgas.bankservice.mapper.TransferMapper;
import org.burgas.bankservice.repository.TransferRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, propagation = SUPPORTS)
public class TransferService {

    private final TransferRepository transferRepository;
    private final TransferMapper transferMapper;

    public List<TransferResponse> findBySenderCardId(final UUID senderCardId) {
        UUID senderId = senderCardId == null ? UUID.nameUUIDFromBytes("0".getBytes(UTF_8)) : senderCardId;
        List<Transfer> senderTransfers = this.transferRepository.findTransfersBySenderId(senderId);
        List<Transfer> recipientTransfers = this.transferRepository.findTransfersByRecipientId(senderId);

        LinkedList<Transfer> transfers = new LinkedList<>();
        transfers.addAll(senderTransfers);
        transfers.addAll(recipientTransfers);

        return transfers.parallelStream()
                .map(this.transferMapper::toResponse)
                .collect(Collectors.toList());
    }
}
