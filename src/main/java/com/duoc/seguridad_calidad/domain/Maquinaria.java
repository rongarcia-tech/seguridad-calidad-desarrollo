package com.duoc.seguridad_calidad.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "maquinarias")
public class Maquinaria {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoMaquinaria tipo;

    private String marca;
    private String modelo;
    private Integer anioFabricacion;
    private String capacidad;         // ej. litros, hp, m3 (string para simplificar)
    private String ubicacionComuna;   // simplificado
    private String ubicacionRegion;

    private BigDecimal precioPorDia;  // precio base referencial

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dueno_id", nullable = false)
    private Usuario dueno;

    @OneToMany(mappedBy = "maquinaria", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mantenimiento> mantenciones = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoMaquinaria getTipo() {
        return tipo;
    }

    public void setTipo(TipoMaquinaria tipo) {
        this.tipo = tipo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Integer getAnioFabricacion() {
        return anioFabricacion;
    }

    public void setAnioFabricacion(Integer anioFabricacion) {
        this.anioFabricacion = anioFabricacion;
    }

    public String getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(String capacidad) {
        this.capacidad = capacidad;
    }

    public String getUbicacionComuna() {
        return ubicacionComuna;
    }

    public void setUbicacionComuna(String ubicacionComuna) {
        this.ubicacionComuna = ubicacionComuna;
    }

    public String getUbicacionRegion() {
        return ubicacionRegion;
    }

    public void setUbicacionRegion(String ubicacionRegion) {
        this.ubicacionRegion = ubicacionRegion;
    }

    public BigDecimal getPrecioPorDia() {
        return precioPorDia;
    }

    public void setPrecioPorDia(BigDecimal precioPorDia) {
        this.precioPorDia = precioPorDia;
    }

    public Usuario getDueno() {
        return dueno;
    }

    public void setDueno(Usuario dueno) {
        this.dueno = dueno;
    }

    public List<Mantenimiento> getMantenciones() {
        return mantenciones;
    }

    public void setMantenciones(List<Mantenimiento> mantenciones) {
        this.mantenciones = mantenciones;
    }
}

