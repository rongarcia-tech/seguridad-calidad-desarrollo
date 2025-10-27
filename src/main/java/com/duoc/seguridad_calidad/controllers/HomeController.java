package com.duoc.seguridad_calidad.controllers;

import com.duoc.seguridad_calidad.services.AvisoService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class HomeController {
    private final AvisoService avisoService;

    public HomeController(AvisoService avisoService) {
        this.avisoService = avisoService;
    }
    @Operation(summary = "Saludo simple", description = "Devuelve un saludo")
    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("destacados", avisoService.destacados());
        return "home";
    }
}
