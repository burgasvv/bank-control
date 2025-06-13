package org.burgas.bankservice.dto;

import lombok.*;
import org.burgas.bankservice.entity.OperationType;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public final class OperationResponse extends Response {

    private UUID id;
    private String card;
    private OperationType operationType;
    private BigDecimal money;
    private String completedAt;
}
