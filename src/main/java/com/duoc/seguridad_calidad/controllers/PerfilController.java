package com.duoc.seguridad_calidad.controllers;

import com.duoc.seguridad_calidad.domain.Perfil;
import com.duoc.seguridad_calidad.domain.Usuario;
import com.duoc.seguridad_calidad.repositories.PerfilRepository;
import com.duoc.seguridad_calidad.repositories.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/perfil")
public class PerfilController {

    private final PerfilRepository perfilRepo;
    private final UsuarioRepository usuarioRepo;

    public PerfilController(PerfilRepository perfilRepo, UsuarioRepository usuarioRepo) {
        this.perfilRepo = perfilRepo;
        this.usuarioRepo = usuarioRepo;
    }

    @Operation(
        summary = "Ver perfil del usuario autenticado",
        description = "Muestra los datos del perfil del usuario actualmente autenticado."
    )
    @GetMapping
    public String ver(Authentication auth, Model model) {
        Usuario u = usuarioRepo.findByEmail(auth.getName()).orElseThrow();
        model.addAttribute("perfil", u.getPerfil());
        return "perfil";
    }

    @Operation(
        summary = "Guardar o actualizar perfil del usuario",
        description = "Guarda los cambios realizados en el perfil del usuario autenticado, incluyendo dirección, teléfono y cultivos."
    )
    @PostMapping
    public String guardar(Authentication auth,
                          @RequestParam String direccion,
                          @RequestParam String telefono,
                          @RequestParam String cultivos) {
        Usuario u = usuarioRepo.findByEmail(auth.getName()).orElseThrow();
        Perfil p = u.getPerfil();
        if (p == null) { 
            p = new Perfil(); 
            p.setUsuario(u); 
        }
        p.setDireccion(direccion);
        p.setTelefono(telefono);
        p.setCultivos(cultivos);
        perfilRepo.save(p);
        return "redirect:/perfil";
    }
}
