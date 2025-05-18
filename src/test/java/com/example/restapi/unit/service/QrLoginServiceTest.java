package com.example.restapi.unit.service;

import com.example.restapi.service.QrLoginService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QrLoginServiceTest {

    @Test
    void testRegistrarYVerificarToken() {
        QrLoginService service = new QrLoginService();

        String token = "abc123";
        service.registrarToken(token);

        assertFalse(service.estaAutorizado(token));

        service.marcarAutorizado(token);
        assertTrue(service.estaAutorizado(token));
    }

    @Test
    void testGenerarCodigoQR() throws Exception {
        QrLoginService service = new QrLoginService();

        String contenido = "https://example.com/qr-login?token=abc123";
        byte[] qrCode = service.generarCodigoQR(contenido);

        assertNotNull(qrCode);
        assertTrue(qrCode.length > 0); // Verifica que se haya generado contenido
    }
}
