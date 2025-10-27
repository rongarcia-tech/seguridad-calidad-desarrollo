package com.duoc.seguridad_calidad.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "avisos")
public class Aviso {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maquinaria_id", nullable = false)
    private Maquinaria maquinaria;

    private LocalDate disponibleDesde;
    private LocalDate disponibleHasta;

    private BigDecimal precioPorDia;      // puede sobreescribir el de maquinaria
    private boolean destacado;

    @Enumerated(EnumType.STRING)
    private MedioPago medioPago;          // simple (uno). Cambia a Set si quieres varios.

    @Column(length = 2000)
    private String condicionesArriendo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Maquinaria getMaquinaria() {
        return maquinaria;
    }

    public void setMaquinaria(Maquinaria maquinaria) {
        this.maquinaria = maquinaria;
    }

    public LocalDate getDisponibleDesde() {
        return disponibleDesde;
    }

    public void setDisponibleDesde(LocalDate disponibleDesde) {
        this.disponibleDesde = disponibleDesde;
    }

    public LocalDate getDisponibleHasta() {
        return disponibleHasta;
    }

    public void setDisponibleHasta(LocalDate disponibleHasta) {
        this.disponibleHasta = disponibleHasta;
    }

    public BigDecimal getPrecioPorDia() {
        return precioPorDia;
    }

    public void setPrecioPorDia(BigDecimal precioPorDia) {
        this.precioPorDia = precioPorDia;
    }

    public boolean isDestacado() {
        return destacado;
    }

    public void setDestacado(boolean destacado) {
        this.destacado = destacado;
    }

    public MedioPago getMedioPago() {
        return medioPago;
    }

    public void setMedioPago(MedioPago medioPago) {
        this.medioPago = medioPago;
    }

    public String getCondicionesArriendo() {
        return condicionesArriendo;
    }

    public void setCondicionesArriendo(String condicionesArriendo) {
        this.condicionesArriendo = condicionesArriendo;
    }
}

