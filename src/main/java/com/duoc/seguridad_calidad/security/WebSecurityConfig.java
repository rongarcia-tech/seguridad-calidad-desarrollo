package com.duoc.seguridad_calidad.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;

import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        var pp = PathPatternRequestMatcher.withDefaults();
        var openApiDocs = pp.matcher("/v3/api-docs/**");
        var swaggerUi   = pp.matcher("/swagger-ui/**");

        http
          .cors(Customizer.withDefaults())
          .authorizeHttpRequests(auth -> auth
              .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
              .requestMatchers("/", "/home", "/login", "/register",
                                "/buscar", "/avisos/destacados").permitAll()
              .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
              .requestMatchers("/perfil/**", "/avisos/**", "/reservas/**","/maquinaria", "/maquinaria/**").authenticated()
              .anyRequest().authenticated()
          )
          .csrf(csrf -> csrf.ignoringRequestMatchers(openApiDocs, swaggerUi))
          .headers(headers -> {
              // CSP
              headers.contentSecurityPolicy(csp -> csp.policyDirectives(
                  "default-src 'self'; " +
                  "script-src 'self'; " +
                  "style-src 'self' 'unsafe-inline'; " +
                  "img-src 'self' data:; " +
                  "object-src 'none'; base-uri 'self'; frame-ancestors 'none'"));

              // Clickjacking
              headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::deny);

              // Referrer-Policy
              headers.referrerPolicy(r -> r.policy(ReferrerPolicy.SAME_ORIGIN));

              // Permissions-Policy (Feature-Policy)
              headers
                      .addHeaderWriter(new StaticHeadersWriter(
                              "Permissions-Policy",
                              "geolocation=(), microphone=(), camera=()"
                      ));

              // HSTS (solo efectivo sobre HTTPS)
              headers.httpStrictTransportSecurity(hsts -> hsts
                  .includeSubDomains(true)
                  .preload(true)
                  .maxAgeInSeconds(31536000));
          })
          .formLogin(login -> login
              .loginPage("/login")
              .loginProcessingUrl("/login")
              .defaultSuccessUrl("/home", false)
              .failureUrl("/login?error")
              .permitAll()
          )
          .logout(logout -> logout
              .logoutUrl("/logout")
              .logoutSuccessUrl("/login?logout")
              .deleteCookies("JSESSIONID")
              .permitAll()
          );

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        var cfg = new CorsConfiguration();
        cfg.setAllowedOriginPatterns(List.of("http://localhost:*", "http://127.0.0.1:*"));
        cfg.setAllowedMethods(List.of("GET","POST","PUT","DELETE","PATCH","OPTIONS"));
        cfg.setAllowedHeaders(List.of("Authorization","Content-Type","Accept"));
        cfg.setAllowCredentials(true);

        var source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        source.registerCorsConfiguration("/v3/api-docs/**", cfg);
        source.registerCorsConfiguration("/swagger-ui/**", cfg);
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
