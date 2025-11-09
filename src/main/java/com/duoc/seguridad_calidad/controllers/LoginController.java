package com.duoc.seguridad_calidad.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Operation(
        summary = "Página de inicio de sesión",
        description = "Muestra el formulario de login o redirige al home si el usuario ya ha iniciado sesión."
    )
    
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model,
                        Authentication authentication) {

        if (authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            return "redirect:/home";
        }

        if (error != null) {
            model.addAttribute("error", "Credenciales inválidas");
        }
        if (logout != null) {
            model.addAttribute("mensaje", "Has cerrado sesión correctamente.");
        }

        return "login";
    }
}

