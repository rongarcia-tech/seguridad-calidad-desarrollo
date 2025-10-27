package com.duoc.seguridad_calidad.services.impl;

import com.duoc.seguridad_calidad.domain.Maquinaria;
import com.duoc.seguridad_calidad.domain.TipoMaquinaria;
import com.duoc.seguridad_calidad.repositories.MaquinariaRepository;
import com.duoc.seguridad_calidad.services.MaquinariaService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class MaquinariaServiceImpl implements MaquinariaService {

    private final MaquinariaRepository repo;

    public MaquinariaServiceImpl(MaquinariaRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<Maquinaria> buscar(TipoMaquinaria tipo, String region, String comuna,
                                   LocalDate fechaDesde, LocalDate fechaHasta,
                                   BigDecimal precioMax) {

        // 1) Empezamos con un Specification "siempre verdadero"
        Specification<Maquinaria> spec = (root, q, cb) -> cb.conjunction();

        // 2) Vamos agregando condiciones
        if (tipo != null) {
            spec = spec.and((root, q, cb) -> cb.equal(root.get("tipo"), tipo));
        }
        if (region != null && !region.isBlank()) {
            spec = spec.and((root, q, cb) -> cb.equal(root.get("ubicacionRegion"), region));
        }
        if (comuna != null && !comuna.isBlank()) {
            spec = spec.and((root, q, cb) -> cb.equal(root.get("ubicacionComuna"), comuna));
        }
        if (precioMax != null) {
            spec = spec.and((root, q, cb) -> cb.lessThanOrEqualTo(root.get("precioPorDia"), precioMax));
        }

        // 3) (Opcional) Si quieres forzar resultados únicos
        //    q.distinct(true) se debe aplicar en un único lugar:
        spec = spec.and((root, q, cb) -> {
            q.distinct(true);
            return cb.conjunction();
        });

        // Nota: El filtrado real por disponibilidad (fechaDesde/fechaHasta)
        // se hace mejor cruzando Aviso/Reserva. Lo podemos añadir después.
        // Por ahora, se ignoran esas fechas para evitar un Criteria complejo.

        return repo.findAll(spec);
    }
}

