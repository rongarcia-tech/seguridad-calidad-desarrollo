package com.duoc.seguridad_calidad.services;

import com.duoc.seguridad_calidad.domain.Aviso;
import com.duoc.seguridad_calidad.domain.Reserva;
import com.duoc.seguridad_calidad.domain.Usuario;

import java.time.LocalDate;

public interface ReservaService {
    Reserva reservar(Aviso aviso, Usuario arrendatario, LocalDate inicio, LocalDate fin);
}
