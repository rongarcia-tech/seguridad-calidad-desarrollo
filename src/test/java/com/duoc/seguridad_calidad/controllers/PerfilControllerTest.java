package com.duoc.seguridad_calidad.controllers;

import com.duoc.seguridad_calidad.domain.Perfil;
import com.duoc.seguridad_calidad.domain.Usuario;
import com.duoc.seguridad_calidad.repositories.PerfilRepository;
import com.duoc.seguridad_calidad.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PerfilController.class)
@WithMockUser(username = "test@test.com")
class PerfilControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PerfilRepository perfilRepository;

    @MockitoBean
    private UsuarioRepository usuarioRepository;

    private Usuario usuarioConPerfil;
    private Usuario usuarioSinPerfil;

    @BeforeEach
    void setUp() {
        usuarioConPerfil = new Usuario();
        usuarioConPerfil.setId(1L);
        usuarioConPerfil.setEmail("test@test.com");
        usuarioConPerfil.setPerfil(new Perfil());

        usuarioSinPerfil = new Usuario();
        usuarioSinPerfil.setId(2L);
        usuarioSinPerfil.setEmail("test@test.com");
        // No se le asigna perfil
    }

    @Test
    void verPerfil() throws Exception {
        when(usuarioRepository.findByEmail("test@test.com")).thenReturn(Optional.of(usuarioConPerfil));

        mockMvc.perform(get("/perfil"))
                .andExpect(status().isOk())
                .andExpect(view().name("perfil"))
                .andExpect(model().attribute("perfil", usuarioConPerfil.getPerfil()));
    }

    @Test
    void guardarPerfil_cuandoExiste_actualiza() throws Exception {
        when(usuarioRepository.findByEmail("test@test.com")).thenReturn(Optional.of(usuarioConPerfil));
        when(perfilRepository.save(any(Perfil.class))).thenReturn(new Perfil());

        mockMvc.perform(post("/perfil").with(csrf())
                        .param("direccion", "Direccion Test")
                        .param("telefono", "123456789")
                        .param("cultivos", "Cultivos Test"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/perfil"));
    }

    @Test
    void guardarPerfil_cuandoNoExiste_creaUnoNuevo() throws Exception {
        when(usuarioRepository.findByEmail("test@test.com")).thenReturn(Optional.of(usuarioSinPerfil));
        when(perfilRepository.save(any(Perfil.class))).thenReturn(new Perfil());

        mockMvc.perform(post("/perfil").with(csrf())
                        .param("direccion", "Nueva Direccion")
                        .param("telefono", "987654321")
                        .param("cultivos", "Nuevos Cultivos"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/perfil"));

        // Capturar el perfil que se guarda para verificar que se le asignó el usuario correcto
        ArgumentCaptor<Perfil> perfilCaptor = ArgumentCaptor.forClass(Perfil.class);
        verify(perfilRepository).save(perfilCaptor.capture());
        Perfil perfilGuardado = perfilCaptor.getValue();

        assertThat(perfilGuardado.getUsuario()).isEqualTo(usuarioSinPerfil);
        assertThat(perfilGuardado.getDireccion()).isEqualTo("Nueva Direccion");
    }
}
