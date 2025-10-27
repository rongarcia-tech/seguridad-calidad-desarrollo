package com.duoc.seguridad_calidad.services.impl;

import com.duoc.seguridad_calidad.domain.Usuario;
import com.duoc.seguridad_calidad.repositories.UsuarioRepository;
import com.duoc.seguridad_calidad.services.UsuarioService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository repo;
    private final PasswordEncoder encoder;

    public UsuarioServiceImpl(UsuarioRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @Override
    public Usuario registrar(String nombre, String email, String rawPassword) {
        if (repo.existsByEmail(email)) {
            throw new IllegalArgumentException("Email ya registrado");
        }
        Usuario u = new Usuario();
        u.setNombre(nombre);
        u.setEmail(email);
        u.setPasswordHash(encoder.encode(rawPassword));
        u.setRoles(Set.of("USER"));
        return repo.save(u);
    }

    @Override
    public Usuario buscarPorEmail(String email) {
        return repo.findByEmail(email).orElse(null);
    }
}
