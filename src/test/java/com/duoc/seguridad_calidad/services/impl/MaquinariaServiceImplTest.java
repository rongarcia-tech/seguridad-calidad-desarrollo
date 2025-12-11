package com.duoc.seguridad_calidad.services.impl;

import com.duoc.seguridad_calidad.domain.Maquinaria;
import com.duoc.seguridad_calidad.domain.TipoMaquinaria;
import com.duoc.seguridad_calidad.repositories.MaquinariaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class MaquinariaServiceImplTest {

    @Mock
    private MaquinariaRepository maquinariaRepository;

    @InjectMocks
    private MaquinariaServiceImpl maquinariaService;

    private Maquinaria maquinaria1;
    private Maquinaria maquinaria2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        maquinaria1 = new Maquinaria();
        maquinaria1.setId(1L);
        maquinaria1.setTipo(TipoMaquinaria.EXCAVADORA);
        maquinaria1.setUbicacionRegion("Metropolitana");
        maquinaria1.setUbicacionComuna("Santiago");
        maquinaria1.setPrecioPorDia(new BigDecimal("100"));

        maquinaria2 = new Maquinaria();
        maquinaria2.setId(2L);
        maquinaria2.setTipo(TipoMaquinaria.GRUA);
        maquinaria2.setUbicacionRegion("Valparaíso");
        maquinaria2.setUbicacionComuna("Valparaíso");
        maquinaria2.setPrecioPorDia(new BigDecimal("200"));
    }

    @Test
    void buscarSinFiltros() {
        when(maquinariaRepository.findAll(any(Specification.class))).thenReturn(List.of(maquinaria1, maquinaria2));

        List<Maquinaria> resultado = maquinariaService.buscar(null, null, null, null, null, null);

        assertEquals(2, resultado.size());
    }

    @Test
    void buscarConTodosLosFiltros() {
        when(maquinariaRepository.findAll(any(Specification.class))).thenReturn(List.of(maquinaria1));

        List<Maquinaria> resultado = maquinariaService.buscar(TipoMaquinaria.EXCAVADORA, "Metropolitana", "Santiago", null, null, new BigDecimal("150"));

        assertEquals(1, resultado.size());
        assertEquals(TipoMaquinaria.EXCAVADORA, resultado.get(0).getTipo());
    }
}
