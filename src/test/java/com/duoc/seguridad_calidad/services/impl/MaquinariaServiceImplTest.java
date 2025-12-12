package com.duoc.seguridad_calidad.services.impl;

import com.duoc.seguridad_calidad.domain.Maquinaria;
import com.duoc.seguridad_calidad.domain.TipoMaquinaria;
import com.duoc.seguridad_calidad.repositories.MaquinariaRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MaquinariaServiceImplTest {

    @Mock
    private MaquinariaRepository maquinariaRepository;

    // Mocks necesarios para simular la ejecución interna de la Specification
    @Mock
    private Root<Maquinaria> root;
    @Mock
    private CriteriaQuery<?> query;
    @Mock
    private CriteriaBuilder builder;
    @Mock
    private Predicate predicate;

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
        verify(maquinariaRepository).findAll(any(Specification.class));
    }

    @Test
    void buscarConTodosLosFiltros() {
        when(maquinariaRepository.findAll(any(Specification.class))).thenReturn(List.of(maquinaria1));
        List<Maquinaria> resultado = maquinariaService.buscar(TipoMaquinaria.EXCAVADORA, "Metropolitana", "Santiago", null, null, new BigDecimal("150"));
        assertEquals(1, resultado.size());
        assertEquals(TipoMaquinaria.EXCAVADORA, resultado.get(0).getTipo());
    }

    @Test
    void buscarSoloPorTipo() {
        when(maquinariaRepository.findAll(any(Specification.class))).thenReturn(List.of(maquinaria1));
        List<Maquinaria> resultado = maquinariaService.buscar(TipoMaquinaria.EXCAVADORA, null, null, null, null, null);
        assertEquals(1, resultado.size());
    }

    @Test
    void buscarSoloPorRegion() {
        when(maquinariaRepository.findAll(any(Specification.class))).thenReturn(List.of(maquinaria1));
        List<Maquinaria> resultado = maquinariaService.buscar(null, "Metropolitana", null, null, null, null);
        assertEquals(1, resultado.size());
    }

    @Test
    void buscarSoloPorComuna() {
        when(maquinariaRepository.findAll(any(Specification.class))).thenReturn(List.of(maquinaria1));
        List<Maquinaria> resultado = maquinariaService.buscar(null, null, "Santiago", null, null, null);
        assertEquals(1, resultado.size());
    }

    @Test
    void buscarSoloPorPrecioMax() {
        when(maquinariaRepository.findAll(any(Specification.class))).thenReturn(List.of(maquinaria1));
        List<Maquinaria> resultado = maquinariaService.buscar(null, null, null, null, null, new BigDecimal("150"));
        assertEquals(1, resultado.size());
    }

    @Test
    void buscarConRegionVacia() {
        when(maquinariaRepository.findAll(any(Specification.class))).thenReturn(List.of(maquinaria1, maquinaria2));
        List<Maquinaria> resultado = maquinariaService.buscar(null, " ", null, null, null, null);
        assertEquals(2, resultado.size());
    }

    @Test
    void buscarConComunaVacia() {
        when(maquinariaRepository.findAll(any(Specification.class))).thenReturn(List.of(maquinaria1, maquinaria2));
        List<Maquinaria> resultado = maquinariaService.buscar(null, null, "  ", null, null, null);
        assertEquals(2, resultado.size());
    }

    @Test
    void buscarDevuelveListaVacia() {
        when(maquinariaRepository.findAll(any(Specification.class))).thenReturn(Collections.emptyList());
        List<Maquinaria> resultado = maquinariaService.buscar(TipoMaquinaria.TRACTOR, null, null, null, null, null);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void buscar_VerificarDistinctEnSpecification() {
        // Configurar el comportamiento de los mocks de JPA
        when(builder.conjunction()).thenReturn(predicate);
        when(builder.equal(any(), any())).thenReturn(predicate);
        when(builder.lessThanOrEqualTo(any(), any(BigDecimal.class))).thenReturn(predicate);

        // Ejecutar el servicio
        maquinariaService.buscar(TipoMaquinaria.EXCAVADORA, "Region", "Comuna", null, null, new BigDecimal("100"));

        // Capturar la Specification que se pasó al repositorio
        ArgumentCaptor<Specification<Maquinaria>> specCaptor = ArgumentCaptor.forClass(Specification.class);
        verify(maquinariaRepository).findAll(specCaptor.capture());

        Specification<Maquinaria> capturedSpec = specCaptor.getValue();

        // Ejecutar manualmente el método toPredicate de la Specification usando los mocks
        // Esto forzará la ejecución de las lambdas internas, incluyendo q.distinct(true)
        capturedSpec.toPredicate(root, query, builder);

        // Verificar que se llamó a distinct(true) en el objeto query
        verify(query).distinct(true);
        
        // Verificar también que se llamó a builder.conjunction() (parte del return cb.conjunction())
        verify(builder, atLeastOnce()).conjunction();
    }
}
