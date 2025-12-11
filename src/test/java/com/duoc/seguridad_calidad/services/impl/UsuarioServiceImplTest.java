package com.duoc.seguridad_calidad.services.impl;

import com.duoc.seguridad_calidad.domain.Usuario;
import com.duoc.seguridad_calidad.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Test User");
        usuario.setEmail("test@test.com");
        usuario.setPasswordHash("encodedPassword");
    }

    @Test
    void registrarUsuarioExitosamente() {
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario nuevoUsuario = usuarioService.registrar("Test User", "test@test.com", "password");

        assertNotNull(nuevoUsuario);
        assertEquals("test@test.com", nuevoUsuario.getEmail());
        assertEquals("encodedPassword", nuevoUsuario.getPasswordHash());
    }

    @Test
    void registrarUsuarioConEmailExistente() {
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.registrar("Test User", "test@test.com", "password");
        });
    }

    @Test
    void buscarPorEmailExistente() {
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuario));

        Usuario encontrado = usuarioService.buscarPorEmail("test@test.com");

        assertNotNull(encontrado);
        assertEquals("test@test.com", encontrado.getEmail());
    }

    @Test
    void buscarPorEmailNoExistente() {
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        Usuario encontrado = usuarioService.buscarPorEmail("noexiste@test.com");

        assertNull(encontrado);
    }
}
