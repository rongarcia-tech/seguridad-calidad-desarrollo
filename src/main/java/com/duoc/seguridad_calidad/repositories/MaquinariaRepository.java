package com.duoc.seguridad_calidad.repositories;

import com.duoc.seguridad_calidad.domain.Maquinaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface MaquinariaRepository extends JpaRepository<Maquinaria, Long>, JpaSpecificationExecutor<Maquinaria> {
    List<Maquinaria> findByDuenoIdOrderByIdDesc(Long duenoId);
    Optional<Maquinaria> findByIdAndDuenoEmail(Long id, String email);
}
