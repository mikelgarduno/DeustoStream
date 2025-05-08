package com.example.restapi.unit.modelos;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.restapi.model.Pelicula;
import com.example.restapi.model.Perfil;
import com.example.restapi.model.Series;
import com.example.restapi.model.Usuario;
import com.example.restapi.model.Valoracion;

class ValoracionTest {
    private Usuario usuario;
    private Pelicula pelicula;
    private Series serie;
    private Perfil perfil;
    private Valoracion valoracion;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        pelicula = new Pelicula();
        serie = new Series();
        perfil = new Perfil();
        valoracion = new Valoracion(4, "Muy buena", usuario, pelicula, serie, perfil);
        valoracion.setId(1L);
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals(4, valoracion.getPuntuacion());
        assertEquals("Muy buena", valoracion.getComentario());
        assertEquals(usuario, valoracion.getUsuario());
        assertEquals(pelicula, valoracion.getPelicula());
        assertEquals(serie, valoracion.getSerie());
        assertEquals(perfil, valoracion.getPerfil());
        assertEquals(1L, valoracion.getId());
    }

    @Test
    void testSetters() {
        valoracion.setPuntuacion(5);
        valoracion.setComentario("Excelente");
        Usuario usuario2 = new Usuario();
        Pelicula pelicula2 = new Pelicula();
        Series serie2 = new Series();
        Perfil perfil2 = new Perfil();
        valoracion.setUsuario(usuario2);
        valoracion.setPelicula(pelicula2);
        valoracion.setSerie(serie2);
        valoracion.setPerfil(perfil2);
        valoracion.setId(2L);

        assertEquals(5, valoracion.getPuntuacion());
        assertEquals("Excelente", valoracion.getComentario());
        assertEquals(usuario2, valoracion.getUsuario());
        assertEquals(pelicula2, valoracion.getPelicula());
        assertEquals(serie2, valoracion.getSerie());
        assertEquals(perfil2, valoracion.getPerfil());
        assertEquals(2L, valoracion.getId());
    }

    @Test
    void testEqualsAndHashCode() {
        Valoracion v2 = new Valoracion(4, "Muy buena", usuario, pelicula, serie, perfil);
        v2.setId(1L);

        assertEquals(valoracion, v2);
        assertEquals(valoracion.hashCode(), v2.hashCode());

        v2.setPuntuacion(3);
        assertNotEquals(valoracion, v2);
    }

    @Test
    void testToString() {
        String str = valoracion.toString();
        assertTrue(str.contains("Valoracion{"));
        assertTrue(str.contains("puntuacion=4"));
        assertTrue(str.contains("comentario='Muy buena'"));
    }

    @Test
    void testNotEqualsDifferentClass() {
        assertNotEquals("some string", valoracion);
    }

    @Test
    void testEqualsSameObject() {
        assertEquals(valoracion, valoracion);
    }

    @Test
    void testDefaultConstructor() {
        Valoracion v = new Valoracion();
        assertNull(v.getId());
        assertEquals(0, v.getPuntuacion());
        assertNull(v.getComentario());
        assertNull(v.getUsuario());
        assertNull(v.getPelicula());
        assertNull(v.getSerie());
        assertNull(v.getPerfil());
    }
}
