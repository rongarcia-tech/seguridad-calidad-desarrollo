package com.duoc.seguridad_calidad.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @Operation(
        summary = "Página de inicio de sesión",
        description = "Muestra el formulario de login o redirige al home si el usuario ya ha iniciado sesión."
    )
    @GetMapping("/login")
    public String login(Authentication authentication) {
        if (authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            return "redirect:/home";
        }
        return "login"; // templates/login.html
    }
}
