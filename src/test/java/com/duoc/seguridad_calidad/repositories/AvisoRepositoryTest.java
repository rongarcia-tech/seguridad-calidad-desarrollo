package com.duoc.seguridad_calidad.repositories;

import com.duoc.seguridad_calidad.domain.Aviso;
import com.duoc.seguridad_calidad.domain.Maquinaria;
import com.duoc.seguridad_calidad.domain.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class AvisoRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AvisoRepository avisoRepository;

    private Usuario dueno;
    private Maquinaria maquinaria;

    @BeforeEach
    void setUp() {
        dueno = new Usuario();
        dueno.setEmail("dueno@test.com");
        dueno.setNombre("Dueño Test");
        dueno.setPasswordHash("password");
        entityManager.persist(dueno);

        maquinaria = new Maquinaria();
        maquinaria.setMarca("Test Marca");
        maquinaria.setModelo("Test Model");
        maquinaria.setDueno(dueno);
        entityManager.persist(maquinaria);
        entityManager.flush();
    }

    @Test
    void findTop10ByDestacadoTrueOrderByIdDesc_returnsDestacadoAvisos() {
        for (int i = 0; i < 12; i++) {
            Aviso aviso = new Aviso();
            aviso.setCondicionesArriendo("Aviso " + i);
            aviso.setDestacado(i < 10); // Destaca los primeros 10
            aviso.setMaquinaria(maquinaria);
            entityManager.persist(aviso);
        }
        entityManager.flush();

        List<Aviso> avisos = avisoRepository.findTop10ByDestacadoTrueOrderByIdDesc();

        assertThat(avisos).hasSize(10);
        assertThat(avisos.get(0).getCondicionesArriendo()).isEqualTo("Aviso 9");
    }

    @Test
    void findByMaquinaria_Dueno_IdOrderByIdDesc_returnsAvisosForDueno() {
        Aviso a1 = new Aviso();
        a1.setCondicionesArriendo("Aviso 1");
        a1.setMaquinaria(maquinaria);
        entityManager.persist(a1);

        Aviso a2 = new Aviso();
        a2.setCondicionesArriendo("Aviso 2");
        a2.setMaquinaria(maquinaria);
        entityManager.persist(a2);
        entityManager.flush();

        List<Aviso> avisos = avisoRepository.findByMaquinaria_Dueno_IdOrderByIdDesc(dueno.getId());

        assertThat(avisos).hasSize(2);
        assertThat(avisos.get(0).getCondicionesArriendo()).isEqualTo("Aviso 2");
        assertThat(avisos.get(1).getCondicionesArriendo()).isEqualTo("Aviso 1");
    }
}
