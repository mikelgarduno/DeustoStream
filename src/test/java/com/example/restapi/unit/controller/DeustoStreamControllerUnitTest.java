package com.example.restapi.unit.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.restapi.controller.DeustoStreamController;
import com.example.restapi.model.Capitulo;
import com.example.restapi.model.Generos;
import com.example.restapi.model.Pelicula;
import com.example.restapi.model.Series;
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

    @Test
    void testUpdatePelicula_ValidData() {
        Long peliculaId = 1L;
        Pelicula peliculaDetails = new Pelicula("Updated Title", Generos.DRAMA, 150, 2021, "Updated Description",
                "updatedUrl");
        Pelicula updatedPelicula = new Pelicula("Updated Title", Generos.DRAMA, 150, 2021, "Updated Description",
                "updatedUrl");

        when(deustoStreamService.updatePelicula(peliculaId, peliculaDetails)).thenReturn(updatedPelicula);

        ResponseEntity<Pelicula> response = deustoStreamController.updatePelicula(peliculaId, peliculaDetails);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Updated Title", response.getBody().getTitulo());
    }

    @Test
    void testUpdatePelicula_NotFound() {
        Long peliculaId = 99L;
        Pelicula peliculaDetails = new Pelicula("Nonexistent", Generos.DRAMA, 120, 2020, "Nonexistent Description",
                "url");

        when(deustoStreamService.updatePelicula(peliculaId, peliculaDetails))
                .thenThrow(new RuntimeException("Not Found"));

        ResponseEntity<Pelicula> response = deustoStreamController.updatePelicula(peliculaId, peliculaDetails);

        assertEquals(400, response.getStatusCode().value());
        assertEquals(null, response.getBody());
    }

    @Test
    void testDeletePelicula_Found() {
        Long peliculaId = 1L;

        when(deustoStreamService.getPeliculaById(peliculaId)).thenReturn(Optional.of(new Pelicula()));

        ResponseEntity<Void> response = deustoStreamController.deletePelicula(peliculaId);

        assertEquals(204, response.getStatusCode().value());
    }

    @Test
    void testDeletePelicula_NotFound() {
        Long peliculaId = 99L;

        when(deustoStreamService.getPeliculaById(peliculaId)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = deustoStreamController.deletePelicula(peliculaId);

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void testGetAllSeries() {
        List<Series> seriesList = List.of(
                new Series("Breaking Bad", 2008, "Descripcion", Generos.DRAMA, null, "url"),
                new Series("Game of Thrones", 2011, "Descripcion", Generos.FANTASIA, null, "url2"));

        when(deustoStreamService.getAllSeries()).thenReturn(seriesList);

        List<Series> result = deustoStreamController.getAllSeries();

        assertEquals(2, result.size());
        assertEquals("Breaking Bad", result.get(0).getTitulo());
    }

    @Test
    void testGetSeriesById_Found() {
        Series series = new Series("Breaking Bad", 2008, "Descripcion", Generos.DRAMA, null, "url");
        Long seriesId = 1L;

        when(deustoStreamService.getSeriesById(seriesId)).thenReturn(Optional.of(series));

        ResponseEntity<Series> response = deustoStreamController.getSeriesById(seriesId);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Breaking Bad", response.getBody().getTitulo());
    }

    @Test
    void testGetSeriesById_NotFound() {
        Long seriesId = 2L;

        when(deustoStreamService.getSeriesById(seriesId)).thenReturn(Optional.empty());

        ResponseEntity<Series> response = deustoStreamController.getSeriesById(seriesId);

        assertEquals(404, response.getStatusCode().value());
        assertEquals(null, response.getBody());
    }

    @Test
    void testCreateSeries_AutoGenerarCapitulos() {
        // Crear una serie SIN capítulos todavía, pero con numeroCapitulos > 0
        Series series = new Series();
        series.setTitulo("The Witcher");
        series.setAnio(2019);
        series.setDescripcion("Fantasia épica");
        series.setGenero(Generos.FANTASIA);
        series.setImagenUrl("url3");
        series.setNumeroCapitulos(2); // <- ¡Esto activa tu if!

        // IMPORTANTE: no hace falta meter capitulos manualmente, el controller los
        // creará.

        // Simula que cuando se guarda, ya tiene los capítulos creados
        Series serieConCapitulos = new Series();
        serieConCapitulos.setTitulo("The Witcher");
        serieConCapitulos.setAnio(2019);
        serieConCapitulos.setDescripcion("Fantasia épica");
        serieConCapitulos.setGenero(Generos.FANTASIA);
        serieConCapitulos.setImagenUrl("url3");
        serieConCapitulos.setNumeroCapitulos(2);

        // Creamos los capítulos generados
        List<Capitulo> capitulos = List.of(
                new Capitulo("Capítulo 1 de The Witcher", 30),
                new Capitulo("Capítulo 2 de The Witcher", 30));
        serieConCapitulos.setCapitulos(capitulos);

        when(deustoStreamService.createSeries(any(Series.class))).thenReturn(serieConCapitulos);

        // Ejecutamos el controller
        ResponseEntity<Series> response = deustoStreamController.createSeries(series);

        // Assertions
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getCapitulos().size());
        assertEquals("Capítulo 1 de The Witcher", response.getBody().getCapitulos().get(0).getTitulo());
    }

    @Test
    void testUpdateSeries_ValidData() {
        Long seriesId = 1L;
        Series seriesDetails = new Series("Updated Series", 2020, "Updated Description", Generos.DRAMA, null,
                "updatedUrl");
        Series updatedSeries = new Series("Updated Series", 2020, "Updated Description", Generos.DRAMA, null,
                "updatedUrl");

        when(deustoStreamService.updateSeries(seriesId, seriesDetails)).thenReturn(updatedSeries);

        ResponseEntity<Series> response = deustoStreamController.updateSeries(seriesId, seriesDetails);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Updated Series", response.getBody().getTitulo());
    }

    @Test
    void testUpdateSeries_NotFound() {
        Long seriesId = 99L;
        Series seriesDetails = new Series("Nonexistent Series", 2020, "Nonexistent Description", Generos.DRAMA, null,
                "url");

        when(deustoStreamService.updateSeries(seriesId, seriesDetails)).thenThrow(new RuntimeException("Not Found"));

        ResponseEntity<Series> response = deustoStreamController.updateSeries(seriesId, seriesDetails);

        assertEquals(400, response.getStatusCode().value());
        assertEquals(null, response.getBody());
    }

    @Test
    void testDeleteSeries_Found() {
        Long seriesId = 1L;

        when(deustoStreamService.getSeriesById(seriesId)).thenReturn(Optional.of(new Series()));

        ResponseEntity<Void> response = deustoStreamController.deleteSeries(seriesId);

        assertEquals(204, response.getStatusCode().value());
    }

    @Test
    void testDeleteSeries_NotFound() {
        Long seriesId = 99L;

        when(deustoStreamService.getSeriesById(seriesId)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = deustoStreamController.deleteSeries(seriesId);

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void testCreateSeries_InvalidData_NullSeries() {
        ResponseEntity<Series> response = deustoStreamController.createSeries(null);

        assertEquals(400, response.getStatusCode().value());
        assertEquals(null, response.getBody());
    }
}
