package com.duoc.seguridad_calidad.domain;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class MaquinariaTest {

    @Test
    void testMaquinariaGettersAndSetters() {
        Maquinaria maquinaria = new Maquinaria();
        Long id = 1L;
        TipoMaquinaria tipo = TipoMaquinaria.EXCAVADORA;
        String marca = "Caterpillar";
        String modelo = "320D";
        Integer anio = 2020;
        String capacidad = "1.2 m3";
        String comuna = "Santiago";
        String region = "Metropolitana";
        BigDecimal precio = new BigDecimal("150000");
        Usuario dueno = new Usuario();
        List<Mantenimiento> mantenciones = new ArrayList<>();

        maquinaria.setId(id);
        maquinaria.setTipo(tipo);
        maquinaria.setMarca(marca);
        maquinaria.setModelo(modelo);
        maquinaria.setAnioFabricacion(anio);
        maquinaria.setCapacidad(capacidad);
        maquinaria.setUbicacionComuna(comuna);
        maquinaria.setUbicacionRegion(region);
        maquinaria.setPrecioPorDia(precio);
        maquinaria.setDueno(dueno);
        maquinaria.setMantenciones(mantenciones);

        assertEquals(id, maquinaria.getId());
        assertEquals(tipo, maquinaria.getTipo());
        assertEquals(marca, maquinaria.getMarca());
        assertEquals(modelo, maquinaria.getModelo());
        assertEquals(anio, maquinaria.getAnioFabricacion());
        assertEquals(capacidad, maquinaria.getCapacidad());
        assertEquals(comuna, maquinaria.getUbicacionComuna());
        assertEquals(region, maquinaria.getUbicacionRegion());
        assertEquals(precio, maquinaria.getPrecioPorDia());
        assertEquals(dueno, maquinaria.getDueno());
        assertEquals(mantenciones, maquinaria.getMantenciones());
    }
}
