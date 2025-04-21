package com.example.restapi.unit.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.restapi.controller.DeustoStreamController;
import com.example.restapi.model.Generos;
import com.example.restapi.model.Pelicula;
import com.example.restapi.service.DeustoStreamService;

@ExtendWith(MockitoExtension.class)
public class DeustoStreamControllerUnitTest {
    @Mock
    private DeustoStreamService deustoStreamService;

    @InjectMocks
    private DeustoStreamController deustoStreamController;

    @Test
    void testGetAllPeliculas() {
        List<Pelicula> peliculas = List.of(
                new Pelicula("Inception", Generos.ACCION, 148, 2010, "Sueños", "url"),
                new Pelicula("Matrix", Generos.CIENCIA_FICCION, 136, 1999, "Neo", "url2"));

        when(deustoStreamService.getAllPeliculas()).thenReturn(peliculas);

        List<Pelicula> result = deustoStreamController.getAllPeliculas();

        assertEquals(2, result.size());
        assertEquals("Inception", result.get(0).getTitulo());
    }
}
