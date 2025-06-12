package org.burgas.bankservice.dto;

import lombok.*;
import org.burgas.bankservice.entity.CardType;
import org.burgas.bankservice.entity.PaymentSystem;

import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public final class CardRequest extends Request {

    private UUID id;
    private UUID identityId;
    private CardType cardType;
    private PaymentSystem paymentSystem;
    private Long pin;
    private Integer years;
}
