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
        usuario.setId(1L); // Asignar un ID para las pruebas
    }

    @Test
    void testGetters() {
        assertEquals("Juan", usuario.getNombre());
        assertEquals("Pérez", usuario.getApellido());
        assertEquals("juan.perez@example.com", usuario.getCorreo());
        assertEquals("1234", usuario.getContrasenya());
        assertEquals(1L, usuario.getId());
    }

    @Test
    void testSetters() {
        usuario.setId(2L);
        usuario.setNombre("Pedro");
        usuario.setApellido("Gómez");
        usuario.setCorreo("pedro.gomez@example.com");
        usuario.setContrasenya("4321");

        assertEquals(2L, usuario.getId());
        assertEquals("Pedro", usuario.getNombre());
        assertEquals("Gómez", usuario.getApellido());
        assertEquals("pedro.gomez@example.com", usuario.getCorreo());
        assertEquals("4321", usuario.getContrasenya());
    }

    @Test
    void testEqualsSameObject() {
        assertTrue(usuario.equals(usuario), "An object should equal itself");
    }

    @Test
    void testEqualsNull() {
        assertFalse(usuario.equals(null), "An object should not equal null");
    }

    @Test
    void testEqualsDifferentClass() {
        assertFalse(usuario.equals(new Object()), "An object should not equal an object of a different class");
    }

    @Test
    void testEqualsDifferentNombre() {
        Usuario usuario2 = new Usuario("Pedro", "Pérez", "juan.perez@example.com", "1234");
        assertFalse(usuario.equals(usuario2), "Objects with different nombre should not be equal");
    }

    @Test
    void testEqualsDifferentCorreo() {
        Usuario usuario2 = new Usuario("Juan", "Pérez", "pedro.perez@example.com", "1234");
        assertFalse(usuario.equals(usuario2), "Objects with different correo should not be equal");
    }

    @Test
    void testEqualsSameNombreAndCorreo() {
        Usuario usuario2 = new Usuario("Juan", "Diferente", "juan.perez@example.com", "diferente");
        assertTrue(usuario.equals(usuario2), "Objects with same nombre and correo should be equal regardless of other fields");
        assertEquals(usuario.hashCode(), usuario2.hashCode(), "Equal objects should have equal hash codes");
    }

    @Test
    void testToString() {
        String toString = usuario.toString();
        assertTrue(toString.contains("Juan"));
        assertTrue(toString.contains("Pérez"));
        assertTrue(toString.contains("juan.perez@example.com"));
    }
}