package org.burgas.bankservice.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.burgas.bankservice.entity.Card;
import org.burgas.bankservice.entity.Identity;
import org.burgas.bankservice.exception.CardNotFoundException;
import org.burgas.bankservice.exception.IdentityNotAuthenticatedException;
import org.burgas.bankservice.exception.IdentityNotAuthorizedException;
import org.burgas.bankservice.exception.IdentitySelfControlException;
import org.burgas.bankservice.repository.CardRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.burgas.bankservice.message.CardMessages.CARD_NOT_FOUND;
import static org.burgas.bankservice.message.IdentityMessages.*;

@WebFilter(
        urlPatterns = {
                "/identities/by-id", "/identities/by-email",
                "/identities/update", "/identities/delete",
                "/identities/change-password", "/identities/enable-disable",

                "/cards/by-parameters", "/cards/transfer"
        },
        asyncSupported = true
)
public final class IdentityWebFilter extends OncePerRequestFilter {

    private final CardRepository cardRepository;

    public IdentityWebFilter(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain
    ) throws ServletException, IOException {

        if (
                request.getRequestURI().equals("/identities/by-id") || request.getRequestURI().equals("/identities/delete") ||
                request.getRequestURI().equals("/identities/update") || request.getRequestURI().equals("/identities/change-password")
        ) {

            Authentication authentication = (Authentication) request.getUserPrincipal();
            String identityIdParam = request.getParameter("identityId");

            if (authentication.isAuthenticated()) {
                Identity identity = (Identity) authentication.getPrincipal();
                UUID identityId = identityIdParam == null || identityIdParam.isBlank() ?
                        UUID.nameUUIDFromBytes("0".getBytes(StandardCharsets.UTF_8)) : UUID.fromString(identityIdParam);

                if (identity.getId().equals(identityId)) {
                    filterChain.doFilter(request, response);

                } else {
                    throw new IdentityNotAuthorizedException(IDENTITY_NOT_AUTHORIZED.getMessage());
                }

            } else {
                throw new IdentityNotAuthenticatedException(IDENTITY_NOT_AUTHENTICATED.getMessage());
            }

        } else if (request.getRequestURI().equals("/identities/by-email")) {

            Authentication authentication = (Authentication) request.getUserPrincipal();
            String email = request.getParameter("email");

            if (authentication.isAuthenticated()) {
                Identity identity = (Identity) authentication.getPrincipal();

                if (identity.getUsername().equals(email)) {
                    filterChain.doFilter(request, response);

                } else {
                    throw new IdentityNotAuthorizedException(IDENTITY_NOT_AUTHORIZED.getMessage());
                }

            } else {
                throw new IdentityNotAuthenticatedException(IDENTITY_NOT_AUTHENTICATED.getMessage());
            }

        } else if (request.getRequestURI().equals("/identities/enable-disable")) {

            Authentication authentication = (Authentication) request.getUserPrincipal();
            String identityIdParam = request.getParameter("identityId");

            if (authentication.isAuthenticated()) {
                Identity identity = (Identity) authentication.getPrincipal();
                UUID identityId = identityIdParam == null || identityIdParam.isBlank() ?
                        UUID.nameUUIDFromBytes("0".getBytes(StandardCharsets.UTF_8)) : UUID.fromString(identityIdParam);

                if (!identity.getId().equals(identityId)) {
                    filterChain.doFilter(request, response);

                } else {
                    throw new IdentitySelfControlException(IDENTITY_SELF_CONTROL.getMessage());
                }

            } else {
                throw new IdentityNotAuthenticatedException(IDENTITY_NOT_AUTHENTICATED.getMessage());
            }

        } else if (request.getRequestURI().equals("/cards/by-parameters")) {

            Authentication authentication = (Authentication) request.getUserPrincipal();
            String identityIdParam = request.getParameter("identityId");

            if (authentication.isAuthenticated()) {
                Identity identity = (Identity) authentication.getPrincipal();
                UUID identityId = identityIdParam == null || identityIdParam.isBlank() ?
                        UUID.nameUUIDFromBytes("0".getBytes(StandardCharsets.UTF_8)) : UUID.fromString(identityIdParam);

                if (
                        identity.getId().equals(identityId) ||
                        identity.getAuthority().name().equals("ADMIN") ||
                        identity.getAuthority().name().equals("EMPLOYEE")
                ) {
                    filterChain.doFilter(request, response);

                } else {
                    throw new IdentityNotAuthorizedException(IDENTITY_NOT_AUTHORIZED.getMessage());
                }

            } else {
                throw new IdentityNotAuthenticatedException(IDENTITY_NOT_AUTHENTICATED.getMessage());
            }

        } else if (request.getRequestURI().equals("/cards/transfer")) {

            Authentication authentication = (Authentication) request.getUserPrincipal();
            String fromCardIdParam = request.getParameter("fromCardId");

            if (authentication.isAuthenticated()) {

                Identity identity = (Identity) authentication.getPrincipal();
                UUID fromCardId = fromCardIdParam == null || fromCardIdParam.isBlank() ?
                        UUID.nameUUIDFromBytes("0".getBytes(StandardCharsets.UTF_8)) : UUID.fromString(fromCardIdParam);
                Card card = this.cardRepository.findById(fromCardId).orElseThrow(
                        () -> new CardNotFoundException(CARD_NOT_FOUND.getMessage())
                );

                if (card.getIdentityId().equals(identity.getId())) {
                    filterChain.doFilter(request, response);

                } else {
                    throw new IdentityNotAuthorizedException(IDENTITY_NOT_AUTHORIZED.getMessage());
                }

            } else {
                throw new IdentityNotAuthenticatedException(IDENTITY_NOT_AUTHENTICATED.getMessage());
            }
        }
    }
}
