package com.eric.ecommerce_user_service.config;

import com.eric.ecommerce_user_service.auth.CustomUserDetailsService;
import com.eric.ecommerce_user_service.auth.JwtAuthenticationEntryPoint;
import com.eric.ecommerce_user_service.auth.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtFilter jwtFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final CustomUserDetailsService userDetailsService;


    /**
     * Configures the security filter chain for the application.
     * - Disables CSRF since JWT is stateless.
     * - Defines public endpoints that do not require authentication.
     * - Secures all other endpoints.
     * - Sets up exception handling for unauthorized access.
     * - Ensures the application runs in stateless mode (no sessions).
     * - Adds the JWT filter before the default Spring authentication filter.
     *
     * @param http HttpSecurity instance to configure security settings.
     * @return SecurityFilterChain that defines security rules.
     * @throws Exception if configuration fails.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // CSRF configuration
                .authorizeHttpRequests( auth -> auth
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html",
                                "/api/user/**" // Allow your public endpoints
                        ).permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/api/user/{username}/roles").hasRole("ADMIN") // Only admins can update roles
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider()) // Use custom auth provider
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter before Spring's auth filter
        return http.build();
    }

    /**
     * Defines a password encoder using BCrypt hashing.
     * This ensures passwords are securely hashed before storage.
     *
     * @return BCryptPasswordEncoder instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Provides the AuthenticationManager, which is responsible for user authentication.
     * Uses the default authentication manager configuration provided by Spring.
     *
     * @param authConfig AuthenticationConfiguration instance.
     * @return AuthenticationManager instance.
     * @throws Exception if retrieval fails.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Defines the authentication provider.
     * - Uses a `UserDetailsService` to fetch user details.
     * - Applies the `BCryptPasswordEncoder` for password hashing and verification.
     *
     * @return Configured DaoAuthenticationProvider.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder()); // Encrypt passwords with BCrypt
        return authProvider;
    }

}
