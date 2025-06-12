package org.burgas.bankservice.dto;

import lombok.*;
import org.burgas.bankservice.entity.Authority;

import java.util.UUID;

@Getter
@Builder
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public final class IdentityResponse extends Response {

    private UUID id;
    private Authority authority;
    private String name;
    private String surname;
    private String patronymic;
    private String password;
    private String email;
    private String phone;
    private String passport;
    private Boolean enabled;
    private String createdAt;
    private String updatedAt;
}
