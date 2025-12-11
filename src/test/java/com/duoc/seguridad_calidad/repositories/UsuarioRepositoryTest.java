package com.duoc.seguridad_calidad.repositories;

import com.duoc.seguridad_calidad.domain.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UsuarioRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    void findByEmail_returnsUser_whenUserExists() {
        Usuario usuario = new Usuario();
        usuario.setEmail("test@test.com");
        usuario.setNombre("Test User");
        usuario.setPasswordHash("password");
        entityManager.persistAndFlush(usuario);

        Optional<Usuario> found = usuarioRepository.findByEmail("test@test.com");

        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("test@test.com");
    }

    @Test
    void findByEmail_returnsEmpty_whenUserDoesNotExist() {
        Optional<Usuario> found = usuarioRepository.findByEmail("nonexistent@test.com");

        assertThat(found).isNotPresent();
    }

    @Test
    void existsByEmail_returnsTrue_whenUserExists() {
        Usuario usuario = new Usuario();
        usuario.setEmail("test@test.com");
        usuario.setNombre("Test User");
        usuario.setPasswordHash("password");
        entityManager.persistAndFlush(usuario);

        boolean exists = usuarioRepository.existsByEmail("test@test.com");

        assertThat(exists).isTrue();
    }

    @Test
    void existsByEmail_returnsFalse_whenUserDoesNotExist() {
        boolean exists = usuarioRepository.existsByEmail("nonexistent@test.com");

        assertThat(exists).isFalse();
    }
}
