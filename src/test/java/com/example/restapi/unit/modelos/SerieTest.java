package com.example.restapi.unit.modelos;

import org.junit.jupiter.api.Test;

import com.example.restapi.model.Generos;
import com.example.restapi.model.Perfil;
import com.example.restapi.model.Series;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class SerieTest {

    @Test
    public void testSerieConstructor() {
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
    public void testSerieToString() {
        Series serie = new Series("Breaking Bad", 2008, "Descripcion", Generos.DRAMA, null, "url");
        serie.setId(1L);
        String toString = serie.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("Breaking Bad"));
    }

    @Test
    public void testGettersAndSetters() {
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
    public void testGetNumeroCapitulos() {
        Series serie = new Series("Breaking Bad", 2008, "Descripcion", Generos.DRAMA, null, "url");
        serie.setId(1L);
        assertEquals(0, serie.getNumeroCapitulos());
    }

    @Test
    public void testSetCapitulos() {
        Series serie = new Series("Breaking Bad", 2008, "Descripcion", Generos.DRAMA, null, "url");
        serie.setId(1L);
        assertNotNull(serie.getCapitulos());
        assertTrue(serie.getCapitulos().isEmpty());
    }

    @Test
    public void testSetCapitulosWithEmptyList() {
        Series serie = new Series("Breaking Bad", 2008, "Descripcion", Generos.DRAMA, null, "url");
        serie.setId(1L);
        serie.setCapitulos(new ArrayList<>());
        assertNotNull(serie.getCapitulos());
        assertTrue(serie.getCapitulos().isEmpty());
    }

    @Test
    public void setNumeroCapitulos() {
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
void testEqualsAndHashCodeAllCases() {
    Generos gen = Generos.DRAMA;

    // 1) this == o
    Series self = new Series("T", 2000, "D", gen, null, "url1");
    assertTrue(self.equals(self), "equals debe devolver true al compararse consigo mismo");

    // 2) o == null
    assertFalse(self.equals(null), "equals debe devolver false si o es null");

    // 3) distinta clase
    assertFalse(self.equals("una cadena"), "equals debe devolver false para otro tipo");

    // 4) ignorar imagenUrl, capitulos y perfiles
    Series a = new Series("T", 2000, "D", gen, null, "urlA");
    Series b = new Series("T", 2000, "D", gen, null, "urlB");
    a.setPerfil(new ArrayList<>());  // lista vacía
    b.setPerfil(null);
    assertEquals(a, b,    "equals debe ignorar diferencias en perfiles e imagenUrl");

    // 5) misma igualdad cuando el id coincide
    a.setId(1L);
    b.setId(1L);
    assertEquals(a, b,    "equals debe devolver true si todos los campos considerados (incluyendo id) coinciden");
    assertEquals(a.hashCode(), b.hashCode(), "hashCode debe ser igual para objetos iguales");

    // 6) desigualdad si cambia el id
    b.setId(2L);
    assertNotEquals(a, b, "equals debe devolver false si los ids difieren");
    assertNotEquals(a.hashCode(), b.hashCode(), "hashCode debe diferir si los ids difieren");
}

}