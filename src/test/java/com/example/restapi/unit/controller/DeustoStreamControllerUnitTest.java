package com.example.restapi.unit.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

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

    @Test
    void testGetPeliculaById_Found() {
        Pelicula pelicula = new Pelicula("Inception", Generos.ACCION, 148, 2010, "Sueños", "url");
        Long peliculaId = 1L;

        when(deustoStreamService.getPeliculaById(peliculaId)).thenReturn(Optional.of(pelicula));

        ResponseEntity<Pelicula> response = deustoStreamController.getPeliculaById(peliculaId);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Inception", response.getBody().getTitulo());
    }

    @Test
    void testGetPeliculaById_NotFound() {
        Long peliculaId = 2L;

        when(deustoStreamService.getPeliculaById(peliculaId)).thenReturn(Optional.empty());

        ResponseEntity<Pelicula> response = deustoStreamController.getPeliculaById(peliculaId);

        assertEquals(404, response.getStatusCode().value());
        assertEquals(null, response.getBody());
    }

    @Test
    void testCreatePelicula_ValidData() {
        Pelicula pelicula = new Pelicula("Interstellar", Generos.CIENCIA_FICCION, 169, 2014, "Espacio", "url3");
        Pelicula nuevaPelicula = new Pelicula("Interstellar", Generos.CIENCIA_FICCION, 169, 2014, "Espacio", "url3");

        when(deustoStreamService.createPelicula(pelicula)).thenReturn(nuevaPelicula);

        ResponseEntity<Pelicula> response = deustoStreamController.createPelicula(pelicula);

        assertEquals(201, response.getStatusCode().value());
        assertEquals("Interstellar", response.getBody().getTitulo());
    }

    @Test
    void testCreatePelicula_InvalidData_NullPelicula() {
        ResponseEntity<Pelicula> response = deustoStreamController.createPelicula(null);

        assertEquals(400, response.getStatusCode().value());
        assertEquals(null, response.getBody());
    }

    @Test
    void testCreatePelicula_InvalidData_MissingTitulo() {
        Pelicula pelicula = new Pelicula(null, Generos.ACCION, 120, 2022, "Sin título", "url");

        ResponseEntity<Pelicula> response = deustoStreamController.createPelicula(pelicula);

        assertEquals(400, response.getStatusCode().value());
        assertEquals(null, response.getBody());
    }

    @Test
    void testCreatePelicula_InvalidData_InvalidAnio() {
        Pelicula pelicula = new Pelicula("Titulo", Generos.ACCION, 120, -1, "Descripción", "url");

        ResponseEntity<Pelicula> response = deustoStreamController.createPelicula(pelicula);

        assertEquals(400, response.getStatusCode().value());
        assertEquals(null, response.getBody());
    }
}
