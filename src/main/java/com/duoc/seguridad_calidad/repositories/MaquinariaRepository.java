package com.duoc.seguridad_calidad.repositories;

import com.duoc.seguridad_calidad.domain.Maquinaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MaquinariaRepository extends JpaRepository<Maquinaria, Long>, JpaSpecificationExecutor<Maquinaria> {
    // Usaremos Specifications para la búsqueda por tipo/ubicación/fecha/precio
}
