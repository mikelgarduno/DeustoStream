package com.example.restapi;

import org.junit.jupiter.api.Test;

import com.example.restapi.model.Generos;
import com.example.restapi.model.Series;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class SerieTest {

    @Test
    public void testSerieConstructor() {
        Series serie = new Series("Breaking Bad", 2008 ,"Descripcion", Generos.DRAMA, null, "url");
        assertNotNull(serie);
        assertEquals("Breaking Bad", serie.getTitulo());
        assertEquals(2008, serie.getAnio());
        assertEquals("Descripcion", serie.getDescripcion());
        assertEquals(Generos.DRAMA, serie.getGenero());
        assertEquals("url", serie.getImagenUrl());
        assertNotNull(serie.getCapitulos());
        assertTrue(serie.getCapitulos().isEmpty());
    }
    @Test
    public void testSerieToString() {
        Series serie = new Series("Breaking Bad", 2008 ,"Descripcion", Generos.DRAMA, null, "url");
        serie.setId(1L);
        String toString = serie.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("Breaking Bad"));
    }

    @Test
    public void testGettersAndSetters() {
        Series serie = new Series("Breaking Bad", 2008 ,"Descripcion", Generos.DRAMA, null, "url");
        serie.setId(1L);
        assertNotNull(serie.getId());
        assertNotNull(serie.getTitulo());
        assertNotNull(serie.getAnio());
        assertNotNull(serie.getDescripcion());
        assertNotNull(serie.getGenero());
        assertNotNull(serie.getCapitulos());
        assertNotNull(serie.getImagenUrl());
    }
    @Test
    public void testGetNumeroCapitulos() {
        Series serie = new Series("Breaking Bad", 2008 ,"Descripcion", Generos.DRAMA, null, "url");
        serie.setId(1L);
        assertEquals(0, serie.getNumeroCapitulos());
    }
    @Test
    public void testSetCapitulos() {
        Series serie = new Series("Breaking Bad", 2008 ,"Descripcion", Generos.DRAMA, null, "url");
        serie.setId(1L);
        assertNotNull(serie.getCapitulos());
        assertTrue(serie.getCapitulos().isEmpty());
    }
    @Test
    public void testSetCapitulosWithEmptyList() {
        Series serie = new Series("Breaking Bad", 2008 ,"Descripcion", Generos.DRAMA, null, "url");
        serie.setId(1L);
        serie.setCapitulos(new ArrayList<>());
        assertNotNull(serie.getCapitulos());
        assertTrue(serie.getCapitulos().isEmpty());
    }
    @Test
    public void testEqualsAndHashCode() {
        Series serie1 = new Series("Breaking Bad", 2008, "Descripcion", Generos.DRAMA, null, "url");
        serie1.setId(1L);
        Series serie2 = new Series("Breaking Bad", 2008, "Descripcion", Generos.DRAMA, null, "url");
        serie2.setId(1L);
        Series serie3 = new Series("Better Call Saul", 2015, "Descripcion", Generos.DRAMA, null, "url");
        serie3.setId(2L);

        assertEquals(serie1, serie2);
        assertNotEquals(serie1, serie3);
        assertEquals(serie1.hashCode(), serie2.hashCode());
        assertNotEquals(serie1.hashCode(), serie3.hashCode());
    }
}