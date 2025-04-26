package com.example.restapi.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.restapi.client.WebController;
import com.example.restapi.model.Generos;
import com.example.restapi.model.Pelicula;
import com.example.restapi.model.Series;
import com.example.restapi.model.Usuario;
import com.example.restapi.service.AuthService;
import com.example.restapi.service.DeustoStreamService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Collections;
import java.util.List;


@WebMvcTest(WebController.class)
class WebControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DeustoStreamService deustoStreamService;

    @MockitoBean
    private AuthService authService;


    @Test
    void testMostrarIndex() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    void testMostrarPanelAdmin() throws Exception {
        when(deustoStreamService.getAllPeliculas()).thenReturn(Collections.emptyList());
        when(deustoStreamService.getAllSeries()).thenReturn(Collections.emptyList());
        when(deustoStreamService.getAllUsuarios()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/admin")
                .sessionAttr("usuario", new Usuario())) // Aquí puedes poner cualquier objeto válido
                .andExpect(status().isOk())
                .andExpect(view().name("panelAdmin"))
                .andExpect(model().attributeExists("peliculas", "series", "usuarios"));
    }

    /*@Test
    void testCatalogoPage() throws Exception {
        List<Pelicula> peliculas = List.of(new Pelicula());
        List<Series> series = List.of(new Series());
        Usuario usuario = new Usuario();
        usuario.setListaMeGustaPeliculas(peliculas);
        usuario.setListaMeGustaSeries(series);
        when(deustoStreamService.getAllPeliculas()).thenReturn(peliculas);
        when(deustoStreamService.getAllSeries()).thenReturn(series);

        mockMvc.perform(get("/catalogo")
                .sessionAttr("usuario", usuario))
                .andExpect(status().isOk())
                .andExpect(view().name("catalogo"))
                .andExpect(model().attribute("peliculas", peliculas))
                .andExpect(model().attribute("series", series))
                .andExpect(model().attribute("peliculasFavoritas", peliculas))
                .andExpect(model().attribute("seriesFavoritas", series))
                .andExpect(model().attribute("usuario", usuario));
    }*/

    @Test
    void testMostrarPeliculas_AdminAutenticado() throws Exception {
        Usuario admin = new Usuario();
        admin.setCorreo("admin@deustostream.es");

        List<Pelicula> peliculas = List.of(
                new Pelicula("Matrix", Generos.ACCION, 120, 1999, "desc", "url"));
        when(deustoStreamService.getAllPeliculas()).thenReturn(peliculas);

        mockMvc.perform(get("/admin/peliculas")
                .sessionAttr("usuario", admin))
                .andExpect(status().isOk())
                .andExpect(view().name("peliculas"))
                .andExpect(model().attributeExists("peliculas"));
    }

    @Test
    void testMostrarPeliculas_NoAutorizado() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setCorreo("usuario@ejemplo.com");

        mockMvc.perform(get("/admin/peliculas")
                .sessionAttr("usuario", usuario))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/acceso-denegado"));
    }

    @Test
    void testMostrarSeries_Autorizado() throws Exception {
        Usuario admin = new Usuario();
        admin.setCorreo("admin@deustostream.es");
        List<Series> series = List.of(new Series());

        when(deustoStreamService.getAllSeries()).thenReturn(series);

        mockMvc.perform(get("/admin/series").sessionAttr("usuario", admin))
                .andExpect(status().isOk())
                .andExpect(view().name("series"))
                .andExpect(model().attribute("series", series));
    }

    @Test
    void testMostrarSeries_NoAutorizado() throws Exception {
        Usuario user = new Usuario();
        user.setCorreo("user@gmail.com");

        mockMvc.perform(get("/admin/series").sessionAttr("usuario", user))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/acceso-denegado"));
    }
    @Test
    void testMostrarAccesoDenegado() throws Exception {
        Usuario usuario = new Usuario(); // simula un usuario logueado
        usuario.setCorreo("usuario@correo.com");
    
        mockMvc.perform(get("/acceso-denegado").sessionAttr("usuario", usuario))
                .andExpect(status().isOk())
                .andExpect(view().name("accesoDenegado"));
    }

}