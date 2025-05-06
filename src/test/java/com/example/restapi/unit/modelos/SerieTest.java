package com.example.restapi.unit.modelos;

import org.junit.jupiter.api.Test;

import com.example.restapi.model.Generos;
import com.example.restapi.model.Perfil;
import com.example.restapi.model.Series;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class SerieTest {

    @Test
    void testSerieConstructor() {
        Series serie = new Series("Breaking Bad", 2008, "Descripcion", Generos.DRAMA, null, "url");
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
    void testSerieToString() {
        Series serie = new Series("Breaking Bad", 2008, "Descripcion", Generos.DRAMA, null, "url");
        serie.setId(1L);
        String toString = serie.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("Breaking Bad"));
    }

    @Test
    void testGettersAndSetters() {
        Series serie = new Series("Breaking Bad", 2008, "Descripcion", Generos.DRAMA, null, "url");
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
    void testGetNumeroCapitulos() {
        Series serie = new Series("Breaking Bad", 2008, "Descripcion", Generos.DRAMA, null, "url");
        serie.setId(1L);
        assertEquals(0, serie.getNumeroCapitulos());
    }

    @Test
    void testSetCapitulos() {
        Series serie = new Series("Breaking Bad", 2008, "Descripcion", Generos.DRAMA, null, "url");
        serie.setId(1L);
        assertNotNull(serie.getCapitulos());
        assertTrue(serie.getCapitulos().isEmpty());
    }

    @Test
    void testSetCapitulosWithEmptyList() {
        Series serie = new Series("Breaking Bad", 2008, "Descripcion", Generos.DRAMA, null, "url");
        serie.setId(1L);
        serie.setCapitulos(new ArrayList<>());
        assertNotNull(serie.getCapitulos());
        assertTrue(serie.getCapitulos().isEmpty());
    }

    @Test
    void setNumeroCapitulos() {
        Series serie = new Series("Breaking Bad", 2008, "Descripcion", Generos.DRAMA, null, "url");
        serie.setId(1L);
        serie.setNumeroCapitulos(5);
        assertEquals(5, serie.getNumeroCapitulos());
    }

    @Test
    void testSetAndGetPerfiles() {
        Series s = new Series();
        // por defecto es null
        assertNull(s.getPerfiles());
        // al asignar una lista, debe devolverla exactamente
        List<Perfil> lista = new ArrayList<>();
        s.setPerfil(lista);
        assertSame(lista, s.getPerfiles());
    }

    

    @Test
    public void testEqualsWithSameObject() {
        Series serie = new Series("Breaking Bad", 2008, "Descripcion", Generos.DRAMA, null, "url");
        //assertTrue(serie.equals(serie));
        assertEquals(serie, serie);
    }

    @Test
    void testEqualsWithNull() {
        Series serie = new Series("Breaking Bad", 2008, "Descripcion", Generos.DRAMA, null, "url");
        //assertFalse(serie.equals(null));
        assertNotEquals(serie, (Integer) null);
    }

    @Test
    void testEqualsWithDifferentClass() {
        Series serie = new Series("Breaking Bad", 2008, "Descripcion", Generos.DRAMA, null, "url");
        //assertFalse(serie.equals(new Object()));
        assertNotEquals(serie, new Object());
    }

    @Test
    void testEqualsWithIdenticalSeries() {
        Series serie1 = new Series("Breaking Bad", 2008, "Descripcion", Generos.DRAMA, null, "url");
        serie1.setId(1L);
        Series serie2 = new Series("Breaking Bad", 2008, "Descripcion", Generos.DRAMA, null, "url");
        serie2.setId(1L);
        //assertTrue(serie1.equals(serie2));
        assertEquals(serie1, serie2);
    }

    @Test
    void testEqualsWithDifferentId() {
        Series serie1 = new Series("Breaking Bad", 2008, "Descripcion", Generos.DRAMA, null, "url");
        serie1.setId(1L);
        Series serie2 = new Series("Breaking Bad", 2008, "Descripcion", Generos.DRAMA, null, "url");
        serie2.setId(2L);
        //assertFalse(serie1.equals(serie2));
        assertNotEquals(serie1, serie2);
    }

    @Test
    void testEqualsWithDifferentTitulo() {
        Series serie1 = new Series("Breaking Bad", 2008, "Descripcion", Generos.DRAMA, null, "url");
        Series serie2 = new Series("Better Call Saul", 2008, "Descripcion", Generos.DRAMA, null, "url");
        //assertFalse(serie1.equals(serie2));
        assertNotEquals(serie1, serie2);
    }

    @Test
    void testEqualsWithDifferentAnio() {
        Series serie1 = new Series("Breaking Bad", 2008, "Descripcion", Generos.DRAMA, null, "url");
        Series serie2 = new Series("Breaking Bad", 2009, "Descripcion", Generos.DRAMA, null, "url");
        //assertFalse(serie1.equals(serie2));
        assertNotEquals(serie1, serie2);
    }

    @Test
    void testEqualsWithDifferentDescripcion() {
        Series serie1 = new Series("Breaking Bad", 2008, "Descripcion", Generos.DRAMA, null, "url");
        Series serie2 = new Series("Breaking Bad", 2008, "Otra descripcion", Generos.DRAMA, null, "url");
        //assertFalse(serie1.equals(serie2));
        assertNotEquals(serie1, serie2);
    }

    @Test
    void testEqualsWithDifferentGenero() {
        Series serie1 = new Series("Breaking Bad", 2008, "Descripcion", Generos.DRAMA, null, "url");
        Series serie2 = new Series("Breaking Bad", 2008, "Descripcion", Generos.ACCION, null, "url");
        //assertFalse(serie1.equals(serie2));
        assertNotEquals(serie1, serie2);
    }

    @Test
    void testHashCodeConsistency() {
        Series serie = new Series("Breaking Bad", 2008, "Descripcion", Generos.DRAMA, null, "url");
        serie.setId(1L);
        int initialHashCode = serie.hashCode();
        assertEquals(initialHashCode, serie.hashCode());
    }

    @Test
    void testHashCodeEquality() {
        Series serie1 = new Series("Breaking Bad", 2008, "Descripcion", Generos.DRAMA, null, "url");
        serie1.setId(1L);
        Series serie2 = new Series("Breaking Bad", 2008, "Descripcion", Generos.DRAMA, null, "url");
        serie2.setId(1L);
        assertEquals(serie1.hashCode(), serie2.hashCode());
    }

    @Test
    void testHashCodeWithDifferentValues() {
        Series serie1 = new Series("Breaking Bad", 2008, "Descripcion", Generos.DRAMA, null, "url");
        serie1.setId(1L);
        Series serie2 = new Series("Better Call Saul", 2015, "Otra descripcion", Generos.COMEDIA, null, "url");
        serie2.setId(2L);
        assertNotEquals(serie1.hashCode(), serie2.hashCode());
    }

    @Test
    public void testHashCodeWithNullValues() {
        Series serie1 = new Series(null, 0, null, null, null, null);
        Series serie2 = new Series(null, 0, null, null, null, null);
        assertEquals(serie1.hashCode(), serie2.hashCode());
    }
}