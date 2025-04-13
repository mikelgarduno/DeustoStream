package com.example.restapi.integration;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasSize;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.example.restapi.model.Generos;
import com.example.restapi.model.Pelicula;
import com.example.restapi.model.Series;
import com.example.restapi.model.Usuario;
import com.example.restapi.service.DeustoStreamService;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class DeustoStreamIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DeustoStreamService deustoStreamService;

    @Test
    void testGetAllPeliculas() throws Exception {
        Pelicula pelicula1 = new Pelicula("Inception", Generos.ACCION, 148, 2010, "Un sueño dentro de otro", "url1");
        when(deustoStreamService.getAllPeliculas()).thenReturn(List.of(pelicula1));
        mockMvc.perform(get("/api/peliculas")
                .sessionAttr("usuario", new Usuario())) // aquí puedes poner cualquier objeto válido
                .andExpect(status().isOk());
        
    }

    @Test
    void testGetPeliculaById() throws Exception {
        Pelicula pelicula = new Pelicula("Inception", Generos.ACCION, 148, 2010, "Un sueño dentro de otro", "url1");
        pelicula.setId(1L);
        when(deustoStreamService.getPeliculaById(1L)).thenReturn(java.util.Optional.of(pelicula));
        mockMvc.perform(get("/api/peliculas/1")
                .sessionAttr("usuario", new Usuario())) // aquí puedes poner cualquier objeto válido
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Inception"))
                .andExpect(jsonPath("$.genero").value(Generos.ACCION.toString()))
                .andExpect(jsonPath("$.duracion").value(148))
                .andExpect(jsonPath("$.anio").value(2010))
                .andExpect(jsonPath("$.sinopsis").value("Un sueño dentro de otro"));

    }
    @Test
    void testGetAllSeries() throws Exception {
        Series serie1 = new Series("Breaking Bad", 2008, "Un profesor de química se convierte en fabricante de metanfetaminas",Generos.ACCION, new ArrayList<>(), "url2");
        when(deustoStreamService.getAllSeries()).thenReturn(List.of(serie1));
        mockMvc.perform(get("/api/series")
                .sessionAttr("usuario", new Usuario())) // aquí puedes poner cualquier objeto válido
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].titulo").value("Breaking Bad"));
    }
    @Test
    void testGetSeriesById() throws Exception {
        Series serie = new Series("Breaking Bad", 2008, "Un profesor de química se convierte en fabricante de metanfetaminas",Generos.ACCION, new ArrayList<>(), "url2");
        serie.setId(1L);
        when(deustoStreamService.getSeriesById(1L)).thenReturn(java.util.Optional.of(serie));
        mockMvc.perform(get("/api/series/1")
                .sessionAttr("usuario", new Usuario())) // aquí puedes poner cualquier objeto válido
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Breaking Bad"))
                .andExpect(jsonPath("$.anio").value(2008))
                .andExpect(jsonPath("$.descripcion").value("Un profesor de química se convierte en fabricante de metanfetaminas"));
    }
}
