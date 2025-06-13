package org.burgas.bankservice.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public final class TransferResponse extends Response {

    private UUID id;
    private CardIdentity sender;
    private CardIdentity recipient;
    private BigDecimal money;
    private String completedAt;
}
