package com.duoc.seguridad_calidad.controllers;

import com.duoc.seguridad_calidad.domain.Aviso;
import com.duoc.seguridad_calidad.domain.Maquinaria;
import com.duoc.seguridad_calidad.domain.Usuario;
import com.duoc.seguridad_calidad.repositories.AvisoRepository;
import com.duoc.seguridad_calidad.repositories.MaquinariaRepository;
import com.duoc.seguridad_calidad.repositories.UsuarioRepository;
import com.duoc.seguridad_calidad.services.AvisoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AvisoController.class)
@WithMockUser(username = "test@test.com")
class AvisoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MaquinariaRepository maquinariaRepository;

    @MockitoBean
    private UsuarioRepository usuarioRepository;

    @MockitoBean
    private AvisoRepository avisoRepository;

    @MockitoBean
    private AvisoService avisoService;

    private Usuario usuario;
    private Maquinaria maquinaria;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("test@test.com");

        maquinaria = new Maquinaria();
        maquinaria.setId(1L);
        maquinaria.setDueno(usuario);

        when(usuarioRepository.findByEmail("test@test.com")).thenReturn(Optional.of(usuario));
    }

    @Test
    void testFormNuevoAviso() throws Exception {
        when(maquinariaRepository.findAll()).thenReturn(List.of(maquinaria));

        mockMvc.perform(get("/avisos/nuevo"))
                .andExpect(status().isOk())
                .andExpect(view().name("aviso-form"))
                .andExpect(model().attribute("misMaquinarias", List.of(maquinaria)));
    }

    @Test
    void testPublicarAviso() throws Exception {
        when(maquinariaRepository.findById(anyLong())).thenReturn(Optional.of(maquinaria));
        when(avisoService.publicar(any(), any(), any(), any(),  anyBoolean(), any(), any())).thenReturn(new Aviso());

        mockMvc.perform(post("/avisos").with(csrf())
                        .param("maquinariaId", "1")
                        .param("desde", "2024-01-01")
                        .param("hasta", "2024-01-10")
                        .param("precioPorDia", "100")
                        .param("destacado", "true")
                        .param("condiciones", "condiciones")
                        .param("medioPago", "TARJETA_CREDITO"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    void testMisAvisos() throws Exception {
        Aviso aviso = new Aviso();
        aviso.setId(1L);
        aviso.setMaquinaria(maquinaria);
        when(avisoRepository.findByMaquinaria_Dueno_IdOrderByIdDesc(1L)).thenReturn(List.of(aviso));

        mockMvc.perform(get("/avisos/mios"))
                .andExpect(status().isOk())
                .andExpect(view().name("avisos-mios"))
                .andExpect(model().attribute("avisos", List.of(aviso)));
    }

    @Test
    @WithMockUser
    void verAviso_cuandoExiste_muestraAviso() throws Exception {
        Aviso aviso = new Aviso();
        aviso.setId(1L);
        when(avisoService.buscarPorId(1L)).thenReturn(aviso);

        mockMvc.perform(get("/avisos/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("ver-aviso"))
                .andExpect(model().attribute("aviso", aviso));
    }

    @Test
    @WithMockUser
    void verAviso_cuandoNoExiste_lanzaExcepcion() throws Exception {
        when(avisoService.buscarPorId(99L)).thenThrow(new NoSuchElementException("Aviso no encontrado"));

        mockMvc.perform(get("/avisos/99"))
                .andExpect(status().isNotFound());
    }
}
