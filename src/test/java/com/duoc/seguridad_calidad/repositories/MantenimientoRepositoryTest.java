package com.duoc.seguridad_calidad.repositories;

import com.duoc.seguridad_calidad.domain.Mantenimiento;
import com.duoc.seguridad_calidad.domain.Maquinaria;
import com.duoc.seguridad_calidad.domain.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class MantenimientoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MantenimientoRepository mantenimientoRepository;

    private Maquinaria maquinaria;

    @BeforeEach
    void setUp() {
        Usuario dueno = new Usuario();
        dueno.setEmail("dueno@test.com");
        dueno.setNombre("Dueño Test");
        dueno.setPasswordHash("password");
        entityManager.persist(dueno);

        maquinaria = new Maquinaria();
        maquinaria.setMarca("Jhondier");
        maquinaria.setDueno(dueno);
        entityManager.persist(maquinaria);
        entityManager.flush();
    }

    @Test
    void saveAndFindById_works() {
        Mantenimiento mantenimiento = new Mantenimiento();
        mantenimiento.setDetalle("Mantenimiento de prueba");
        mantenimiento.setMaquinaria(maquinaria);
        mantenimiento.setFecha(LocalDate.now());
        mantenimiento = mantenimientoRepository.save(mantenimiento);

        Optional<Mantenimiento> found = mantenimientoRepository.findById(mantenimiento.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getDetalle()).isEqualTo("Mantenimiento de prueba");
        assertThat(found.get().getFecha()).isNotNull();
    }
}
