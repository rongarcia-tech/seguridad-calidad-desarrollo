package com.duoc.seguridad_calidad.controllers;

import com.duoc.seguridad_calidad.domain.Aviso;
import com.duoc.seguridad_calidad.domain.Maquinaria;
import com.duoc.seguridad_calidad.domain.Reserva;
import com.duoc.seguridad_calidad.domain.Usuario;
import com.duoc.seguridad_calidad.repositories.AvisoRepository;
import com.duoc.seguridad_calidad.repositories.ReservaRepository;
import com.duoc.seguridad_calidad.repositories.UsuarioRepository;
import com.duoc.seguridad_calidad.services.ReservaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ReservaController.class)
@WithMockUser(username = "test@test.com") // para los métodos que usan Authentication
class ReservaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AvisoRepository avisoRepo;

    @MockitoBean
    private UsuarioRepository userRepo;

    @MockitoBean
    private ReservaRepository reservaRepo;

    @MockitoBean
    private ReservaService reservaService;

    private Usuario usuario;
    private Maquinaria maquinaria;
    private Aviso aviso;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("test@test.com");

        maquinaria = new Maquinaria();
        maquinaria.setId(10L);
        maquinaria.setMarca("Marca de prueba");

        aviso = new Aviso();
        aviso.setId(100L);
        aviso.setMaquinaria(maquinaria);

        when(userRepo.findByEmail("test@test.com"))
                .thenReturn(Optional.of(usuario));
        when(avisoRepo.findById(100L))
                .thenReturn(Optional.of(aviso));
    }

    // -------------------------------------------------------------------------
    // 1) GET /reservas/nueva  -> form(...)
    // -------------------------------------------------------------------------
    @Test
    void formNuevaReserva_muestraFormularioConAviso() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/reservas/nueva")
                        .param("avisoId", "100"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("reserva-form"))
                .andExpect(MockMvcResultMatchers.model().attribute("aviso", aviso));
    }

    // -------------------------------------------------------------------------
    // 2) POST /reservas       -> crear(...)
    // -------------------------------------------------------------------------
    @Test
    void crearReserva_redirigeHomeYLlamaServicio() throws Exception {
        String inicio = "2024-01-01";
        String fin = "2024-01-05";

        mockMvc.perform(MockMvcRequestBuilders.post("/reservas")
                        .with(csrf())
                        .param("avisoId", "100")
                        .param("inicio", inicio)
                        .param("fin", fin))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/home"));

        // Verifica que el service se llamó con los argumentos correctos
        verify(reservaService).reservar(
                Mockito.eq(aviso),
                Mockito.eq(usuario),
                Mockito.eq(LocalDate.parse(inicio)),
                Mockito.eq(LocalDate.parse(fin))
        );
    }

    // -------------------------------------------------------------------------
    // 3) GET /reservas/mias   -> misReservas(...)
    // -------------------------------------------------------------------------
    @Test
    void misReservas_devuelveSoloReservasDelUsuarioActual() throws Exception {
        // Reserva del usuario autenticado
        Reserva r1 = new Reserva();
        r1.setId(1L);
        r1.setArrendatario(usuario);
        r1.setMaquinaria(maquinaria);
        r1.setFechaInicio(LocalDate.of(2024, 1, 1));
        r1.setFechaFin(LocalDate.of(2024, 1, 3));
        r1.setPrecioTotal(new BigDecimal("100000"));

        // Reserva de otro usuario, que NO debe aparecer
        Usuario otroUsuario = new Usuario();
        otroUsuario.setId(2L);
        otroUsuario.setEmail("otro@otro.com");

        Reserva r2 = new Reserva();
        r2.setId(2L);
        r2.setArrendatario(otroUsuario);
        r2.setMaquinaria(maquinaria);

        when(reservaRepo.findAll()).thenReturn(List.of(r1, r2));

        mockMvc.perform(MockMvcRequestBuilders.get("/reservas/mias"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("reservas-mias"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("reservas"))
                // Solo debe haber 1 reserva, la del usuario con id = 1
                .andExpect(MockMvcResultMatchers.model().attribute("reservas", hasSize(1)))
                .andExpect(MockMvcResultMatchers.model().attribute("reservas",
                        hasItem(allOf(
                                hasProperty("id", is(1L)),
                                hasProperty("arrendatario", is(usuario))
                        ))
                ));
    }
}
