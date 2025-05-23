package com.example.restapi.unit.modelos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.example.restapi.model.Generos;
import com.example.restapi.model.Pelicula;

class PeliculaTest {
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
        assertEquals(148, pelicula.getDuracion());
        assertEquals(2010, pelicula.getAnio());
        assertNotNull(pelicula.getSinopsis());
        assertNotNull(pelicula.getImagenUrl());
    }
    @Test
    void testConstructor() {
        Pelicula pelicula = new Pelicula("Inception", Generos.ACCION, 148, 2010, "Un sueño dentro de otro", "url");
        assertNotNull(pelicula);
        assertNotNull(pelicula.getTitulo());
        assertNotNull(pelicula.getGenero());
        assertEquals(148, pelicula.getDuracion());
        assertEquals(2010, pelicula.getAnio());
        assertNotNull(pelicula.getSinopsis());
        assertNotNull(pelicula.getImagenUrl());
    }
    @Test
    void testEqualsCompleteCoverage() {
        Pelicula pelicula1 = new Pelicula("Inception", Generos.ACCION, 148, 2010, "Un sueño dentro de otro", "url");
        Pelicula pelicula2 = new Pelicula("Inception", Generos.ACCION, 148, 2010, "Un sueño dentro de otro", "url");
        
        // Test same object reference (this == o)
        assertEquals(pelicula1, pelicula1);
        
        // Test null comparison (o == null)
        assertNotEquals(pelicula1, Integer.valueOf(0));
        
        // Test different class (getClass() != o.getClass())
        assertNotEquals(pelicula1, new Object());
        
        // Test successful equality (all fields match)
        assertEquals(pelicula1, pelicula2);
        
        // Test different attributes
        Pelicula differentTitle = new Pelicula("Different", Generos.ACCION, 148, 2010, "Un sueño dentro de otro", "url");
        Pelicula differentGenre = new Pelicula("Inception", Generos.TERROR, 148, 2010, "Un sueño dentro de otro", "url");
        Pelicula differentDuration = new Pelicula("Inception", Generos.ACCION, 120, 2010, "Un sueño dentro de otro", "url");
        Pelicula differentYear = new Pelicula("Inception", Generos.ACCION, 148, 2020, "Un sueño dentro de otro", "url");

        assertNotEquals(pelicula1, differentTitle);
        assertNotEquals(pelicula1, differentGenre);
        assertNotEquals(pelicula1, differentDuration);
        assertNotEquals(pelicula1, differentYear);
        
    }

    @Test
    void testHashCode() {
        Pelicula pelicula1 = new Pelicula("Inception", Generos.ACCION, 148, 2010, "Un sueño dentro de otro", "url");
        Pelicula pelicula2 = new Pelicula("Inception", Generos.ACCION, 148, 2010, "Un sueño dentro de otro", "url");
        Pelicula differentTitle = new Pelicula("Different", Generos.ACCION, 148, 2010, "Un sueño dentro de otro", "url");
        Pelicula differentGenre = new Pelicula("Inception", Generos.TERROR, 148, 2010, "Un sueño dentro de otro", "url");
        Pelicula differentDuration = new Pelicula("Inception", Generos.ACCION, 120, 2010, "Un sueño dentro de otro", "url");
        Pelicula differentYear = new Pelicula("Inception", Generos.ACCION, 148, 2020, "Un sueño dentro de otro", "url");

        // Test same hashCode for equal objects
        assertEquals(pelicula1.hashCode(), pelicula2.hashCode());

        // Test different hashCode for objects with different attributes
        assertNotEquals(pelicula1.hashCode(), differentTitle.hashCode());
        assertNotEquals(pelicula1.hashCode(), differentGenre.hashCode());
        assertNotEquals(pelicula1.hashCode(), differentDuration.hashCode());
        assertNotEquals(pelicula1.hashCode(), differentYear.hashCode());
    }

}
