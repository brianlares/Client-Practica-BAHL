package com.bahl.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests((authHttp) -> authHttp
                .requestMatchers(HttpMethod.GET, "/api/v1/authorized").permitAll()
                .requestMatchers("/api/v1/**").permitAll()
                //.requestMatchers(HttpMethod.GET, "/list").hasAnyAuthority("SCOPE_read", "SCOPE_write")
                //.requestMatchers(HttpMethod.POST, "/create").hasAnyAuthority("SCOPE_write")
                .anyRequest().authenticated())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login(login -> login.loginPage("/oauth2/authorization/api-rest"))
                .oauth2Client(withDefaults())
                .oauth2ResourceServer( resourceServer -> resourceServer.jwt(withDefaults()));

        return http.build();
    }
}
