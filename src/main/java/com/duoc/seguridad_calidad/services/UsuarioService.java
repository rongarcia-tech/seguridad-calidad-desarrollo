package com.duoc.seguridad_calidad.services;

import com.duoc.seguridad_calidad.domain.Usuario;

public interface UsuarioService {
    Usuario registrar(String nombre, String email, String rawPassword);
    Usuario buscarPorEmail(String email);
}
