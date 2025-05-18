package com.example.restapi.unit.controller;

import com.example.restapi.controller.MobileLoginController;
import com.example.restapi.service.QrLoginService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class MobileLoginControllerUnitTest {

    @Test
    void testAutorizarQr_debeMarcarTokenYGuardarSesion() {
        QrLoginService qrLoginService = mock(QrLoginService.class);
        HttpSession session = mock(HttpSession.class);
        String token = "abc123";

        MobileLoginController controller = new MobileLoginController(qrLoginService);
        String resultado = controller.autorizarQr(token, session);

        verify(qrLoginService).marcarAutorizado(token);
        verify(session).setAttribute("usuarioAutenticado", true);
        assertEquals("Autorizado", resultado);
    }
}
