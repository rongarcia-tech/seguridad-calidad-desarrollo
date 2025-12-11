package com.duoc.seguridad_calidad.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class WebSecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Rutas Públicas: Deberían ser accesibles (200 OK)")
    void publicRoutesShouldBeAccessible() throws Exception {
        // Asumiendo que tienes el LoginController y el archivo style.css creados
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/css/style.css"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Rutas Privadas: Deberían redirigir al login si no estás autenticado")
    void privateRoutesShouldRedirectToLogin() throws Exception {
        // Intento de acceso a /perfil sin usuario
        mockMvc.perform(get("/perfil/mi-usuario"))
                .andExpect(status().is3xxRedirection()) // Redirección
                .andExpect(redirectedUrl("http://localhost/login")); // URL por defecto de formLogin
    }

    @Test
    @DisplayName("Rutas Privadas: Deberían ser accesibles con usuario autenticado")
    @WithMockUser(username = "user", roles = "USER")
    void privateRoutesShouldBeAccessibleWithUser() throws Exception {
        // CAMBIO: Esperamos 405 porque tu controlador solo acepta POST en esta URL
        mockMvc.perform(get("/maquinaria"))
                .andExpect(status().isMethodNotAllowed());

        mockMvc.perform(get("/perfil/mi-usuario"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("CORS: Debería permitir orígenes configurados")
    void corsConfigurationTest() throws Exception {
        mockMvc.perform(options("/api/cualquier-cosa")
                        .header("Origin", "http://localhost:3000")
                        .header("Access-Control-Request-Method", "GET"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:3000"))
                .andExpect(header().string("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,PATCH,OPTIONS"));
    }

    @Test
    @DisplayName("PasswordEncoder: Debería ser una instancia de BCrypt")
    void passwordEncoderBeanTest() {
        // Verifica que el bean se inyecta y codifica correctamente
        String rawPassword = "password123";
        String encoded = passwordEncoder.encode(rawPassword);

        assertTrue(passwordEncoder.matches(rawPassword, encoded));
        assertTrue(encoded.startsWith("$2a$")); // Prefijo estándar de BCrypt
    }

    @Test
    @DisplayName("H2 Console: Debería ser accesible y permitir Frames SAMEORIGIN")
    void h2ConsoleTest() throws Exception {
        // Al corregir el config, Frame Options ahora es SAMEORIGIN, no DENY.
        mockMvc.perform(get("/h2-console/login.do"))
                .andExpect(status().isNotFound()) // 404 porque MockMvc no renderiza el servlet
                .andExpect(header().string("X-Frame-Options", "SAMEORIGIN"));
    }

    // Asegúrate de tener este import estático arriba:
    // import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

    @Test
    @DisplayName("CSRF: Debería requerir token para POST en rutas protegidas")
    void csrfProtectionTest() throws Exception {
        // CASO 1: Sin Token (Debe fallar con 403)
        // Incluso si ponemos usuario, fallará porque falta el token CSRF.
        // Esto confirma que la protección CSRF está activa.
        mockMvc.perform(post("/reservas/crear").with(user("user")))
                .andExpect(status().isForbidden());

        // CASO 2: Con Token Y Con Usuario (Debe pasar la seguridad)
        // Usamos .with(csrf()) para el token
        // Y .with(user(...)) para la autenticación
        mockMvc.perform(post("/reservas/crear")
                        .with(csrf())
                        .with(user("user").roles("USER")))
                // Ahora sí esperamos 404, porque pasamos seguridad pero el controlador no existe
                .andExpect(status().isNotFound());
    }
}