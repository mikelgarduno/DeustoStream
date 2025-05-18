package com.example.restapi.integration;

import com.example.restapi.controller.QrLoginController;
import com.example.restapi.repository.UsuarioRepository;
import com.example.restapi.service.QrLoginService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QrLoginController.class)
public class QrLoginControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private QrLoginService qrLoginService;

    @MockitoBean
    private UsuarioRepository usuarioRepository;

    // ---- GET /qr-login-form ----

    @Test
    void mostrarFormularioLogin_conTokenValido_debeDevolverVistaYToken() throws Exception {
        mockMvc.perform(get("/qr-login-form").param("token", "abc123"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("token"))
                .andExpect(view().name("qr-login-form"));
    }

    @Test
    void mostrarFormularioLogin_sinToken_debeDevolverVistaConError() throws Exception {
        mockMvc.perform(get("/qr-login-form").param("token", ""))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"))
                .andExpect(view().name("qr-login-form"));
    }

    // ---- POST /qr-login-submit ----

    @Test
    void procesarLoginQr_credencialesInvalidas_debeDevolverVistaConError() throws Exception {
        when(usuarioRepository.findByCorreoAndContrasenya("correo@test.com", "wrongpass"))
                .thenReturn(null);

        mockMvc.perform(post("/qr-login-submit")
                .param("token", "abc123")
                .param("correo", "correo@test.com")
                .param("contrasenya", "wrongpass"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("token", "abc123"))
                .andExpect(view().name("qr-login-form"));
    }

    // ---- GET /qr-login ----

    @Test
    void mostrarCodigoQr_tokenYaExisteEnSesion_debeRetornarImagen() throws Exception {
        when(qrLoginService.generarCodigoQR(anyString())).thenReturn("fake-image".getBytes());

        mockMvc.perform(get("/qr-login").sessionAttr("qr-token", "abc123"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG_VALUE));
    }

    @Test
    void mostrarCodigoQr_tokenNoExisteEnSesion_debeGenerarTokenYRetornarImagen() throws Exception {
        when(qrLoginService.generarCodigoQR(contains("qr-login-form"))).thenReturn("img-data".getBytes());

        mockMvc.perform(get("/qr-login"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG_VALUE));
    }
}