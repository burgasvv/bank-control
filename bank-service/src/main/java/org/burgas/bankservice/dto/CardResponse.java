package org.burgas.bankservice.dto;

import lombok.*;
import org.burgas.bankservice.entity.CardType;
import org.burgas.bankservice.entity.PaymentSystem;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public final class CardResponse extends Response {

    private UUID id;
    private CardIdentity cardIdentity;
    private CardType cardType;
    private PaymentSystem paymentSystem;
    private String number;
    private String validTill;
    private Long code;
    private BigDecimal money;
    private String createdAt;
    private String updatedAt;
}
