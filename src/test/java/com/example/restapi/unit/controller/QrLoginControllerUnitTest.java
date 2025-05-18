package com.example.restapi.unit.controller;

import com.example.restapi.controller.QrLoginController;
import com.example.restapi.repository.UsuarioRepository;
import com.example.restapi.service.QrLoginService;
import com.example.restapi.model.Usuario;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.Model;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class QrLoginControllerUnitTest {

    @Mock
    private QrLoginService qrService;
    
    @Mock
    private UsuarioRepository usuarioRepo;
    
    @Mock
    private Model model;

    @InjectMocks
    private QrLoginController controller;

    @Test
    void testMostrarFormularioLogin_conTokenValido() {
        String view = controller.mostrarFormularioLogin("abc123", model);

        verify(model).addAttribute("token", "abc123");
        assertEquals("qr-login-form", view);
    }

    @Test
    void testMostrarFormularioLogin_conTokenVacio() {
        String view = controller.mostrarFormularioLogin("", model);

        verify(model).addAttribute(eq("error"), contains("Token inválido"));
        assertEquals("qr-login-form", view);
    }

    @Test
    void testMostrarFormularioLogin_conTokenNull() {
        String view = controller.mostrarFormularioLogin(null, model);

        verify(model).addAttribute(eq("error"), contains("Token inválido"));
        assertEquals("qr-login-form", view);
    }

    @Test
    void testProcesarLoginQr_credencialesInvalidas() {
        when(usuarioRepo.findByCorreoAndContrasenya("correo@test.com", "123")).thenReturn(null);

        String view = controller.procesarLoginQr("abc", "correo@test.com", "123", model);

        verify(model).addAttribute(eq("error"), contains("Credenciales inválidas"));
        verify(model).addAttribute("token", "abc");
        assertEquals("qr-login-form", view);
    }

    @Test
    void testProcesarLoginQr_credencialesValidas() {
        Usuario usuarioMock = new Usuario();
        when(usuarioRepo.findByCorreoAndContrasenya("correo@test.com", "123")).thenReturn(usuarioMock);

        String view = controller.procesarLoginQr("abc", "correo@test.com", "123", model);

        verify(qrService).marcarAutorizado("abc", usuarioMock);
        verify(model).addAttribute("mensaje", "Inicio de sesión autorizado correctamente. Puedes cerrar esta ventana.");
        assertEquals("qr-login-success", view);
    }

    @Test
    void testMostrarCodigoQr_sinTokenExistente() throws Exception {
        MockHttpSession session = new MockHttpSession();
        byte[] qrBytes = new byte[]{1, 2, 3};
        
        when(qrService.generarCodigoQR(contains("token="))).thenReturn(qrBytes);
        
        byte[] result = controller.mostrarCodigoQr(session);
        
        assertNotNull(session.getAttribute("qr-token"));
        String token = (String) session.getAttribute("qr-token");
        verify(qrService).registrarToken(token);
        verify(qrService).generarCodigoQR(contains("token=" + token));
        assertArrayEquals(qrBytes, result);
    }

    @Test
    void testMostrarCodigoQr_conTokenExistente() throws Exception {
        MockHttpSession session = new MockHttpSession();
        String existingToken = "existing-token";
        session.setAttribute("qr-token", existingToken);
        
        byte[] qrBytes = new byte[]{1, 2, 3};
        when(qrService.generarCodigoQR(contains("token=" + existingToken))).thenReturn(qrBytes);
        
        byte[] result = controller.mostrarCodigoQr(session);
        
        verify(qrService, never()).registrarToken(anyString());
        verify(qrService).generarCodigoQR(contains("token=" + existingToken));
        assertArrayEquals(qrBytes, result);
    }
}
