package com.duoc.seguridad_calidad.domain;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class AvisoTest {

    @Test
    void testAvisoGettersAndSetters() {
        Aviso aviso = new Aviso();
        Long id = 1L;
        Maquinaria maquinaria = new Maquinaria();
        LocalDate desde = LocalDate.of(2024, 1, 1);
        LocalDate hasta = LocalDate.of(2024, 1, 31);
        BigDecimal precio = new BigDecimal("200000");
        boolean destacado = true;
        MedioPago medioPago = MedioPago.TRANSFERENCIA;
        String condiciones = "Condiciones de prueba";

        aviso.setId(id);
        aviso.setMaquinaria(maquinaria);
        aviso.setDisponibleDesde(desde);
        aviso.setDisponibleHasta(hasta);
        aviso.setPrecioPorDia(precio);
        aviso.setDestacado(destacado);
        aviso.setMedioPago(medioPago);
        aviso.setCondicionesArriendo(condiciones);

        assertEquals(id, aviso.getId());
        assertEquals(maquinaria, aviso.getMaquinaria());
        assertEquals(desde, aviso.getDisponibleDesde());
        assertEquals(hasta, aviso.getDisponibleHasta());
        assertEquals(precio, aviso.getPrecioPorDia());
        assertTrue(aviso.isDestacado());
        assertEquals(medioPago, aviso.getMedioPago());
        assertEquals(condiciones, aviso.getCondicionesArriendo());
    }
}
