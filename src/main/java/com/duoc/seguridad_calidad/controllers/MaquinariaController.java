package com.duoc.seguridad_calidad.controllers;

import com.duoc.seguridad_calidad.domain.Aviso;
import com.duoc.seguridad_calidad.domain.Maquinaria;
import com.duoc.seguridad_calidad.domain.TipoMaquinaria;
import com.duoc.seguridad_calidad.dtos.MaquinariaForm;
import com.duoc.seguridad_calidad.repositories.AvisoRepository;
import com.duoc.seguridad_calidad.repositories.MaquinariaRepository;
import com.duoc.seguridad_calidad.repositories.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.Optional;

@Controller
@RequestMapping("/maquinaria")
public class MaquinariaController {

    private final MaquinariaRepository maquinariaRepository;
    private final AvisoRepository avisoRepository;
    private final UsuarioRepository usuarioRepository;
    TipoMaquinaria[] values = TipoMaquinaria.values();

    public MaquinariaController(MaquinariaRepository maquinariaRepository, AvisoRepository avisoRepository, UsuarioRepository usuarioRepository) {
        this.maquinariaRepository = maquinariaRepository;
        this.avisoRepository = avisoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Operation(
        summary = "Ver detalle de maquinaria",
        description = "Muestra la información detallada de una maquinaria y su aviso asociado, si existe."
    )
    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        Optional<Maquinaria> maq = maquinariaRepository.findById(id);
        if (maq.isEmpty()) return "redirect:/buscar";

        // Simplificación: toma el último aviso asociado a la maquinaria
       /* Aviso aviso = avisoRepository.findAll().stream()
                .filter(a -> a.getMaquinaria().getId().equals(id))
                .reduce((first, second) -> second).orElse(null);*/

        model.addAttribute("maq", maq.get());
        //model.addAttribute("aviso", aviso);
        return "maquinaria-detalle";
    }

    @Operation(
            summary = "Listar mis maquinarias",
            description = "Muestra las maquinarias cuyo dueño es el usuario autenticado."
    )
    @GetMapping("/mias")
    public String mias(Authentication auth, Model model) {
        var user = usuarioRepository.findByEmail(auth.getName()).orElseThrow();
        var lista = maquinariaRepository.findByDuenoIdOrderByIdDesc(user.getId());
        model.addAttribute("misMaquinarias", lista);
        return "maquinaria-mias";
    }
    @Operation(
            summary = "Formulario nueva maquinaria",
            description = "Muestra el formulario para registrar una maquinaria propia."
    )
    @GetMapping("/nueva")
    public String nueva(Model model) {
        System.out.println("=== DEBUG tipos ===");
        Arrays.stream(values).forEach(v -> System.out.println("TipoMaquinaria: " + v.name()));
        System.out.println("===================");

        model.addAttribute("maquinaria", new Maquinaria());
        model.addAttribute("tipos", values);

        return "maquinaria-form";
    }


    @Operation(
            summary = "Crear maquinaria",
            description = "Crea una maquinaria y la asocia al usuario autenticado como dueño."
    )
    @PostMapping
    public String crear(@ModelAttribute("maquinaria") Maquinaria maquinaria,
                        Authentication auth,
                        RedirectAttributes ra) {
        var dueno = usuarioRepository.findByEmail(auth.getName()).orElseThrow();
        maquinaria.setDueno(dueno);               // dueño desde el contexto
        maquinariaRepository.save(maquinaria);
        ra.addFlashAttribute("ok", "Maquinaria creada.");
        return "redirect:/maquinaria/mias";
    }
}
