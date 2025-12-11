package com.duoc.seguridad_calidad.controllers;

import com.duoc.seguridad_calidad.services.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(RegistroController.class)
@AutoConfigureMockMvc(addFilters = false) //
class RegistroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService usuarioService;

    @Test
    void testFormRegistro() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    void testSubmitRegistroExitoso() throws Exception {
        when(usuarioService.registrar(anyString(), anyString(), anyString())).thenReturn(null);

        mockMvc.perform(post("/register").with(csrf())
                        .param("nombre", "Test User")
                        .param("email", "test@test.com")
                        .param("password", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void testSubmitRegistroFallido() throws Exception {
        when(usuarioService.registrar(anyString(), anyString(), anyString()))
                .thenThrow(new IllegalArgumentException("Email ya registrado"));

        mockMvc.perform(post("/register").with(csrf())
                        .param("nombre", "Test User")
                        .param("email", "test@test.com")
                        .param("password", "password"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attribute("error", "Email ya registrado"));
    }
}
