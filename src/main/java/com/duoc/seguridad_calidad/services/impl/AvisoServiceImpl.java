package com.duoc.seguridad_calidad.services.impl;

import com.duoc.seguridad_calidad.domain.Aviso;
import com.duoc.seguridad_calidad.domain.Maquinaria;
import com.duoc.seguridad_calidad.domain.MedioPago;
import com.duoc.seguridad_calidad.repositories.AvisoRepository;
import com.duoc.seguridad_calidad.services.AvisoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class AvisoServiceImpl implements AvisoService {

    private final AvisoRepository repo;

    public AvisoServiceImpl(AvisoRepository repo) {
        this.repo = repo;
    }

    @Override
    public Aviso publicar(Maquinaria m, LocalDate desde, LocalDate hasta,
                          BigDecimal precioPorDia, boolean destacado,
                          String condiciones, MedioPago medioPago) {
        Aviso a = new Aviso();
        a.setMaquinaria(m);
        a.setDisponibleDesde(desde);
        a.setDisponibleHasta(hasta);
        a.setPrecioPorDia(precioPorDia);
        a.setDestacado(destacado);
        a.setCondicionesArriendo(condiciones);
        a.setMedioPago(medioPago);
        return repo.save(a);
    }

    @Override
    public List<Aviso> destacados() {
        return repo.findTop10ByDestacadoTrueOrderByIdDesc();
    }

    @Override
    public Aviso buscarPorId(Long id) {
        return repo.findById(id).orElseThrow(() -> new java.util.NoSuchElementException("Aviso no encontrado con ID: " + id));
    }
}
