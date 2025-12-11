package com.duoc.seguridad_calidad.repositories;

import com.duoc.seguridad_calidad.domain.Maquinaria;
import com.duoc.seguridad_calidad.domain.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class MaquinariaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MaquinariaRepository maquinariaRepository;

    private Usuario dueno;

    @BeforeEach
    void setUp() {
        dueno = new Usuario();
        dueno.setEmail("dueno@test.com");
        dueno.setNombre("Dueño Test");
        dueno.setPasswordHash("password");
        entityManager.persistAndFlush(dueno);
    }

    @Test
    void findByDuenoIdOrderByIdDesc_returnsMaquinaria_whenExists() {
        Maquinaria m1 = new Maquinaria();
        m1.setMarca("Maquina 1");
        m1.setDueno(dueno);
        entityManager.persist(m1);

        Maquinaria m2 = new Maquinaria();
        m2.setMarca("Maquina 2");
        m2.setDueno(dueno);
        entityManager.persist(m2);
        entityManager.flush();

        List<Maquinaria> maquinarias = maquinariaRepository.findByDuenoIdOrderByIdDesc(dueno.getId());

        assertThat(maquinarias).hasSize(2);
        assertThat(maquinarias.get(0).getMarca()).isEqualTo("Maquina 2");
        assertThat(maquinarias.get(1).getMarca()).isEqualTo("Maquina 1");
    }

    @Test
    void findByIdAndDuenoEmail_returnsMaquinaria_whenExists() {
        Maquinaria maquinaria = new Maquinaria();
        maquinaria.setMarca("Maquina Test");
        maquinaria.setDueno(dueno);
        entityManager.persistAndFlush(maquinaria);

        Optional<Maquinaria> found = maquinariaRepository.findByIdAndDuenoEmail(maquinaria.getId(), dueno.getEmail());

        assertThat(found).isPresent();
        assertThat(found.get().getMarca()).isEqualTo("Maquina Test");
    }

    @Test
    void findByIdAndDuenoEmail_returnsEmpty_whenMaquinariaDoesNotExist() {
        Optional<Maquinaria> found = maquinariaRepository.findByIdAndDuenoEmail(999L, dueno.getEmail());

        assertThat(found).isNotPresent();
    }

    @Test
    void findByIdAndDuenoEmail_returnsEmpty_whenDuenoDoesNotMatch() {
        Maquinaria maquinaria = new Maquinaria();
        maquinaria.setMarca("Maquina Test");
        maquinaria.setDueno(dueno);
        entityManager.persistAndFlush(maquinaria);

        Optional<Maquinaria> found = maquinariaRepository.findByIdAndDuenoEmail(maquinaria.getId(), "otro@test.com");

        assertThat(found).isNotPresent();
    }
}
