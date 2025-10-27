package com.duoc.seguridad_calidad.config;

import com.duoc.seguridad_calidad.domain.*;
import com.duoc.seguridad_calidad.repositories.*;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Configuration
public class DemoDataConfig {
    @Bean
    ApplicationRunner showDsUrl(DataSource ds) {
        return args -> {
            try (var c = ds.getConnection()) {
                System.out.println(">>> DS URL = " + c.getMetaData().getURL());
            }
        };
    }

    @Bean
    CommandLineRunner seedData(
            UsuarioRepository usuarioRepo,
            PerfilRepository perfilRepo,
            MaquinariaRepository maqRepo,
            MantenimientoRepository mantRepo,
            AvisoRepository avisoRepo,
            PasswordEncoder encoder
    ) {
        return args -> {
            // Usuario demo
            if (usuarioRepo.findByEmail("demo@agro.cl").isEmpty()) {
                Usuario u = new Usuario();
                u.setNombre("Usuario Demo");
                u.setEmail("demo@agro.cl");
                u.setPasswordHash(encoder.encode("123456")); // contraseña: 123456
                u.setRoles(Set.of("USER"));
                usuarioRepo.save(u);

                Perfil p = new Perfil();
                p.setUsuario(u);
                p.setDireccion("Ruta S/N, Campo Fértil");
                p.setTelefono("+56 9 1234 5678");
                p.setCultivos("Trigo, Maíz");
                perfilRepo.save(p);

                // Maquinaria del dueño
                Maquinaria m = new Maquinaria();
                m.setDueno(u);
                m.setTipo(TipoMaquinaria.TRACTOR);
                m.setMarca("John Deere");
                m.setModelo("5075E");
                m.setAnioFabricacion(2021);
                m.setCapacidad("75 HP");
                m.setUbicacionRegion("Ñuble");
                m.setUbicacionComuna("San Carlos");
                m.setPrecioPorDia(new BigDecimal("120000"));
                maqRepo.save(m);

                // Mantención
                Mantenimiento mt = new Mantenimiento();
                mt.setMaquinaria(m);
                mt.setFecha(LocalDate.now().minusMonths(2));
                mt.setDetalle("Cambio de aceite y filtros");
                mantRepo.save(mt);

                // Aviso vigente
                Aviso a = new Aviso();
                a.setMaquinaria(m);
                a.setDisponibleDesde(LocalDate.now().plusDays(1));
                a.setDisponibleHasta(LocalDate.now().plusMonths(2));
                a.setPrecioPorDia(new BigDecimal("135000"));
                a.setDestacado(true);
                a.setCondicionesArriendo("Devolución con estanque como se entrega. Uso responsable.");
                a.setMedioPago(MedioPago.TRANSFERENCIA);
                avisoRepo.save(a);
            }
        };
    }
}
