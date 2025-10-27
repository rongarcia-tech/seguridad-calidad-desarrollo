package com.duoc.seguridad_calidad.repositories;

import com.duoc.seguridad_calidad.domain.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
}
