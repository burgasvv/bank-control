package org.burgas.bankservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public final class Card extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID identityId;

    @Enumerated(value = EnumType.STRING)
    private CardType cardType;

    @Enumerated(value = EnumType.STRING)
    private PaymentSystem paymentSystem;

    private String number;
    private LocalDate validTill;
    private Long code;
    private BigDecimal money;
    private String pin;
    private Boolean enabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
