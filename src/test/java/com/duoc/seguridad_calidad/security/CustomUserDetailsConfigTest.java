package com.duoc.seguridad_calidad.security;

import com.duoc.seguridad_calidad.domain.Usuario;
import com.duoc.seguridad_calidad.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {CustomUserDetailsConfig.class})
class CustomUserDetailsConfigTest {

    @Autowired
    private UserDetailsService userDetailsService;

    @MockitoBean
    private UsuarioRepository usuarioRepository;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setEmail("test@test.com");
        usuario.setPasswordHash("hashedPassword");
        usuario.setRoles(Set.of("USER", "ADMIN"));
    }

    @Test
    void userDetailsService_whenUserFound_shouldReturnUserDetails() {
        when(usuarioRepository.findByEmail("test@test.com")).thenReturn(Optional.of(usuario));

        UserDetails userDetails = userDetailsService.loadUserByUsername("test@test.com");

        assertNotNull(userDetails);
        assertEquals("test@test.com", userDetails.getUsername());
        assertEquals("hashedPassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
        assertEquals(2, userDetails.getAuthorities().size());
    }

    @Test
    void userDetailsService_whenUserNotFound_shouldThrowException() {
        when(usuarioRepository.findByEmail("notfound@test.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("notfound@test.com");
        });
    }
}
