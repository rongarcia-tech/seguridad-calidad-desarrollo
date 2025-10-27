package com.duoc.seguridad_calidad.controllers;

import com.duoc.seguridad_calidad.domain.Aviso;
import com.duoc.seguridad_calidad.domain.Usuario;
import com.duoc.seguridad_calidad.repositories.AvisoRepository;
import com.duoc.seguridad_calidad.repositories.ReservaRepository;
import com.duoc.seguridad_calidad.repositories.UsuarioRepository;
import com.duoc.seguridad_calidad.services.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/reservas")
public class ReservaController {

    private final AvisoRepository avisoRepo;
    private final UsuarioRepository userRepo;
    private final ReservaRepository reservaRepo;
    private final ReservaService reservaService;

    public ReservaController(AvisoRepository avisoRepo, UsuarioRepository userRepo, ReservaRepository reservaRepo, ReservaService reservaService) {
        this.avisoRepo = avisoRepo;
        this.userRepo = userRepo;
        this.reservaRepo = reservaRepo;
        this.reservaService = reservaService;
    }

    @Operation(
        summary = "Formulario de nueva reserva",
        description = "Muestra el formulario para crear una nueva reserva para un aviso específico."
    )
    @GetMapping("/nueva")
    public String form(@RequestParam Long avisoId, Model model) {
        Aviso a = avisoRepo.findById(avisoId).orElseThrow();
        model.addAttribute("aviso", a);
        return "reserva-form";
    }

    @Operation(
        summary = "Crear nueva reserva",
        description = "Registra una nueva reserva para el usuario autenticado sobre un aviso determinado, con fechas de inicio y fin."
    )
    @PostMapping
    public String crear(Authentication auth,
                        @RequestParam Long avisoId,
                        @RequestParam String inicio,
                        @RequestParam String fin) {
        Aviso a = avisoRepo.findById(avisoId).orElseThrow();
        Usuario u = userRepo.findByEmail(auth.getName()).orElseThrow();
        reservaService.reservar(a, u, LocalDate.parse(inicio), LocalDate.parse(fin));
        return "redirect:/home";
    }

    @Operation(
        summary = "Listar mis reservas",
        description = "Muestra todas las reservas realizadas por el usuario autenticado."
    )
    @GetMapping("/mias")
    public String misReservas(Authentication auth, Model model) {
        var user = userRepo.findByEmail(auth.getName()).orElseThrow();
        var mias = reservaRepo.findAll().stream()
                .filter(r -> r.getArrendatario().getId().equals(user.getId()))
                .toList();
        model.addAttribute("reservas", mias);
        return "reservas-mias";
    }
}
