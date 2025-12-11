package com.duoc.seguridad_calidad.domain;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class MantenimientoTest {

    @Test
    void testMantenimientoGettersAndSetters() {
        Mantenimiento mantenimiento = new Mantenimiento();
        Long id = 1L;
        Maquinaria maquinaria = new Maquinaria();
        LocalDate fecha = LocalDate.now();
        String detalle = "Cambio de aceite y filtros.";

        mantenimiento.setId(id);
        mantenimiento.setMaquinaria(maquinaria);
        mantenimiento.setFecha(fecha);
        mantenimiento.setDetalle(detalle);

        assertEquals(id, mantenimiento.getId());
        assertEquals(maquinaria, mantenimiento.getMaquinaria());
        assertEquals(fecha, mantenimiento.getFecha());
        assertEquals(detalle, mantenimiento.getDetalle());
    }
}
