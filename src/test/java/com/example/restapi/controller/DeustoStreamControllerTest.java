package com.example.restapi.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.restapi.model.Generos;
import com.example.restapi.model.Pelicula;
import com.example.restapi.model.Series;
import com.example.restapi.model.Usuario;
import com.example.restapi.service.DeustoStreamService;

@WebMvcTest(DeustoStreamController.class)
@AutoConfigureMockMvc
public class DeustoStreamControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DeustoStreamService deustoStreamService;

    @Test
    void testGetAllPeliculas() throws Exception {
        List<Pelicula> peliculas = List.of(
                new Pelicula("Inception", Generos.ACCION, 148, 2010, "Sueños", "url"),
                new Pelicula("Matrix", Generos.CIENCIA_FICCION, 136, 1999, "Neo", "url2"));

        when(deustoStreamService.getAllPeliculas()).thenReturn(peliculas);

        mockMvc.perform(get("/api/peliculas")
                .sessionAttr("usuario", new Usuario())) // aquí puedes poner cualquier objeto válido
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].titulo").value("Inception"));
    }

    @Test
    void testGetPeliculaById() throws Exception {
        Pelicula pelicula = new Pelicula("Inception", Generos.ACCION, 148, 2010, "Sueños", "url");
        pelicula.setId(1L);

        when(deustoStreamService.getPeliculaById(1L)).thenReturn(java.util.Optional.of(pelicula));

        mockMvc.perform(get("/api/peliculas/1")
                .sessionAttr("usuario", new Usuario())) // aquí puedes poner cualquier objeto válido
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Inception"))
                .andExpect(jsonPath("$.genero").value(Generos.ACCION.toString()))
                .andExpect(jsonPath("$.duracion").value(148))
                .andExpect(jsonPath("$.anio").value(2010))
                .andExpect(jsonPath("$.sinopsis").value("Sueños"));
    }

    @Test
    void testGetPeliculaByIdNotFound() throws Exception {
        when(deustoStreamService.getPeliculaById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/peliculas/1")
                .sessionAttr("usuario", new Usuario())) // aquí puedes poner cualquier objeto válido
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllSeries() throws Exception {
        List<Series> series = List.of(
                new Series("Breaking Bad", 2008, "Un profesor de química se convierte en fabricante de metanfetaminas",
                        Generos.ACCION, new ArrayList<>(), "url2"),
                new Series("Game of Thrones", 2011, "Lucha por el trono de hierro", Generos.FANTASIA, new ArrayList<>(),
                        "url3"));

        when(deustoStreamService.getAllSeries()).thenReturn(series);

        mockMvc.perform(get("/api/series")
                .sessionAttr("usuario", new Usuario())) // aquí puedes poner cualquier objeto válido
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].titulo").value("Breaking Bad"));
    }

    @Test
    void testCreatePelicula() throws Exception {
        Pelicula pelicula = new Pelicula("Inception", Generos.ACCION, 148, 2010, "Sueños", "url");
        pelicula.setId(1L);

        when(deustoStreamService.createPelicula(any(Pelicula.class))).thenReturn(pelicula);
        mockMvc.perform(post("/api/peliculas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{\"titulo\":\"Inception\",\"genero\":\"ACCION\",\"duracion\":148,\"anio\":2010,\"sinopsis\":\"Sueños\",\"url\":\"url\"}")
                .sessionAttr("usuario", new Usuario())) // aquí puedes poner cualquier objeto válido
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value("Inception"))
                .andExpect(jsonPath("$.genero").value(Generos.ACCION.toString()))
                .andExpect(jsonPath("$.duracion").value(148))
                .andExpect(jsonPath("$.anio").value(2010))
                .andExpect(jsonPath("$.sinopsis").value("Sueños"));
    }

    @Test
    public void testCreatePelicula_BadRequest() throws Exception {
        String peliculaJson = """
                {
                    "titulo": null,
                    "genero": "ACCION",
                    "duracion": 0,
                    "anio": 0,
                    "sinopsis": "desc",
                    "url": "img"
                }
                """;

        mockMvc.perform(post("/api/peliculas")
                .sessionAttr("usuario", new Usuario()) // aquí puedes poner cualquier objeto válido
                .contentType(MediaType.APPLICATION_JSON)
                .content(peliculaJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdatePelicula() throws Exception {
        Pelicula pelicula = new Pelicula("Inception 2", Generos.ACCION, 150, 2025, "Sueños más locos", "url2");
        pelicula.setId(1L);

        when(deustoStreamService.updatePelicula(eq(1L), any(Pelicula.class))).thenReturn(pelicula);

        mockMvc.perform(put("/api/peliculas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{\"titulo\":\"Inception 2\",\"genero\":\"ACCION\",\"duracion\":150,\"anio\":2025,\"sinopsis\":\"Sueños más locos\",\"url\":\"url2\"}")
                .sessionAttr("usuario", new Usuario())) // aquí puedes poner cualquier objeto válido
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Inception 2"))
                .andExpect(jsonPath("$.genero").value(Generos.ACCION.toString()))
                .andExpect(jsonPath("$.duracion").value(150))
                .andExpect(jsonPath("$.anio").value(2025))
                .andExpect(jsonPath("$.sinopsis").value("Sueños más locos"));
    }

    @Test
    public void testUpdatePelicula_BadRequest() throws Exception {
        // Simular excepción en el servicio
        when(deustoStreamService.updatePelicula(eq(1L), any(Pelicula.class)))
                .thenThrow(new RuntimeException("Error"));

        Pelicula pelicula = new Pelicula(null, Generos.ACCION, 0, 0, "desc", "img");

        mockMvc.perform(put("/api/peliculas/1")
                .sessionAttr("usuario", new Usuario()) // aquí puedes poner cualquier objeto válido
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{\"titulo\":\"Inception 2\",\"genero\":\"ACCION\",\"duracion\":150,\"anio\":2025,\"sinopsis\":\"Sueños más locos\",\"url\":\"url2\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeletePeliculaFound() throws Exception {
        Pelicula pelicula = new Pelicula("Inception", Generos.ACCION, 148, 2010, "Sueños", "url");
        pelicula.setId(1L);

        when(deustoStreamService.getPeliculaById(1L)).thenReturn(Optional.of(pelicula));
        doNothing().when(deustoStreamService).deletePelicula(1L);

        mockMvc.perform(delete("/api/peliculas/1")
                .sessionAttr("usuario", new Usuario())) // aquí puedes poner cualquier objeto válido
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeletePeliculaNotFound() throws Exception {
        when(deustoStreamService.getPeliculaById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/peliculas/1")
                .sessionAttr("usuario", new Usuario())) // aquí puedes poner cualquier objeto válido
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetSeriesById() throws Exception {
        Series series = new Series("Breaking Bad", 2008,
                "Un profesor de química se convierte en fabricante de metanfetaminas",
                Generos.ACCION, new ArrayList<>(), "url2");
        series.setId(1L);

        when(deustoStreamService.getSeriesById(1L)).thenReturn(java.util.Optional.of(series));

        mockMvc.perform(get("/api/series/1")
                .sessionAttr("usuario", new Usuario())) // aquí puedes poner cualquier objeto válido
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Breaking Bad"))
                .andExpect(jsonPath("$.genero").value(Generos.ACCION.toString()))
                .andExpect(jsonPath("$.anio").value(2008))
                .andExpect(jsonPath("$.descripcion")
                        .value("Un profesor de química se convierte en fabricante de metanfetaminas"));
    }


    @Test
    void testGetSeriesByIdNotFound() throws Exception {
        when(deustoStreamService.getSeriesById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/series/1")
                .sessionAttr("usuario", new Usuario())) // aquí puedes poner cualquier objeto válido
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateSeries() throws Exception {
        Series series = new Series("Breaking Bad", 2008,
                "Un profesor de química se convierte en fabricante de metanfetaminas",
                Generos.ACCION, new ArrayList<>(), "url2");
        series.setId(1L);

        when(deustoStreamService.createSeries(any(Series.class))).thenReturn(series);
        mockMvc.perform(post("/api/series")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{\"titulo\":\"Breaking Bad\",\"anio\":2008,\"descripcion\":\"Un profesor de química se convierte en fabricante de metanfetaminas\",\"genero\":\"ACCION\",\"temporadas\":[],\"url\":\"url2\"}")
                .sessionAttr("usuario", new Usuario())) // aquí puedes poner cualquier objeto válido
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value("Breaking Bad"))
                .andExpect(jsonPath("$.genero").value(Generos.ACCION.toString()))
                .andExpect(jsonPath("$.anio").value(2008))
                .andExpect(jsonPath("$.descripcion")
                        .value("Un profesor de química se convierte en fabricante de metanfetaminas"));
    }

    @Test
    void testUpdateSeries() throws Exception {
        Series series = new Series("Breaking Bad", 2008,
                "Un profesor de química se convierte en fabricante de metanfetaminas",
                Generos.ACCION, new ArrayList<>(), "url2");
        series.setId(1L);

        when(deustoStreamService.updateSeries(eq(1L), any(Series.class))).thenReturn(series);

        mockMvc.perform(put("/api/series/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{\"titulo\":\"Breaking Bad\",\"anio\":2008,\"descripcion\":\"Un profesor de química se convierte en fabricante de metanfetaminas\",\"genero\":\"ACCION\",\"temporadas\":[],\"url\":\"url2\"}")
                .sessionAttr("usuario", new Usuario())) // aquí puedes poner cualquier objeto válido
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Breaking Bad"))
                .andExpect(jsonPath("$.genero").value(Generos.ACCION.toString()))
                .andExpect(jsonPath("$.anio").value(2008))
                .andExpect(jsonPath("$.descripcion")
                        .value("Un profesor de química se convierte en fabricante de metanfetaminas"));
    }

    @Test
    public void testUpdateSeries_BadRequest() throws Exception {
        // Simular excepción en el servicio
        when(deustoStreamService.updateSeries(eq(1L), any(Series.class)))
                .thenThrow(new RuntimeException("Error"));

        mockMvc.perform(put("/api/series/1")
                .sessionAttr("usuario", new Usuario()) // aquí puedes poner cualquier objeto válido
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{\"titulo\":\"Breaking Bad\",\"anio\":2008,\"descripcion\":\"Un profesor de química se convierte en fabricante de metanfetaminas\",\"genero\":\"ACCION\",\"temporadas\":[],\"url\":\"url2\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeleteSeriesFound() throws Exception {
        Series series = new Series("Breaking Bad", 2008,
                "Un profesor de química se convierte en fabricante de metanfetaminas",
                Generos.ACCION, new ArrayList<>(), "url2");
        series.setId(1L);

        when(deustoStreamService.getSeriesById(1L)).thenReturn(Optional.of(series));
        doNothing().when(deustoStreamService).deleteSeries(1L);

        mockMvc.perform(delete("/api/series/1")
                .sessionAttr("usuario", new Usuario())) // aquí puedes poner cualquier objeto válido
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteSeriesNotFound() throws Exception {
        when(deustoStreamService.getSeriesById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/series/1")
                .sessionAttr("usuario", new Usuario())) // aquí puedes poner cualquier objeto válido
                .andExpect(status().isNotFound());
    }

}
