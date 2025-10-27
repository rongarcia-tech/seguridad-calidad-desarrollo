package com.duoc.seguridad_calidad.controllers;

import com.duoc.seguridad_calidad.domain.Maquinaria;
import com.duoc.seguridad_calidad.domain.TipoMaquinaria;
import com.duoc.seguridad_calidad.services.MaquinariaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Controller
public class BuscarController {

    private final MaquinariaService maquinariaService;

    public BuscarController(MaquinariaService maquinariaService) {
        this.maquinariaService = maquinariaService;
    }

    @GetMapping("/buscar")
    public String buscar(@RequestParam(required = false) TipoMaquinaria tipo,
                         @RequestParam(required = false) String region,
                         @RequestParam(required = false) String comuna,
                         @RequestParam(required = false) String desde,
                         @RequestParam(required = false) String hasta,
                         @RequestParam(required = false) BigDecimal precioMax,
                         Model model) {

        model.addAttribute("tipos", Arrays.asList(TipoMaquinaria.values()));

        if (tipo != null || (region != null && !region.isBlank()) || (comuna != null && !comuna.isBlank())
                || (desde != null && !desde.isBlank()) || (hasta != null && !hasta.isBlank()) || precioMax != null) {

            LocalDate fDesde = (desde != null && !desde.isBlank()) ? LocalDate.parse(desde) : null;
            LocalDate fHasta = (hasta != null && !hasta.isBlank()) ? LocalDate.parse(hasta) : null;

            List<Maquinaria> rs = maquinariaService.buscar(tipo, region, comuna, fDesde, fHasta, precioMax);
            model.addAttribute("resultados", rs);
        }
        return "buscar";
    }
}
