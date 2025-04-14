package com.example.restapi;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.example.restapi.model.Generos;
import com.example.restapi.model.Pelicula;

public class PeliculaTest {
    @Test
    void testToString() {
        Pelicula pelicula = new Pelicula("Inception", Generos.ACCION, 148, 2010, "Un sueño dentro de otro", "url");
        pelicula.setId(1L);
        String toString = pelicula.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("Inception"));
    }
    @Test
    void testGettersAndSetters() {
        Pelicula pelicula = new Pelicula("Inception", Generos.ACCION, 148, 2010, "Un sueño dentro de otro", "url");
        pelicula.setId(1L);
        assertNotNull(pelicula.getId());
        assertNotNull(pelicula.getTitulo());
        assertNotNull(pelicula.getGenero());
        assertNotNull(pelicula.getDuracion());
        assertNotNull(pelicula.getAnio());
        assertNotNull(pelicula.getSinopsis());
        assertNotNull(pelicula.getImagenUrl());
    }
    @Test
    void testConstructor() {
        Pelicula pelicula = new Pelicula("Inception", Generos.ACCION, 148, 2010, "Un sueño dentro de otro", "url");
        assertNotNull(pelicula);
        assertNotNull(pelicula.getTitulo());
        assertNotNull(pelicula.getGenero());
        assertNotNull(pelicula.getDuracion());
        assertNotNull(pelicula.getAnio());
        assertNotNull(pelicula.getSinopsis());
        assertNotNull(pelicula.getImagenUrl());
    }

}
