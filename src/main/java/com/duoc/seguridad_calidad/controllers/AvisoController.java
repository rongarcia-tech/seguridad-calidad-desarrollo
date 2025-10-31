package com.duoc.seguridad_calidad.controllers;

import com.duoc.seguridad_calidad.domain.*;
import com.duoc.seguridad_calidad.repositories.AvisoRepository;
import com.duoc.seguridad_calidad.repositories.MaquinariaRepository;
import com.duoc.seguridad_calidad.repositories.UsuarioRepository;
import com.duoc.seguridad_calidad.services.AvisoService;
import io.swagger.v3.oas.annotations.Operation; 
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/avisos")
public class AvisoController {

    private final MaquinariaRepository maqRepo;
    private final UsuarioRepository userRepo;
    private final AvisoRepository avisoRepo;
    private final AvisoService avisoService;

    public AvisoController(MaquinariaRepository maqRepo, UsuarioRepository userRepo, AvisoService avisoService, AvisoRepository avisoRepo) {
        this.maqRepo = maqRepo;
        this.userRepo = userRepo;
        this.avisoRepo = avisoRepo;
        this.avisoService = avisoService;
    }

    @Operation(
        summary = "Mostrar formulario para nuevo aviso",
        description = "Devuelve el formulario para crear un nuevo aviso de maquinaria. "
                    + "Solo muestra las maquinarias pertenecientes al usuario autenticado."
    )
    @GetMapping("/nuevo")
    public String form(Authentication auth, Model model) {
        Usuario u = userRepo.findByEmail(auth.getName()).orElseThrow();
        List<Maquinaria> mis = maqRepo.findAll().stream()
                .filter(m -> m.getDueno().getId().equals(u.getId()))
                .toList();
        model.addAttribute("misMaquinarias", mis);
        return "aviso-form";
    }

    @Operation(
        summary = "Publicar un nuevo aviso",
        description = "Crea un nuevo aviso asociado a una maquinaria existente, incluyendo fechas, precio, condiciones y medio de pago."
    )
    @PostMapping
    public String publicar(@RequestParam Long maquinariaId,
                           @RequestParam String desde,
                           @RequestParam String hasta,
                           @RequestParam BigDecimal precioPorDia,
                           @RequestParam(required = false) boolean destacado,
                           @RequestParam String condiciones,
                           @RequestParam MedioPago medioPago) {
        Maquinaria m = maqRepo.findById(maquinariaId).orElseThrow();
        avisoService.publicar(m, LocalDate.parse(desde), LocalDate.parse(hasta),
                precioPorDia, destacado, condiciones, medioPago);
        return "redirect:/home";
    }

    @Operation(
        summary = "Listar mis avisos",
        description = "Muestra todos los avisos publicados por el usuario autenticado, filtrando por dueño de la maquinaria."
    )
    @GetMapping("/mios")
    public String misAvisos(Authentication auth, Model model) {
        var user = userRepo.findByEmail(auth.getName()).orElseThrow();
        var misAvisos = avisoRepo.findByMaquinaria_Dueno_IdOrderByIdDesc(user.getId());
        model.addAttribute("avisos", misAvisos);
        return "avisos-mios";
    }
}
