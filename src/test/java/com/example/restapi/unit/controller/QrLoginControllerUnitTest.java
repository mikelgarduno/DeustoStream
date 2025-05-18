package com.example.restapi.unit.controller;

import com.example.restapi.controller.QrLoginController;
import com.example.restapi.repository.UsuarioRepository;
import com.example.restapi.service.QrLoginService;
import com.example.restapi.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class QrLoginControllerUnitTest {

    @Test
    void testMostrarFormularioLogin_conTokenValido() {
        QrLoginService qrService = mock(QrLoginService.class);
        UsuarioRepository usuarioRepo = mock(UsuarioRepository.class);
        Model model = mock(Model.class);

        QrLoginController controller = new QrLoginController(qrService, usuarioRepo);
        String view = controller.mostrarFormularioLogin("abc123", model);

        verify(model).addAttribute("token", "abc123");
        assertEquals("qr-login-form", view);
    }

    @Test
    void testMostrarFormularioLogin_conTokenVacio() {
        QrLoginService qrService = mock(QrLoginService.class);
        UsuarioRepository usuarioRepo = mock(UsuarioRepository.class);
        Model model = mock(Model.class);

        QrLoginController controller = new QrLoginController(qrService, usuarioRepo);
        String view = controller.mostrarFormularioLogin("", model);

        verify(model).addAttribute(eq("error"), contains("Token inválido"));
        assertEquals("qr-login-form", view);
    }

    @Test
    void testProcesarLoginQr_credencialesInvalidas() {
        QrLoginService qrService = mock(QrLoginService.class);
        UsuarioRepository usuarioRepo = mock(UsuarioRepository.class);
        Model model = mock(Model.class);

        when(usuarioRepo.findByCorreoAndContrasenya("correo@test.com", "123")).thenReturn(null);

        QrLoginController controller = new QrLoginController(qrService, usuarioRepo);
        String view = controller.procesarLoginQr("abc", "correo@test.com", "123", model);

        verify(model).addAttribute(eq("error"), contains("Credenciales inválidas"));
        assertEquals("qr-login-form", view);
    }

    @Test
    void testProcesarLoginQr_credencialesValidas() {
        QrLoginService qrService = mock(QrLoginService.class);
        UsuarioRepository usuarioRepo = mock(UsuarioRepository.class);
        Model model = mock(Model.class);

        Usuario usuarioMock = new Usuario();
        when(usuarioRepo.findByCorreoAndContrasenya("correo@test.com", "123")).thenReturn(usuarioMock);

        QrLoginController controller = new QrLoginController(qrService, usuarioRepo);
        String view = controller.procesarLoginQr("abc", "correo@test.com", "123", model);

        verify(qrService).marcarAutorizado("abc");
        verify(model).addAttribute("mensaje", "Inicio de sesión autorizado correctamente.");
        assertEquals("qr-login-form", view);
    }
}
