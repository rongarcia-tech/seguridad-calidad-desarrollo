package com.duoc.seguridad_calidad.domain;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class ReservaTest {

    @Test
    void testReservaGettersAndSetters() {
        Reserva reserva = new Reserva();
        Long id = 1L;
        Maquinaria maquinaria = new Maquinaria();
        Usuario arrendatario = new Usuario();
        LocalDate inicio = LocalDate.of(2024, 2, 1);
        LocalDate fin = LocalDate.of(2024, 2, 10);
        BigDecimal precio = new BigDecimal("500000");
        EstadoReserva estado = EstadoReserva.CONFIRMADA;

        reserva.setId(id);
        reserva.setMaquinaria(maquinaria);
        reserva.setArrendatario(arrendatario);
        reserva.setFechaInicio(inicio);
        reserva.setFechaFin(fin);
        reserva.setPrecioTotal(precio);
        reserva.setEstado(estado);

        assertEquals(id, reserva.getId());
        assertEquals(maquinaria, reserva.getMaquinaria());
        assertEquals(arrendatario, reserva.getArrendatario());
        assertEquals(inicio, reserva.getFechaInicio());
        assertEquals(fin, reserva.getFechaFin());
        assertEquals(precio, reserva.getPrecioTotal());
        assertEquals(estado, reserva.getEstado());
    }
}
