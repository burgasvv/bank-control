package org.burgas.bankservice.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public final class CardIdentity {

    private UUID id;
    private String name;
    private String surname;
    private String patronymic;
}
