package com.duoc.seguridad_calidad.services.impl;

import com.duoc.seguridad_calidad.domain.Aviso;
import com.duoc.seguridad_calidad.domain.Maquinaria;
import com.duoc.seguridad_calidad.domain.MedioPago;
import com.duoc.seguridad_calidad.repositories.AvisoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AvisoServiceImplTest {

    @Mock
    private AvisoRepository avisoRepository;

    @InjectMocks
    private AvisoServiceImpl avisoService;

    private Maquinaria maquinaria;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        maquinaria = new Maquinaria();
        maquinaria.setId(1L);
        maquinaria.setMarca("Excavadora");
    }

    @Test
    void publicarAvisoExitosamente() {
        Aviso aviso = new Aviso();
        aviso.setId(1L);
        aviso.setMaquinaria(maquinaria);
        aviso.setDisponibleDesde(LocalDate.now().plusDays(1));
        aviso.setDisponibleHasta(LocalDate.now().plusDays(10));
        aviso.setPrecioPorDia(new BigDecimal("100"));
        aviso.setDestacado(true);
        aviso.setCondicionesArriendo("Condiciones de prueba");
        aviso.setMedioPago(MedioPago.TARJETA_CREDITO);

        when(avisoRepository.save(any(Aviso.class))).thenReturn(aviso);

        Aviso nuevoAviso = avisoService.publicar(maquinaria, LocalDate.now().plusDays(1), LocalDate.now().plusDays(10), new BigDecimal("100"), true, "Condiciones de prueba", MedioPago.TARJETA_CREDITO);

        assertNotNull(nuevoAviso);
        assertEquals(MedioPago.TARJETA_CREDITO, nuevoAviso.getMedioPago());
    }

    @Test
    void obtenerDestacados() {
        Aviso aviso1 = new Aviso();
        aviso1.setId(1L);
        aviso1.setDestacado(true);

        Aviso aviso2 = new Aviso();
        aviso2.setId(2L);
        aviso2.setDestacado(true);

        when(avisoRepository.findTop10ByDestacadoTrueOrderByIdDesc()).thenReturn(List.of(aviso2, aviso1));

        List<Aviso> destacados = avisoService.destacados();

        assertEquals(2, destacados.size());
        assertEquals(2L, destacados.get(0).getId());
    }
}
