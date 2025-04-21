package com.example.restapi.unit.modelos;

import com.example.restapi.model.Capitulo;
import com.example.restapi.model.Series;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CapitulosTest {

    @Test
    public void testCapituloConstructor() {
        Capitulo capitulo = new Capitulo("Capítulo 1", 45);
        assertNotNull(capitulo);
        assertEquals("Capítulo 1", capitulo.getTitulo());
        assertEquals(45, capitulo.getDuracion());
    }

    @Test
    public void testGettersAndSetters() {
        Capitulo capitulo = new Capitulo();
        capitulo.setId(1L);
        capitulo.setTitulo("Capítulo 2");
        capitulo.setDuracion(50);

        assertEquals(1L, capitulo.getId());
        assertEquals("Capítulo 2", capitulo.getTitulo());
        assertEquals(50, capitulo.getDuracion());
    }

    @Test
    public void testSetAndGetSerie() {
        Series serie = new Series();
        serie.setId(10L);
        Capitulo capitulo = new Capitulo("Capítulo con serie", 30);
        capitulo.setSerie(serie);

        assertNotNull(capitulo.getSerie());
        assertEquals(10L, capitulo.getSerie().getId());
    }

    @Test
    public void testToString() {
        Capitulo capitulo = new Capitulo("Final Season", 60);
        capitulo.setId(99L);
        String result = capitulo.toString();
        assertTrue(result.contains("Final Season"));
        assertTrue(result.contains("60"));
    }
}
