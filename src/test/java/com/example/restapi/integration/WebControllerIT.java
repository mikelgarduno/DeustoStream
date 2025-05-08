package com.example.restapi.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.restapi.client.WebController;
import com.example.restapi.model.Generos;
import com.example.restapi.model.Pelicula;
import com.example.restapi.model.Perfil;
import com.example.restapi.model.Series;
import com.example.restapi.model.Usuario;
import com.example.restapi.service.AuthService;
import com.example.restapi.service.DeustoStreamService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Collections;
import java.util.List;

@WebMvcTest(WebController.class)
class WebControllerIT {

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
    void testMostrarPerfil_UsuarioAutenticado() throws Exception {
        Usuario usuario = new Usuario();
        Perfil perfil = new Perfil("Test", "avatar.png");
        usuario.getPerfiles().add(perfil);

        mockMvc.perform(get("/perfil")
                .sessionAttr("usuario", usuario))
                .andExpect(status().isOk())
                .andExpect(view().name("perfil"))
                .andExpect(model().attribute("usuario", usuario))
                .andExpect(model().attribute("perfiles", usuario.getPerfiles()))
                .andExpect(model().attribute("avatar", "avatar.png"));
    }

    @Test
    void testValorarPelicula_UsuarioAutenticado() throws Exception {
        Long peliculaId = 1L;
        int puntuacion = 5;
        String comentario = "Muy buena";
        Usuario usuario = new Usuario();
        Perfil perfil = new Perfil("TestPerfil", "avatar.png");

        // No need to mock deustoStreamService.valorarPelicula as it returns void

        mockMvc.perform(
                post("/pelicula/{id}/valorar", peliculaId)
                        .param("puntuacion", String.valueOf(puntuacion))
                        .param("comentario", comentario)
                        .sessionAttr("usuario", usuario)
                        .sessionAttr("perfil", perfil))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pelicula/" + peliculaId));

        // Verify service call
        verify(deustoStreamService).valorarPelicula(peliculaId, perfil, puntuacion, comentario);
    }

    @Test
    void testValorarPelicula_SinUsuarioEnSesion() throws Exception {
        Long peliculaId = 1L;
        int puntuacion = 4;
        String comentario = "Entretenida";

        mockMvc.perform(
                post("/pelicula/{id}/valorar", peliculaId)
                        .param("puntuacion", String.valueOf(puntuacion))
                        .param("comentario", comentario)
        // No usuario in session
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verify(deustoStreamService, never()).valorarPelicula(anyLong(), any(), anyInt(), anyString());
    }

}