package com.duoc.seguridad_calidad.repositories;

import com.duoc.seguridad_calidad.domain.EstadoReserva;
import com.duoc.seguridad_calidad.domain.Maquinaria;
import com.duoc.seguridad_calidad.domain.Reserva;
import com.duoc.seguridad_calidad.domain.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ReservaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ReservaRepository reservaRepository;

    private Usuario arrendatario;
    private Maquinaria maquinaria;

    @BeforeEach
    void setUp() {
        Usuario dueno = new Usuario();
        dueno.setEmail("dueno@test.com");
        dueno.setNombre("Dueño Test");
        dueno.setPasswordHash("password");
        entityManager.persist(dueno);

        maquinaria = new Maquinaria();
        maquinaria.setMarca("Jhondier");
        maquinaria.setDueno(dueno);
        entityManager.persist(maquinaria);

        arrendatario = new Usuario();
        arrendatario.setEmail("arrendatario@test.com");
        arrendatario.setNombre("Arrendatario Test");
        arrendatario.setPasswordHash("password");
        entityManager.persist(arrendatario);
        entityManager.flush();
    }

    @Test
    void saveAndFindById_works() {
        Reserva reserva = new Reserva();
        reserva.setMaquinaria(maquinaria);
        reserva.setArrendatario(arrendatario);
        reserva.setEstado(EstadoReserva.PENDIENTE);
        reserva.setFechaInicio(LocalDate.now());
        reserva.setFechaFin(LocalDate.now().plusDays(1));
        reserva.setPrecioTotal(new BigDecimal("100.00"));
        reserva = reservaRepository.save(reserva);

        Optional<Reserva> found = reservaRepository.findById(reserva.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getEstado()).isEqualTo(EstadoReserva.PENDIENTE);
    }
}
