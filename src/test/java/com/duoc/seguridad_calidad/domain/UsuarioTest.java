package com.duoc.seguridad_calidad.domain;

import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    @Test
    void testUsuarioGettersAndSetters() {
        Usuario usuario = new Usuario();
        Long id = 1L;
        String nombre = "Test User";
        String email = "test@example.com";
        String passwordHash = "hashedPassword";
        Set<String> roles = Set.of("USER", "ADMIN");
        Perfil perfil = new Perfil();

        usuario.setId(id);
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setPasswordHash(passwordHash);
        usuario.setRoles(roles);
        usuario.setPerfil(perfil);

        assertEquals(id, usuario.getId());
        assertEquals(nombre, usuario.getNombre());
        assertEquals(email, usuario.getEmail());
        assertEquals(passwordHash, usuario.getPasswordHash());
        assertEquals(roles, usuario.getRoles());
        assertEquals(perfil, usuario.getPerfil());
    }
}
