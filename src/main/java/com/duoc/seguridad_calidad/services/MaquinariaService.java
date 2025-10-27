package com.duoc.seguridad_calidad.services;

import com.duoc.seguridad_calidad.domain.Maquinaria;
import com.duoc.seguridad_calidad.domain.TipoMaquinaria;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface MaquinariaService {
    List<Maquinaria> buscar(TipoMaquinaria tipo, String region, String comuna,
                            LocalDate fechaDesde, LocalDate fechaHasta,
                            BigDecimal precioMax);
}
