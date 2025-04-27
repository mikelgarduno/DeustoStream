package com.example.restapi.unit.modelos;

import static org.junit.jupiter.api.Assertions.assertFalse;
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
    @Test
    void testEqualsCompleteCoverage() {
        Pelicula pelicula1 = new Pelicula("Inception", Generos.ACCION, 148, 2010, "Un sueño dentro de otro", "url");
        Pelicula pelicula2 = new Pelicula("Inception", Generos.ACCION, 148, 2010, "Un sueño dentro de otro", "url");
        
        // Test same object reference (this == o)
        assertTrue(pelicula1.equals(pelicula1));
        
        // Test null comparison (o == null)
        assertFalse(pelicula1.equals(null));
        
        // Test different class (getClass() != o.getClass())
        assertFalse(pelicula1.equals(new Object()));
        
        // Test successful equality (all fields match)
        assertTrue(pelicula1.equals(pelicula2));
        
        // Test different attributes
        Pelicula differentTitle = new Pelicula("Different", Generos.ACCION, 148, 2010, "Un sueño dentro de otro", "url");
        Pelicula differentGenre = new Pelicula("Inception", Generos.TERROR, 148, 2010, "Un sueño dentro de otro", "url");
        Pelicula differentDuration = new Pelicula("Inception", Generos.ACCION, 120, 2010, "Un sueño dentro de otro", "url");
        Pelicula differentYear = new Pelicula("Inception", Generos.ACCION, 148, 2020, "Un sueño dentro de otro", "url");
        
        assertFalse(pelicula1.equals(differentTitle));
        assertFalse(pelicula1.equals(differentGenre));
        assertFalse(pelicula1.equals(differentDuration));
        assertFalse(pelicula1.equals(differentYear));
    }

}
