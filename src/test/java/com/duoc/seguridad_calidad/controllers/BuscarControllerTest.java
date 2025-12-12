package com.duoc.seguridad_calidad.controllers;

import com.duoc.seguridad_calidad.domain.TipoMaquinaria;
import com.duoc.seguridad_calidad.services.MaquinariaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
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

    @WithMockUser(username = "user", roles = "USER")
    @Test
    void buscarConTodosLosParametros() throws Exception {
        when(maquinariaService.buscar(any(), any(), any(), any(), any(), any())).thenReturn(Collections.emptyList());
        String fechaDesde = "2024-01-01";
        String fechaHasta = "2024-01-31";
        BigDecimal precioMax = new BigDecimal("100000");

        mockMvc.perform(get("/buscar")
                        .param("tipo", TipoMaquinaria.EXCAVADORA.name())
                        .param("region", "Metropolitana")
                        .param("comuna", "Santiago")
                        .param("desde", fechaDesde)
                        .param("hasta", fechaHasta)
                        .param("precioMax", precioMax.toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("buscar"))
                .andExpect(model().attributeExists("resultados"));

        verify(maquinariaService).buscar(
                TipoMaquinaria.EXCAVADORA,
                "Metropolitana",
                "Santiago",
                LocalDate.parse(fechaDesde),
                LocalDate.parse(fechaHasta),
                precioMax
        );
    }

    @WithMockUser(username = "user", roles = "USER")
    @Test
    void buscarConFechasEnBlanco() throws Exception {
        when(maquinariaService.buscar(any(), any(), any(), any(), any(), any())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/buscar")
                        .param("region", "Valparaíso")
                        .param("desde", "")
                        .param("hasta", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("buscar"))
                .andExpect(model().attributeExists("resultados"));

        verify(maquinariaService).buscar(
                null,
                "Valparaíso",
                null,
                null,
                null,
                null
        );
    }

    @WithMockUser(username = "user", roles = "USER")
    @Test
    void buscarSoloPorComuna() throws Exception {
        when(maquinariaService.buscar(any(), any(), any(), any(), any(), any())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/buscar")
                        .param("comuna", "Santiago"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("resultados"));

        verify(maquinariaService).buscar(null, null, "Santiago", null, null, null);
    }

    @WithMockUser(username = "user", roles = "USER")
    @Test
    void buscarSoloPorPrecioMax() throws Exception {
        when(maquinariaService.buscar(any(), any(), any(), any(), any(), any())).thenReturn(Collections.emptyList());
        BigDecimal precio = new BigDecimal("50000");

        mockMvc.perform(get("/buscar")
                        .param("precioMax", precio.toString()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("resultados"));

        verify(maquinariaService).buscar(null, null, null, null, null, precio);
    }

    @WithMockUser(username = "user", roles = "USER")
    @Test
    void buscarSoloPorFechaDesde() throws Exception {
        when(maquinariaService.buscar(any(), any(), any(), any(), any(), any())).thenReturn(Collections.emptyList());
        String fecha = "2024-01-01";

        mockMvc.perform(get("/buscar")
                        .param("desde", fecha))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("resultados"));

        verify(maquinariaService).buscar(null, null, null, LocalDate.parse(fecha), null, null);
    }

    @WithMockUser(username = "user", roles = "USER")
    @Test
    void buscarSoloPorFechaHasta() throws Exception {
        when(maquinariaService.buscar(any(), any(), any(), any(), any(), any())).thenReturn(Collections.emptyList());
        String fecha = "2024-12-31";

        mockMvc.perform(get("/buscar")
                        .param("hasta", fecha))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("resultados"));

        verify(maquinariaService).buscar(null, null, null, null, LocalDate.parse(fecha), null);
    }

    @WithMockUser(username = "user", roles = "USER")
    @Test
    void buscarConParametrosVaciosNoLlamaServicio() throws Exception {
        // region="", comuna="", desde="", hasta="" -> El if debe dar false
        mockMvc.perform(get("/buscar")
                        .param("region", "  ")
                        .param("comuna", "")
                        .param("desde", "")
                        .param("hasta", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("buscar"))
                .andExpect(model().attributeDoesNotExist("resultados"));

        // Verificar que NO se llamó al servicio
        verify(maquinariaService, org.mockito.Mockito.never()).buscar(any(), any(), any(), any(), any(), any());
    }
}
