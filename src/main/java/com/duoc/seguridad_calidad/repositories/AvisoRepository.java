package com.duoc.seguridad_calidad.repositories;

import com.duoc.seguridad_calidad.domain.Aviso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AvisoRepository extends JpaRepository<Aviso, Long> {
    List<Aviso> findTop10ByDestacadoTrueOrderByIdDesc();
    List<Aviso> findByMaquinaria_Dueno_IdOrderByIdDesc(Long duenoId);
}
