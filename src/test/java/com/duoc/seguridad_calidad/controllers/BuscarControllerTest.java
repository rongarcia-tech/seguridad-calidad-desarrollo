package com.duoc.seguridad_calidad.controllers;

import com.duoc.seguridad_calidad.domain.TipoMaquinaria;
import com.duoc.seguridad_calidad.services.MaquinariaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BuscarController.class)
class BuscarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MaquinariaService maquinariaService;

    @WithMockUser(username = "user", roles = "USER")
    @Test
    void buscarSinParametros() throws Exception {
        mockMvc.perform(get("/buscar"))
                .andExpect(status().isOk())
                .andExpect(view().name("buscar"))
                .andExpect(model().attributeExists("tipos"))
                .andExpect(model().attributeDoesNotExist("resultados"));
    }

    @WithMockUser(username = "user", roles = "USER")
    @Test
    void buscarConParametros() throws Exception {
        when(maquinariaService.buscar(any(), any(), any(), any(), any(), any())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/buscar")
                        .param("tipo", TipoMaquinaria.EXCAVADORA.name())
                        .param("region", "Metropolitana"))
                .andExpect(status().isOk())
                .andExpect(view().name("buscar"))
                .andExpect(model().attributeExists("tipos"))
                .andExpect(model().attributeExists("resultados"));
    }
}
