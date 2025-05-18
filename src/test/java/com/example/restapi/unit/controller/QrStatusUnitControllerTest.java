
package com.example.restapi.unit.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.example.restapi.controller.QrStatusController;
import com.example.restapi.model.Usuario;
import com.example.restapi.service.QrLoginService;

import jakarta.servlet.http.HttpSession;

public class QrStatusUnitControllerTest {

    private QrStatusController qrStatusController;

    @Mock
    private QrLoginService qrLoginService;

    @Mock
    private HttpSession session;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        qrStatusController = new QrStatusController(qrLoginService);
    }

    @Test
    public void testVerificarEstadoToken_Autorizado() {
        // Arrange
        String token = "valid-token";
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setCorreo("test@example.com");

        when(qrLoginService.estaAutorizado(token)).thenReturn(true);
        when(qrLoginService.getUsuarioAutorizado(token)).thenReturn(usuario);

        // Act
        Map<String, Object> response = qrStatusController.verificarEstadoToken(token, session);

        // Assert
        assertTrue((Boolean) response.get("autorizado"));
        assertEquals("test@example.com", response.get("usuario"));
        verify(session).setAttribute("usuarioId", 1L);
        verify(session).setAttribute("usuario", usuario);
        verify(session).setAttribute("usuarioAutenticado", true);
    }

    @Test
    public void testVerificarEstadoToken_NoAutorizado() {
        // Arrange
        String token = "invalid-token";
        when(qrLoginService.estaAutorizado(token)).thenReturn(false);

        // Act
        Map<String, Object> response = qrStatusController.verificarEstadoToken(token, session);

        // Assert
        assertFalse((Boolean) response.get("autorizado"));
        verify(session, never()).setAttribute(anyString(), any());
    }

    @Test
    public void testVerificarEstadoToken_AutorizadoSinUsuario() {
        // Arrange
        String token = "valid-token-no-user";
        when(qrLoginService.estaAutorizado(token)).thenReturn(true);
        when(qrLoginService.getUsuarioAutorizado(token)).thenReturn(null);

        // Act
        Map<String, Object> response = qrStatusController.verificarEstadoToken(token, session);

        // Assert
        assertFalse((Boolean) response.get("autorizado"));
        verify(session, never()).setAttribute(anyString(), any());
    }

    @Test
    public void testVerificarEstadoToken_TokenVacio() {
        // Arrange
        String token = "";
        when(qrLoginService.estaAutorizado(token)).thenReturn(false);

        // Act
        Map<String, Object> response = qrStatusController.verificarEstadoToken(token, session);

        // Assert
        assertFalse((Boolean) response.get("autorizado"));
        verify(session, never()).setAttribute(anyString(), any());
    }
}
