package com.duoc.seguridad_calidad.repositories;

import com.duoc.seguridad_calidad.domain.Perfil;
import com.duoc.seguridad_calidad.domain.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class PerfilRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PerfilRepository perfilRepository;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setEmail("usuario@test.com");
        usuario.setNombre("Usuario Test");
        usuario.setPasswordHash("password");
        entityManager.persistAndFlush(usuario);
    }

    @Test
    void saveAndFindById_works() {
        Perfil perfil = new Perfil();
        perfil.setUsuario(usuario);
        perfil.setTelefono("123456789");
        perfil = perfilRepository.save(perfil);

        Optional<Perfil> found = perfilRepository.findById(perfil.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getTelefono()).isEqualTo("123456789");
    }
}
