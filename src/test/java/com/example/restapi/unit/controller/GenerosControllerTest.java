package com.example.restapi.unit.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.example.restapi.controller.GenerosController;
import com.example.restapi.model.Generos;

public class GenerosControllerTest {

    @Test
    void testGetGenerosIsNotEmpty() {
        GenerosController generosController = new GenerosController();
        List<String> result = generosController.getGeneros();
        assertNotNull(result, "La lista no debería ser nula");
        assertTrue(!result.isEmpty(), "La lista no debería estar vacía");
    }

    @Test
    void testGetGenerosHasUniqueValues() {
        GenerosController generosController = new GenerosController();
        List<String> result = generosController.getGeneros();
        assertNotNull(result, "La lista no debería ser nula");
        long distinctCount = result.stream().distinct().count();
        assertEquals(result.size(), distinctCount, "La lista no debería contener valores duplicados");
    }

    @Test
    void testGetGenerosMatchesEnumOrder() {
        GenerosController generosController = new GenerosController();
        List<String> result = generosController.getGeneros();
        assertNotNull(result, "La lista no debería ser nula");
        String[] enumNames = Arrays.stream(Generos.values()).map(Enum::name).toArray(String[]::new);
        assertEquals(List.of(enumNames), result, "La lista debería coincidir con el orden de los valores del enum");
    }

        
}
