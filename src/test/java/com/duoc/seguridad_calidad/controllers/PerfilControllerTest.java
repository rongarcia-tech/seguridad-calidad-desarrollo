package com.duoc.seguridad_calidad.controllers;

import com.duoc.seguridad_calidad.domain.Perfil;
import com.duoc.seguridad_calidad.domain.Usuario;
import com.duoc.seguridad_calidad.repositories.PerfilRepository;
import com.duoc.seguridad_calidad.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
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

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("test@test.com");
        usuario.setPerfil(new Perfil());

        when(usuarioRepository.findByEmail("test@test.com")).thenReturn(Optional.of(usuario));
    }

    @Test
    void verPerfil() throws Exception {
        mockMvc.perform(get("/perfil"))
                .andExpect(status().isOk())
                .andExpect(view().name("perfil"))
                .andExpect(model().attribute("perfil", usuario.getPerfil()));
    }

    @Test
    void guardarPerfil() throws Exception {
        when(perfilRepository.save(any(Perfil.class))).thenReturn(new Perfil());

        mockMvc.perform(post("/perfil").with(csrf())
                        .param("direccion", "Direccion Test")
                        .param("telefono", "123456789")
                        .param("cultivos", "Cultivos Test"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/perfil"));
    }
}
