package org.burgas.bankservice.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public final class CardInit {

    private String number;
    private LocalDate validTill;
    private Long code;
    private Long pin;
}
