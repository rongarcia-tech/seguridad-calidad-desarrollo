package com.duoc.seguridad_calidad.services.impl;

import com.duoc.seguridad_calidad.domain.*;
import com.duoc.seguridad_calidad.repositories.ReservaRepository;
import com.duoc.seguridad_calidad.services.ReservaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@Transactional
public class ReservaServiceImpl implements ReservaService {

    private final ReservaRepository repo;

    public ReservaServiceImpl(ReservaRepository repo) {
        this.repo = repo;
    }

    @Override
    public Reserva reservar(Aviso aviso, Usuario arrendatario, LocalDate inicio, LocalDate fin) {
        if (inicio.isBefore(aviso.getDisponibleDesde()) || fin.isAfter(aviso.getDisponibleHasta()) || !fin.isAfter(inicio)) {
            throw new IllegalArgumentException("Rango de fechas inválido para este aviso");
        }
        long dias = ChronoUnit.DAYS.between(inicio, fin);
        BigDecimal total = aviso.getPrecioPorDia().multiply(BigDecimal.valueOf(dias));

        Reserva r = new Reserva();
        r.setMaquinaria(aviso.getMaquinaria());
        r.setArrendatario(arrendatario);
        r.setFechaInicio(inicio);
        r.setFechaFin(fin);
        r.setPrecioTotal(total);
        r.setEstado(EstadoReserva.PENDIENTE);

        return repo.save(r);
    }
}
