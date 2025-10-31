package com.duoc.seguridad_calidad.dtos;

import com.duoc.seguridad_calidad.domain.Maquinaria;
import com.duoc.seguridad_calidad.domain.TipoMaquinaria;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class MaquinariaForm {
    @NotNull
    private TipoMaquinaria tipo;
    @NotBlank
    private String marca;
    @NotBlank
    private String modelo;
    @Min(1900) @Max(2100)
    private Integer anioFabricacion;
    private String capacidad;
    @NotBlank
    private String ubicacionComuna;
    @NotBlank
    private String ubicacionRegion;
    @NotNull @DecimalMin("0.0")
    private BigDecimal precioPorDia;

    // getters/setters

    public Maquinaria toEntity() {
        Maquinaria m = new Maquinaria();
        m.setTipo(tipo);
        m.setMarca(marca);
        m.setModelo(modelo);
        m.setAnioFabricacion(anioFabricacion);
        m.setCapacidad(capacidad);
        m.setUbicacionComuna(ubicacionComuna);
        m.setUbicacionRegion(ubicacionRegion);
        m.setPrecioPorDia(precioPorDia);
        return m;
    }
}

