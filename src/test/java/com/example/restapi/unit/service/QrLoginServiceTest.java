package com.example.restapi.unit.service;

import com.example.restapi.model.Usuario;
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

        Usuario usuario = new Usuario();
        service.marcarAutorizado(token, usuario);
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
    
    @Test
    void testGetUsuarioAutorizado() {
        QrLoginService service = new QrLoginService();
        String token = "token123";
        Usuario usuario = new Usuario();
        
        service.registrarToken(token);
        assertNull(service.getUsuarioAutorizado(token));
        
        service.marcarAutorizado(token, usuario);
        assertEquals(usuario, service.getUsuarioAutorizado(token));
    }
    
    @Test
    void testTokenNoEncontrado() {
        QrLoginService service = new QrLoginService();
        String token = "tokenInexistente";
        
        assertFalse(service.estaAutorizado(token));
        assertNull(service.getUsuarioAutorizado(token));
    }
    
    @Test
    void testMarcarAutorizadoTokenInexistente() {
        QrLoginService service = new QrLoginService();
        String token = "tokenInexistente";
        Usuario usuario = new Usuario();
        
        service.marcarAutorizado(token, usuario);
        assertFalse(service.estaAutorizado(token));
        assertNull(service.getUsuarioAutorizado(token));
    }

    @Test
    void testGenerarCodigoQRContenidoNulo() {
        QrLoginService service = new QrLoginService();
        
        Exception exception = assertThrows(Exception.class, () -> {
            service.generarCodigoQR(null);
        });
        
        assertTrue(exception.getCause() instanceof IllegalArgumentException || 
                  exception instanceof NullPointerException);
    }

    @Test
    void testLimpiezaDeTokens() throws Exception {
        QrLoginService service = new QrLoginService();
        String token = "tokenTemporal";
        
        service.registrarToken(token);
        assertTrue(service.estaAutorizado(token) == false);
    }
}
