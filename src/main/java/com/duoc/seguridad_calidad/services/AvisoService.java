package com.duoc.seguridad_calidad.services;

import com.duoc.seguridad_calidad.domain.Aviso;
import com.duoc.seguridad_calidad.domain.Maquinaria;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface AvisoService {
    Aviso publicar(Maquinaria maquinaria, LocalDate desde, LocalDate hasta,
                   BigDecimal precioPorDia, boolean destacado,
                   String condiciones, com.duoc.seguridad_calidad.domain.MedioPago medioPago);
    List<Aviso> destacados();
}
