package ru.otus.hw.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import ru.otus.hw.services.CustomUserDetailsService;


@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfiguration {

    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/books", "/api/books/**", "/api/authors", "/api/genres")
                            .permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/books/**")
                            .hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/books/**")
                            .hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/books/**")
                            .hasAnyRole("ADMIN", "EDITOR")
                        .anyRequest().authenticated()
                )
                .userDetailsService(userDetailsService)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
//        BCryptPasswordEncoder encoder =  new BCryptPasswordEncoder();
//        System.out.println(encoder.encode("admin123"));
//        System.out.println(encoder.encode("editor123"));
//        System.out.println(encoder.encode("user123"));
        return new BCryptPasswordEncoder();
    }
}
