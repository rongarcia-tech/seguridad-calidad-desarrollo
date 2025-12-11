package com.duoc.seguridad_calidad.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PerfilTest {

    @Test
    void testPerfilGettersAndSetters() {
        Perfil perfil = new Perfil();
        Long id = 1L;
        String direccion = "Av. Siempre Viva 742";
        String telefono = "+56912345678";
        String cultivos = "Maíz, Trigo";
        Usuario usuario = new Usuario();

        perfil.setId(id);
        perfil.setDireccion(direccion);
        perfil.setTelefono(telefono);
        perfil.setCultivos(cultivos);
        perfil.setUsuario(usuario);

        assertEquals(id, perfil.getId());
        assertEquals(direccion, perfil.getDireccion());
        assertEquals(telefono, perfil.getTelefono());
        assertEquals(cultivos, perfil.getCultivos());
        assertEquals(usuario, perfil.getUsuario());
    }
}
