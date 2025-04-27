package com.example.restapi.integration;

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

import com.example.restapi.controller.DeustoStreamController;
import com.example.restapi.model.Capitulo;
import com.example.restapi.model.Generos;
import com.example.restapi.model.Pelicula;
import com.example.restapi.model.Series;
import com.example.restapi.model.Usuario;
import com.example.restapi.service.DeustoStreamService;

@WebMvcTest(DeustoStreamController.class)
@AutoConfigureMockMvc
public class DeustoStreamControllerIT {
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
                                new Series("Breaking Bad", 2008,
                                                "Un profesor de química se convierte en fabricante de metanfetaminas",
                                                Generos.ACCION, new ArrayList<>(), "url2"),
                                new Series("Game of Thrones", 2011, "Lucha por el trono de hierro", Generos.FANTASIA,
                                                new ArrayList<>(),
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
            Series serie = new Series("Success", 2022, "desc", Generos.DRAMA, new ArrayList<>(), "url");
            serie.setId(1L);
            serie.setNumeroCapitulos(1);
        
            when(deustoStreamService.createSeries(any(Series.class))).thenReturn(serie);
        
            mockMvc.perform(post("/api/series")
                    .sessionAttr("usuario", new Usuario())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {
                            "titulo": "Success",
                            "anio": 2022,
                            "descripcion": "desc",
                            "genero": "DRAMA",
                            "numeroCapitulos": 1,
                            "url": "url"
                        }
                    """))
                    .andExpect(status().isCreated());
        
            // si falla
            when(deustoStreamService.createSeries(any(Series.class))).thenThrow(new RuntimeException("Error"));
        
            mockMvc.perform(post("/api/series")
                    .sessionAttr("usuario", new Usuario())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {
                            "titulo": "Failure",
                            "anio": 2022,
                            "descripcion": "desc",
                            "genero": "DRAMA",
                            "numeroCapitulos": 1,
                            "url": "url"
                        }
                    """))
                    .andExpect(status().isBadRequest());
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

        @Test
        void testAddCapituloToSerie() throws Exception {
        Series serie = new Series("Serie Test", 2020, "Descripción", Generos.ACCION, new ArrayList<>(), "url");
        serie.setId(1L);

        Capitulo capitulo = new Capitulo(); 
        capitulo.setTitulo("Capítulo 1 de Serie Test");
        capitulo.setDuracion(30);
        capitulo.setSerie(serie);

        when(deustoStreamService.getSeriesById(1L)).thenReturn(Optional.of(serie));
        when(deustoStreamService.createCapitulo(any(Capitulo.class))).thenReturn(capitulo);

        mockMvc.perform(post("/api/series/1/capitulos")
                .sessionAttr("usuario", new Usuario())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "titulo": "",
                        "duracion": 0
                        }
                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value("Capítulo 1 de Serie Test"))
                .andExpect(jsonPath("$.duracion").value(30));
        }
        
        @Test
        void testAddCapituloToSerie_SerieNoExiste() throws Exception {
            when(deustoStreamService.getSeriesById(99L)).thenReturn(Optional.empty());
        
            mockMvc.perform(post("/api/series/99/capitulos")
                    .sessionAttr("usuario", new Usuario())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {
                            "titulo": "Cualquiera",
                            "duracion": 30
                        }
                    """))
                    .andExpect(status().isNotFound());
        }

        @Test
        void testAddCapituloWithTituloYDuracionYaDefinidos() throws Exception {
        Series serie = new Series("Serie Existente", 2023, "desc", Generos.ACCION, new ArrayList<>(), "img");
        serie.setId(1L);

        Capitulo capitulo = new Capitulo("Título definido", 45);
        capitulo.setSerie(serie);

        when(deustoStreamService.getSeriesById(1L)).thenReturn(Optional.of(serie));
        when(deustoStreamService.createCapitulo(any(Capitulo.class))).thenReturn(capitulo);

        mockMvc.perform(post("/api/series/1/capitulos")
                .sessionAttr("usuario", new Usuario())
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "titulo": "Título definido",
                        "duracion": 45
                        }
                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value("Título definido"))
                .andExpect(jsonPath("$.duracion").value(45));
        }

        

        @Test
        void testCreateSeriesWithCapitulosAutoGenerados() throws Exception {
            Series serie = new Series("AutoCapitulos", 2024, "desc", Generos.DRAMA, new ArrayList<>(), "img");
            serie.setId(1L);
            serie.setNumeroCapitulos(2);
        
            when(deustoStreamService.createSeries(any(Series.class))).thenReturn(serie);
        
            mockMvc.perform(post("/api/series")
                    .sessionAttr("usuario", new Usuario())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("""
                        {
                            "titulo": "AutoCapitulos",
                            "anio": 2024,
                            "descripcion": "desc",
                            "genero": "DRAMA",
                            "numeroCapitulos": 2,
                            "url": "img"
                        }
                    """))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.titulo").value("AutoCapitulos"))
                    .andExpect(jsonPath("$.anio").value(2024));
        }
        
        @Test
        void testAddCapituloToSerieNotFound() throws Exception {
                when(deustoStreamService.getSeriesById(99L)).thenReturn(Optional.empty());

                mockMvc.perform(post("/api/series/99/capitulos")
                                .sessionAttr("usuario", new Usuario())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                    {
                                                        "titulo": "Cap perdido",
                                                        "duracion": 45
                                                    }
                                                """))
                                .andExpect(status().isNotFound());
        }

        @Test
        void testUpdateCapitulo() throws Exception {
                Capitulo actualizado = new Capitulo("Capitulo actualizado", 60);
                when(deustoStreamService.updateCapitulo(eq(1L), any(Capitulo.class))).thenReturn(actualizado);

                mockMvc.perform(put("/api/capitulos/1")
                                .sessionAttr("usuario", new Usuario())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                    {
                                                        "titulo": "Capitulo actualizado",
                                                        "duracion": 60
                                                    }
                                                """))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.titulo").value("Capitulo actualizado"))
                                .andExpect(jsonPath("$.duracion").value(60));
        }

        @Test
        void testUpdateCapitulo_BadRequest() throws Exception {
                when(deustoStreamService.updateCapitulo(eq(1L), any(Capitulo.class)))
                                .thenThrow(new RuntimeException("error"));

                mockMvc.perform(put("/api/capitulos/1")
                                .sessionAttr("usuario", new Usuario())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                                    {
                                                        "titulo": "X",
                                                        "duracion": 10
                                                    }
                                                """))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void testDeleteCapituloSuccess() throws Exception {
                Capitulo cap = new Capitulo("Capitulo final", 50);
                cap.setId(1L);

                when(deustoStreamService.getCapituloById(1L)).thenReturn(Optional.of(cap));
                doNothing().when(deustoStreamService).deleteCapitulo(1L);

                mockMvc.perform(delete("/api/capitulos/1")
                                .sessionAttr("usuario", new Usuario()))
                                .andExpect(status().isNoContent());
        }

        @Test
        void testDeleteCapituloNotFound() throws Exception {
                when(deustoStreamService.getCapituloById(1L)).thenReturn(Optional.empty());

                mockMvc.perform(delete("/api/capitulos/1")
                                .sessionAttr("usuario", new Usuario()))
                                .andExpect(status().isNotFound());
        }

}
