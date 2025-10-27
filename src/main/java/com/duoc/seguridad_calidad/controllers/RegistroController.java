package com.duoc.seguridad_calidad.controllers;

import com.duoc.seguridad_calidad.services.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegistroController {

    private final UsuarioService usuarioService;

    public RegistroController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/register")
    public String form() { return "register"; }

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
