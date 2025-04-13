package com.example.restapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.example.restapi.model.Generos;

public class GenerosControllerTest {

    @Test
    void testGetGenerosReturnsAllEnumNames() {
        GenerosController generosController = new GenerosController();
        List<String> result = generosController.getGeneros();
        assertNotNull(result, "La lista no debería ser nula");
        assertEquals(Generos.values().length, result.size(), "La lista debe contener todos los géneros");
        for (Generos genero : Generos.values()) {
            assertTrue(result.contains(genero.name()), "Debe contener: " + genero.name());
        }
    }

        
}
