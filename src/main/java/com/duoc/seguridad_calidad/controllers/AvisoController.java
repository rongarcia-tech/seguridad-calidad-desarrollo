package com.duoc.seguridad_calidad.controllers;

import com.duoc.seguridad_calidad.domain.*;
import com.duoc.seguridad_calidad.repositories.MaquinariaRepository;
import com.duoc.seguridad_calidad.repositories.UsuarioRepository;
import com.duoc.seguridad_calidad.services.AvisoService;
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
    private final AvisoService avisoService;

    public AvisoController(MaquinariaRepository maqRepo, UsuarioRepository userRepo, AvisoService avisoService) {
        this.maqRepo = maqRepo;
        this.userRepo = userRepo;
        this.avisoService = avisoService;
    }

    @GetMapping("/nuevo")
    public String form(Authentication auth, Model model) {
        Usuario u = userRepo.findByEmail(auth.getName()).orElseThrow();
        // simplificación: mostrar todas las maquinarias del dueño (si tu entidad Maquinaria tiene dueno)
        List<Maquinaria> mis = maqRepo.findAll().stream().filter(m -> m.getDueno().getId().equals(u.getId())).toList();
        model.addAttribute("misMaquinarias", mis);
        return "aviso-form";
    }

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

    @GetMapping("/mios")
    public String misAvisos(Authentication auth, Model model) {
        var user = userRepo.findByEmail(auth.getName()).orElseThrow();
        // filtra por dueño
        var misAvisos = maqRepo.findAll().stream()
                .filter(a -> a.getDueno().getId().equals(user.getId()))
                .toList();
        model.addAttribute("avisos", misAvisos);
        return "avisos-mios";
    }
}
