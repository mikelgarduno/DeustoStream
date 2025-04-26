package com.example.restapi.unit.modelos;

import static org.junit.jupiter.api.Assertions.*;
import com.example.restapi.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UsuarioTest {

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario("Juan", "Pérez", "juan.perez@example.com", "1234");
    }

    @Test
    void testGetters() {
        assertEquals("Juan", usuario.getNombre());
        assertEquals("Pérez", usuario.getApellido());
        assertEquals("juan.perez@example.com", usuario.getCorreo());
        assertEquals("1234", usuario.getContrasenya());
    }

    @Test
    void testSetters() {
        usuario.setNombre("Pedro");
        usuario.setApellido("Gómez");
        usuario.setCorreo("pedro.gomez@example.com");
        usuario.setContrasenya("4321");

        assertEquals("Pedro", usuario.getNombre());
        assertEquals("Gómez", usuario.getApellido());
        assertEquals("pedro.gomez@example.com", usuario.getCorreo());
        assertEquals("4321", usuario.getContrasenya());
    }

    @Test
    void testEqualsAndHashCode() {
        Usuario usuario2 = new Usuario("Juan", "Pérez", "juan.perez@example.com", "1234");

        assertEquals(usuario, usuario2);
        assertEquals(usuario.hashCode(), usuario2.hashCode());
    }

    @Test
    void testToString() {
        String toString = usuario.toString();
        assertTrue(toString.contains("Juan"));
        assertTrue(toString.contains("Pérez"));
        assertTrue(toString.contains("juan.perez@example.com"));
    }
}