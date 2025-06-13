package org.burgas.bankservice.config;

import lombok.RequiredArgsConstructor;
import org.burgas.bankservice.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    public static final String ADMIN = "ADMIN";
    public static final String EMPLOYEE = "EMPLOYEE";
    public static final String USER = "USER";

    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(this.customUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(this.passwordEncoder);
        return new ProviderManager(daoAuthenticationProvider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.csrfTokenRequestHandler(new XorCsrfTokenRequestAttributeHandler()))
                .cors(cors -> cors.configurationSource(new UrlBasedCorsConfigurationSource()))
                .httpBasic(httpBasic -> httpBasic.securityContextRepository(new RequestAttributeSecurityContextRepository()))
                .authenticationManager(this.authenticationManager())
                .authorizeHttpRequests(
                        httpRequest -> httpRequest

                                .requestMatchers(
                                        "/security/csrf-token",

                                        "/identities/create",

                                        "/cards/deposit", "/cards/withdraw"
                                )
                                .permitAll()

                                .requestMatchers(
                                        "/identities/by-id", "/identities/by-email",
                                        "/identities/update", "/identities/delete",
                                        "/identities/change-password",

                                        "/cards/by-parameters", "/cards/transfer"
                                )
                                .hasAnyAuthority(ADMIN, EMPLOYEE, USER)

                                .requestMatchers(
                                        "/identities",

                                        "/cards/activate-deactivate"
                                )
                                .hasAnyAuthority(ADMIN, EMPLOYEE)

                                .requestMatchers(
                                        "/cards/create"
                                )
                                .hasAnyAuthority(EMPLOYEE)

                                .requestMatchers(
                                        "/identities/enable-disable"
                                )
                                .hasAnyAuthority(ADMIN)
                )
                .build();
    }
}
