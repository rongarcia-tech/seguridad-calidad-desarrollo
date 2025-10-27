package com.duoc.seguridad_calidad.security;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
        // Matchers sin deprecaciones
        // Matchers modernos para CSRF ignoring
        var pp = PathPatternRequestMatcher.withDefaults();

        var openApiDocs = pp.matcher("/v3/api-docs/**");
        var swaggerUi   = pp.matcher("/swagger-ui/**");

        http
                .authorizeHttpRequests(auth -> auth
                        //Datos en memoria
                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                        //swagger
                        .requestMatchers("/v3/api-docs/**","/swagger-ui.html","/swagger-ui/**").permitAll()
                        // estáticos y páginas públicas
                        .requestMatchers("/", "/home", "/login", "/register", "/maquinaria", "/maquinaria/**", "/buscar", "/avisos/destacados").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                        // privadas
                        .requestMatchers("/perfil/**", "/avisos/**", "/reservas/**").authenticated()
                        .anyRequest().permitAll()
                )
                // --- CSRF: ignorar H2 console ---
                .csrf(csrf -> csrf.ignoringRequestMatchers(
                        PathRequest.toH2Console(), openApiDocs, swaggerUi
                ))
                // --- Frames: permitir mismo origen para H2 (usa iframes) ---
                .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
                .formLogin(login -> login
                        .loginPage("/login")                 // GET /login (tu controller ya lo tiene)
                        .loginProcessingUrl("/login")        // POST del formulario
                        .defaultSuccessUrl("/home", false)   // tras login OK -> /home (si no hay SavedRequest)
                        .failureUrl("/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                )
                .csrf(Customizer.withDefaults());
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
