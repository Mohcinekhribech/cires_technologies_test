package com.example.cirestechnologiesTest.security;

import com.example.cirestechnologiesTest.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {

    private static final String[] WHITE_LIST_URL = {
            "/swagger-ui/**",
            "/api/auth",
            "/api/auth/signup",
            "/api/users/batch",
            "/api/users/generate",
            "/h2-console/**",
            "/v3/api-docs/**"
    };
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(WHITE_LIST_URL).permitAll()
//                                .requestMatchers(HttpMethod.GET,"/competition").hasAnyAuthority("Jury", "Manager", "Adherent")
//                                .requestMatchers(HttpMethod.POST,"/competition").hasAnyAuthority("Jury", "Manager")
//                                .requestMatchers(HttpMethod.DELETE,"/competition").hasAnyAuthority("Jury", "Manager")
//                                .requestMatchers(HttpMethod.PUT,"/competition").hasAnyAuthority("Jury", "Manager")
//
//                                .requestMatchers("/fish").hasAnyAuthority("Jury", "Manager")
//                                .requestMatchers("/hunting").hasAnyAuthority("Jury", "Manager")
//                                .requestMatchers("/level").hasAnyAuthority("Manager")
//                                .requestMatchers("/member").hasAnyAuthority("Manager")
//                                .requestMatchers("/ranking/rapport").hasAnyAuthority("Jury", "Manager", "Adherent")
//                                .requestMatchers("/ranking").hasAnyAuthority("Jury", "Manager")
                                .anyRequest()
                                .authenticated()
                )
                .headers(httpSecurityHeadersConfigurer ->
                        httpSecurityHeadersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        ;

        return http.build();
    }


}
