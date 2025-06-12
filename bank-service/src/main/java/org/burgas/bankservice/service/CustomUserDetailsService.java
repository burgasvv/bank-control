package org.burgas.bankservice.service;

import lombok.RequiredArgsConstructor;
import org.burgas.bankservice.exception.IdentityNotFoundException;
import org.burgas.bankservice.repository.IdentityRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import static org.burgas.bankservice.message.IdentityMessages.IDENTITY_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final IdentityRepository identityRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.identityRepository.findIdentityByEmail(username)
                .orElseThrow(
                        () -> new IdentityNotFoundException(IDENTITY_NOT_FOUND.getMessage())
                );
    }
}
