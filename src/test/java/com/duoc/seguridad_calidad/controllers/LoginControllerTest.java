package com.duoc.seguridad_calidad.controllers;

import com.duoc.seguridad_calidad.security.WebSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginController.class)
@Import(WebSecurityConfig.class)
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void loginSinAutenticacion_devuelveVistaLogin() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeDoesNotExist("error"))
                .andExpect(model().attributeDoesNotExist("mensaje"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void loginConAutenticacion_redirigeAHome() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }

    @Test
    @WithAnonymousUser
    void loginConParametroError_muestraMensajeDeError() throws Exception {
        mockMvc.perform(get("/login").param("error", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "Credenciales inválidas"));
    }

    @Test
    @WithAnonymousUser
    void loginConParametroLogout_muestraMensajeDeLogout() throws Exception {
        mockMvc.perform(get("/login").param("logout", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("mensaje"))
                .andExpect(model().attribute("mensaje", "Has cerrado sesión correctamente."));
    }

    @Test
    void loginConAutenticacionPeroNoAutenticado_devuelveVistaLogin() throws Exception {
        // Usamos TestingAuthenticationToken que es una implementación real, no un mock.
        // Por defecto, setAuthenticated(false).
        TestingAuthenticationToken auth = new TestingAuthenticationToken("user", "pass");
        auth.setAuthenticated(false);

        mockMvc.perform(get("/login").with(authentication(auth)))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void loginConUsuarioAnonimo_noRedirigeAHome() throws Exception {
        // Crea un token anónimo real. Spring Security considera a los anónimos como "autenticados" en su contexto,
        // pero el controlador filtra explícitamente por 'instanceof AnonymousAuthenticationToken'.
        AnonymousAuthenticationToken auth = new AnonymousAuthenticationToken(
                "key", "anonymousUser", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));

        mockMvc.perform(get("/login").with(authentication(auth)))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }
}
