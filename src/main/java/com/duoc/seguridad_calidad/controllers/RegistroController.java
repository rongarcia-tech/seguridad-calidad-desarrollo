package com.duoc.seguridad_calidad.controllers;

import com.duoc.seguridad_calidad.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegistroController {

    private final UsuarioService usuarioService;

    public RegistroController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Operation(
        summary = "Formulario de registro",
        description = "Muestra la página para registrar un nuevo usuario."
    )
    @GetMapping("/register")
    public String form() { 
        return "register"; 
    }

    @Operation(
        summary = "Procesar registro de usuario",
        description = "Registra un nuevo usuario con nombre, email y contraseña. "
                    + "Si ocurre un error, retorna al formulario con mensaje de error."
    )
    @PostMapping("/register")
    public String submit(@RequestParam String nombre,
                         @RequestParam String email,
                         @RequestParam String password,
                         Model model) {
        try {
            usuarioService.registrar(nombre, email, password);
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
}
