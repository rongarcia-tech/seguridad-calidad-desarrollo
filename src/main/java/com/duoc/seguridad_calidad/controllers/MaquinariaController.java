package com.duoc.seguridad_calidad.controllers;

import com.duoc.seguridad_calidad.domain.Aviso;
import com.duoc.seguridad_calidad.domain.Maquinaria;
import com.duoc.seguridad_calidad.repositories.AvisoRepository;
import com.duoc.seguridad_calidad.repositories.MaquinariaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/maquinaria")
public class MaquinariaController {

    private final MaquinariaRepository maquinariaRepository;
    private final AvisoRepository avisoRepository;

    public MaquinariaController(MaquinariaRepository maquinariaRepository, AvisoRepository avisoRepository) {
        this.maquinariaRepository = maquinariaRepository;
        this.avisoRepository = avisoRepository;
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        Optional<Maquinaria> maq = maquinariaRepository.findById(id);
        if (maq.isEmpty()) return "redirect:/buscar";
        // Simplificación: tomar el último aviso de esa maquinaria (podrías crear un método repo)
        Aviso aviso = avisoRepository.findAll().stream()
                .filter(a -> a.getMaquinaria().getId().equals(id))
                .reduce((first, second) -> second).orElse(null);

        model.addAttribute("maq", maq.get());
        model.addAttribute("aviso", aviso);
        return "maquinaria-detalle";
    }
}
