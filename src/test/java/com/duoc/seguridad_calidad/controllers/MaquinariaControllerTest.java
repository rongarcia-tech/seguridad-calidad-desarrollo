package com.duoc.seguridad_calidad.controllers;

import com.duoc.seguridad_calidad.domain.Aviso;
import com.duoc.seguridad_calidad.domain.Maquinaria;
import com.duoc.seguridad_calidad.domain.TipoMaquinaria;
import com.duoc.seguridad_calidad.domain.Usuario;
import com.duoc.seguridad_calidad.repositories.AvisoRepository;
import com.duoc.seguridad_calidad.repositories.MaquinariaRepository;
import com.duoc.seguridad_calidad.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MaquinariaController.class)
@WithMockUser(username = "test@test.com")
class MaquinariaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MaquinariaRepository maquinariaRepository;

    @MockitoBean
    private AvisoRepository avisoRepository;

    @MockitoBean
    private UsuarioRepository usuarioRepository;

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
    void testDetalleMaquinariaExistente() throws Exception {
        when(maquinariaRepository.findById(1L)).thenReturn(Optional.of(maquinaria));

        Aviso aviso = new Aviso();
        aviso.setCondicionesArriendo("Condiciones de prueba");
        aviso.setMaquinaria(maquinaria); // si tu lógica filtra por maquinaria

        // Usa el método REAL que usa tu controlador:
        // si utiliza findAll():
        when(avisoRepository.findAll()).thenReturn(List.of(aviso));
        // si usa findByMaquinariaId(1L), entonces mockea ese método en vez de findAll()

        mockMvc.perform(get("/maquinaria/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("maquinaria-detalle"))
                .andExpect(model().attribute("maq", maquinaria));
    }


    @Test
    void testDetalleMaquinariaNoExistente() throws Exception {
        when(maquinariaRepository.findById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/maquinaria/99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/buscar"));
    }

    @Test
    void testMisMaquinarias() throws Exception {
        when(maquinariaRepository.findByDuenoIdOrderByIdDesc(1L)).thenReturn(List.of(maquinaria));

        mockMvc.perform(get("/maquinaria/mias"))
                .andExpect(status().isOk())
                .andExpect(view().name("maquinaria-mias"))
                .andExpect(model().attribute("misMaquinarias", List.of(maquinaria)));
    }

    @Test
    void testNuevaMaquinariaForm() throws Exception {
        mockMvc.perform(get("/maquinaria/nueva"))
                .andExpect(status().isOk())
                .andExpect(view().name("maquinaria-form"))
                .andExpect(model().attributeExists("maquinaria"))
                .andExpect(model().attributeExists("tipos"));
    }

    @Test
    void testCrearMaquinaria() throws Exception {
        when(maquinariaRepository.save(any(Maquinaria.class))).thenReturn(maquinaria);

        mockMvc.perform(post("/maquinaria").with(csrf())
                        .param("nombre", "Test Maquinaria")
                        .param("tipo", TipoMaquinaria.EXCAVADORA.name()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/maquinaria/mias"))
                .andExpect(flash().attributeExists("ok"));
    }
}
