package com.duoc.seguridad_calidad.services.impl;

import com.duoc.seguridad_calidad.domain.*;
import com.duoc.seguridad_calidad.repositories.ReservaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ReservaServiceImplTest {

    @Mock
    private ReservaRepository reservaRepository;

    @InjectMocks
    private ReservaServiceImpl reservaService;

    private Aviso aviso;
    private Usuario arrendatario;
    private Maquinaria maquinaria;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        maquinaria = new Maquinaria();
        maquinaria.setId(1L);
        maquinaria.setMarca("Excavadora");

        arrendatario = new Usuario();
        arrendatario.setId(1L);
        arrendatario.setNombre("Arrendatario");
        arrendatario.setEmail("arrendatario@test.com");

        aviso = new Aviso();
        aviso.setId(1L);
        aviso.setMaquinaria(maquinaria);
        aviso.setDisponibleDesde(LocalDate.now().plusDays(1));
        aviso.setDisponibleHasta(LocalDate.now().plusDays(10));
        aviso.setPrecioPorDia(new BigDecimal("100"));
    }

    @Test
    void reservarExitosamente() {
        Reserva reserva = new Reserva();
        reserva.setId(1L);
        reserva.setMaquinaria(maquinaria);
        reserva.setArrendatario(arrendatario);
        reserva.setFechaInicio(LocalDate.now().plusDays(2));
        reserva.setFechaFin(LocalDate.now().plusDays(5));
        reserva.setPrecioTotal(new BigDecimal("300"));
        reserva.setEstado(EstadoReserva.PENDIENTE);

        when(reservaRepository.save(any(Reserva.class))).thenReturn(reserva);

        Reserva nuevaReserva = reservaService.reservar(aviso, arrendatario, LocalDate.now().plusDays(2), LocalDate.now().plusDays(5));

        assertNotNull(nuevaReserva);
        assertEquals(EstadoReserva.PENDIENTE, nuevaReserva.getEstado());
        assertEquals(0, new BigDecimal("300").compareTo(nuevaReserva.getPrecioTotal()));
    }

    @Test
    void reservarConFechaInicioInvalida() {
        assertThrows(IllegalArgumentException.class, () -> {
            reservaService.reservar(aviso, arrendatario, LocalDate.now(), LocalDate.now().plusDays(5));
        });
    }

    @Test
    void reservarConFechaFinInvalida() {
        assertThrows(IllegalArgumentException.class, () -> {
            reservaService.reservar(aviso, arrendatario, LocalDate.now().plusDays(2), LocalDate.now().plusDays(11));
        });
    }

    @Test
    void reservarConRangoFechasInvalido() {
        assertThrows(IllegalArgumentException.class, () -> {
            reservaService.reservar(aviso, arrendatario, LocalDate.now().plusDays(5), LocalDate.now().plusDays(2));
        });
    }
}
