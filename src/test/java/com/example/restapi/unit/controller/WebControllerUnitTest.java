package com.example.restapi.unit.controller;
import static org.mockito.Mockito.*;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import com.example.restapi.service.DeustoStreamService;
import com.example.restapi.client.WebController;
import com.example.restapi.model.Generos;
import com.example.restapi.model.Pelicula;
import com.example.restapi.model.Series;
import com.example.restapi.model.Usuario;
import com.example.restapi.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;


public class WebControllerUnitTest {

    @Mock
    private DeustoStreamService deustoStreamService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private WebController webController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testMostrarIndex() {
        String viewName = webController.mostrarIndex();
        assertEquals("index", viewName, "The view name should be 'index'");
    }

    @Test
    void testMostrarPeliculas_NoUserInSession() {
        HttpSession mockSession = mock(HttpSession.class);
        Model mockModel = mock(Model.class);

        when(mockSession.getAttribute("usuario")).thenReturn(null);

        String viewName = webController.mostrarPeliculas(mockModel, mockSession);

        assertEquals("redirect:/acceso-denegado", viewName, "The view name should be 'redirect:/acceso-denegado'");
    }

    @Test
    void testMostrarPeliculas_UserWithoutEmail() {
        Usuario mockUsuario = new Usuario();
        HttpSession mockSession = mock(HttpSession.class);
        Model mockModel = mock(Model.class);

        when(mockSession.getAttribute("usuario")).thenReturn(mockUsuario);

        String viewName = webController.mostrarPeliculas(mockModel, mockSession);

        assertEquals("redirect:/acceso-denegado", viewName, "The view name should be 'redirect:/acceso-denegado'");
    }

    @Test
    void testMostrarPeliculas_UserWithInvalidEmailDomain() {
        Usuario mockUsuario = new Usuario();
        mockUsuario.setCorreo("user@example.com");
        HttpSession mockSession = mock(HttpSession.class);
        Model mockModel = mock(Model.class);

        when(mockSession.getAttribute("usuario")).thenReturn(mockUsuario);

        String viewName = webController.mostrarPeliculas(mockModel, mockSession);

        assertEquals("redirect:/acceso-denegado", viewName, "The view name should be 'redirect:/acceso-denegado'");
    }

    @Test
    void testMostrarPeliculas_ValidUserWithNoPeliculas() {
        Usuario mockUsuario = new Usuario();
        mockUsuario.setCorreo("admin@deustostream.es");
        HttpSession mockSession = mock(HttpSession.class);
        Model mockModel = mock(Model.class);

        when(mockSession.getAttribute("usuario")).thenReturn(mockUsuario);
        when(deustoStreamService.getAllPeliculas()).thenReturn(List.of());

        String viewName = webController.mostrarPeliculas(mockModel, mockSession);

        verify(mockModel).addAttribute("peliculas", List.of());
        assertEquals("peliculas", viewName, "The view name should be 'peliculas'");
    }

    @Test
    void testMostrarPeliculas_ValidUserWithMultiplePeliculas() {
        Usuario mockUsuario = new Usuario();
        mockUsuario.setCorreo("admin@deustostream.es");
        HttpSession mockSession = mock(HttpSession.class);
        Model mockModel = mock(Model.class);

        Pelicula pelicula1 = new Pelicula();
        pelicula1.setTitulo("Pelicula1");
        Pelicula pelicula2 = new Pelicula();
        pelicula2.setTitulo("Pelicula2");

        when(mockSession.getAttribute("usuario")).thenReturn(mockUsuario);
        when(deustoStreamService.getAllPeliculas()).thenReturn(List.of(pelicula1, pelicula2));

        String viewName = webController.mostrarPeliculas(mockModel, mockSession);

        verify(mockModel).addAttribute("peliculas", List.of(pelicula1, pelicula2));
        assertEquals("peliculas", viewName, "The view name should be 'peliculas'");
    }

    @Test
    void testMostrarPeliculas_AccessDenied_NoUser() {
        HttpSession mockSession = mock(HttpSession.class);
        Model mockModel = mock(Model.class);

        when(mockSession.getAttribute("usuario")).thenReturn(null);

        String viewName = webController.mostrarPeliculas(mockModel, mockSession);

        assertEquals("redirect:/acceso-denegado", viewName, "The view name should be 'redirect:/acceso-denegado'");
    }

    @Test
    void testMostrarPeliculas_AccessDenied_InvalidEmail() {
        Usuario mockUsuario = new Usuario();
        mockUsuario.setCorreo("user@example.com");
        HttpSession mockSession = mock(HttpSession.class);
        Model mockModel = mock(Model.class);

        when(mockSession.getAttribute("usuario")).thenReturn(mockUsuario);

        String viewName = webController.mostrarPeliculas(mockModel, mockSession);

        assertEquals("redirect:/acceso-denegado", viewName, "The view name should be 'redirect:/acceso-denegado'");
    }
    @Test
    void testMostrarSeries_NoUserInSession() {
        HttpSession mockSession = mock(HttpSession.class);
        Model mockModel = mock(Model.class);

        when(mockSession.getAttribute("usuario")).thenReturn(null);

        String viewName = webController.mostrarSeries(mockModel, mockSession);

        assertEquals("redirect:/acceso-denegado", viewName, "The view name should be 'redirect:/acceso-denegado'");
    }

    @Test
    void testMostrarSeries_UserWithoutEmail() {
        Usuario mockUsuario = new Usuario();
        HttpSession mockSession = mock(HttpSession.class);
        Model mockModel = mock(Model.class);

        when(mockSession.getAttribute("usuario")).thenReturn(mockUsuario);

        String viewName = webController.mostrarSeries(mockModel, mockSession);

        assertEquals("redirect:/acceso-denegado", viewName, "The view name should be 'redirect:/acceso-denegado'");
    }

    @Test
    void testMostrarSeries_UserWithInvalidEmailDomain() {
        Usuario mockUsuario = new Usuario();
        mockUsuario.setCorreo("user@example.com");
        HttpSession mockSession = mock(HttpSession.class);
        Model mockModel = mock(Model.class);

        when(mockSession.getAttribute("usuario")).thenReturn(mockUsuario);

        String viewName = webController.mostrarSeries(mockModel, mockSession);

        assertEquals("redirect:/acceso-denegado", viewName, "The view name should be 'redirect:/acceso-denegado'");
    }

    @Test
    void testMostrarSeries_ValidUserWithNoSeries() {
        Usuario mockUsuario = new Usuario();
        mockUsuario.setCorreo("admin@deustostream.es");
        HttpSession mockSession = mock(HttpSession.class);
        Model mockModel = mock(Model.class);

        when(mockSession.getAttribute("usuario")).thenReturn(mockUsuario);
        when(deustoStreamService.getAllSeries()).thenReturn(List.of());

        String viewName = webController.mostrarSeries(mockModel, mockSession);

        verify(mockModel).addAttribute("series", List.of());
        assertEquals("series", viewName, "The view name should be 'series'");
    }

    @Test
    void testMostrarSeries_ValidUserWithMultipleSeries() {
        Usuario mockUsuario = new Usuario();
        mockUsuario.setCorreo("admin@deustostream.es");
        HttpSession mockSession = mock(HttpSession.class);
        Model mockModel = mock(Model.class);

        Series serie1 = new Series();
        serie1.setTitulo("Serie1");
        Series serie2 = new Series();
        serie2.setTitulo("Serie2");

        when(mockSession.getAttribute("usuario")).thenReturn(mockUsuario);
        when(deustoStreamService.getAllSeries()).thenReturn(List.of(serie1, serie2));

        String viewName = webController.mostrarSeries(mockModel, mockSession);

        verify(mockModel).addAttribute("series", List.of(serie1, serie2));
        assertEquals("series", viewName, "The view name should be 'series'");
    }

    @Test
    void testMostrarUsuarios_NoUserInSession() {
        HttpSession mockSession = mock(HttpSession.class);
        Model mockModel = mock(Model.class);

        when(mockSession.getAttribute("usuario")).thenReturn(null);

        String viewName = webController.mostrarUsuarios(mockModel, mockSession);

        assertEquals("redirect:/acceso-denegado", viewName, "The view name should be 'redirect:/acceso-denegado'");
    }

    @Test
    void testMostrarUsuarios_UserWithoutEmail() {
        Usuario mockUsuario = new Usuario();
        HttpSession mockSession = mock(HttpSession.class);
        Model mockModel = mock(Model.class);

        when(mockSession.getAttribute("usuario")).thenReturn(mockUsuario);

        String viewName = webController.mostrarUsuarios(mockModel, mockSession);

        assertEquals("redirect:/acceso-denegado", viewName, "The view name should be 'redirect:/acceso-denegado'");
    }

    @Test
    void testMostrarUsuarios_UserWithInvalidEmailDomain() {
        Usuario mockUsuario = new Usuario();
        mockUsuario.setCorreo("user@example.com");
        HttpSession mockSession = mock(HttpSession.class);
        Model mockModel = mock(Model.class);

        when(mockSession.getAttribute("usuario")).thenReturn(mockUsuario);

        String viewName = webController.mostrarUsuarios(mockModel, mockSession);

        assertEquals("redirect:/acceso-denegado", viewName, "The view name should be 'redirect:/acceso-denegado'");
    }

    @Test
    void testMostrarUsuarios_ValidUserWithNoUsuarios() {
        Usuario mockUsuario = new Usuario();
        mockUsuario.setCorreo("admin@deustostream.es");
        HttpSession mockSession = mock(HttpSession.class);
        Model mockModel = mock(Model.class);

        when(mockSession.getAttribute("usuario")).thenReturn(mockUsuario);
        when(deustoStreamService.getAllUsuarios()).thenReturn(List.of());

        String viewName = webController.mostrarUsuarios(mockModel, mockSession);

        verify(mockModel).addAttribute("usuarios", List.of());
        assertEquals("usuarios", viewName, "The view name should be 'usuarios'");
    }

    @Test
    void testMostrarUsuarios_ValidUserWithMultipleUsuarios() {
        Usuario mockUsuario = new Usuario();
        mockUsuario.setCorreo("admin@deustostream.es");
        HttpSession mockSession = mock(HttpSession.class);
        Model mockModel = mock(Model.class);

        Usuario usuario1 = new Usuario();
        usuario1.setCorreo("user1@deustostream.es");
        Usuario usuario2 = new Usuario();
        usuario2.setCorreo("user2@deustostream.es");

        when(mockSession.getAttribute("usuario")).thenReturn(mockUsuario);
        when(deustoStreamService.getAllUsuarios()).thenReturn(List.of(usuario1, usuario2));

        String viewName = webController.mostrarUsuarios(mockModel, mockSession);

        verify(mockModel).addAttribute("usuarios", List.of(usuario1, usuario2));
        assertEquals("usuarios", viewName, "The view name should be 'usuarios'");
    }
/*
    @Test
    void testCatalogo_ValidUserWithNoFilters() {
        Usuario mockUsuario = new Usuario();
        mockUsuario.setListaMeGustaPeliculas(List.of());
        mockUsuario.setListaMeGustaSeries(List.of());
        HttpSession mockSession = mock(HttpSession.class);
        Model mockModel = mock(Model.class);

        when(mockSession.getAttribute("usuario")).thenReturn(mockUsuario);
        when(deustoStreamService.buscarPeliculasFiltradas(null, null)).thenReturn(List.of());
        when(deustoStreamService.buscarSeriesFiltradas(null, null)).thenReturn(List.of());

        String viewName = webController.catalogo(null, null, mockModel, mockSession);

        verify(mockModel).addAttribute("peliculas", List.of());
        verify(mockModel).addAttribute("series", List.of());
        verify(mockModel).addAttribute("peliculasFavoritas", List.of());
        verify(mockModel).addAttribute("seriesFavoritas", List.of());
        verify(mockModel).addAttribute("usuario", mockUsuario);
        verify(mockModel).addAttribute("generos", Generos.values());
        assertEquals("catalogo", viewName, "The view name should be 'catalogo'");
    }

    @Test
    void testCatalogo_ValidUserWithFavorites() {
        Usuario mockUsuario = new Usuario();
        Pelicula favoritePelicula = new Pelicula();
        favoritePelicula.setTitulo("FavoritePelicula");
        Series favoriteSerie = new Series();
        favoriteSerie.setTitulo("FavoriteSerie");
        mockUsuario.setListaMeGustaPeliculas(List.of(favoritePelicula));
        mockUsuario.setListaMeGustaSeries(List.of(favoriteSerie));
        HttpSession mockSession = mock(HttpSession.class);
        Model mockModel = mock(Model.class);

        when(mockSession.getAttribute("usuario")).thenReturn(mockUsuario);
        when(deustoStreamService.buscarPeliculasFiltradas(null, null)).thenReturn(List.of());
        when(deustoStreamService.buscarSeriesFiltradas(null, null)).thenReturn(List.of());

        String viewName = webController.catalogo(null, null, mockModel, mockSession);

        verify(mockModel).addAttribute("peliculas", List.of());
        verify(mockModel).addAttribute("series", List.of());
        verify(mockModel).addAttribute("peliculasFavoritas", List.of(favoritePelicula));
        verify(mockModel).addAttribute("seriesFavoritas", List.of(favoriteSerie));
        verify(mockModel).addAttribute("usuario", mockUsuario);
        verify(mockModel).addAttribute("generos", Generos.values());
        assertEquals("catalogo", viewName, "The view name should be 'catalogo'");
    }

    @Test
    void testVerPeliculas_ValidUserWithNoFilters() {
        Usuario mockUsuario = new Usuario();
        mockUsuario.setListaMeGustaPeliculas(List.of());
        HttpSession mockSession = mock(HttpSession.class);
        Model mockModel = mock(Model.class);

        when(mockSession.getAttribute("usuario")).thenReturn(mockUsuario);
        when(deustoStreamService.buscarPeliculasFiltradas(null, null)).thenReturn(List.of());

        String viewName = webController.verPeliculas(mockModel, mockSession, null, null);

        verify(mockModel).addAttribute("peliculas", List.of());
        verify(mockModel).addAttribute("peliculasFavoritas", List.of());
        verify(mockModel).addAttribute("usuario", mockUsuario);
        verify(mockModel).addAttribute("generos", Generos.values());
        assertEquals("catPeliculas", viewName, "The view name should be 'catPeliculas'");
    }

    @Test
    void testVerPeliculas_ValidUserWithFilters() {
        Usuario mockUsuario = new Usuario();
        mockUsuario.setListaMeGustaPeliculas(List.of());
        HttpSession mockSession = mock(HttpSession.class);
        Model mockModel = mock(Model.class);

        Pelicula pelicula1 = new Pelicula();
        pelicula1.setTitulo("FilteredPelicula1");
        Pelicula pelicula2 = new Pelicula();
        pelicula2.setTitulo("FilteredPelicula2");

        when(mockSession.getAttribute("usuario")).thenReturn(mockUsuario);
        when(deustoStreamService.buscarPeliculasFiltradas("Filtered", Generos.ACCION)).thenReturn(List.of(pelicula1, pelicula2));

        String viewName = webController.verPeliculas(mockModel, mockSession, "Filtered", Generos.ACCION);

        verify(mockModel).addAttribute("peliculas", List.of(pelicula1, pelicula2));
        verify(mockModel).addAttribute("peliculasFavoritas", List.of());
        verify(mockModel).addAttribute("usuario", mockUsuario);
        verify(mockModel).addAttribute("generos", Generos.values());
        assertEquals("catPeliculas", viewName, "The view name should be 'catPeliculas'");
    }

    @Test
    void testVerPeliculas_ValidUserWithFavorites() {
        Usuario mockUsuario = new Usuario();
        Pelicula favoritePelicula = new Pelicula();
        favoritePelicula.setTitulo("FavoritePelicula");
        mockUsuario.setListaMeGustaPeliculas(List.of(favoritePelicula));
        HttpSession mockSession = mock(HttpSession.class);
        Model mockModel = mock(Model.class);

        when(mockSession.getAttribute("usuario")).thenReturn(mockUsuario);
        when(deustoStreamService.buscarPeliculasFiltradas(null, null)).thenReturn(List.of());

        String viewName = webController.verPeliculas(mockModel, mockSession, null, null);

        verify(mockModel).addAttribute("peliculas", List.of());
        verify(mockModel).addAttribute("peliculasFavoritas", List.of(favoritePelicula));
        verify(mockModel).addAttribute("usuario", mockUsuario);
        verify(mockModel).addAttribute("generos", Generos.values());
        assertEquals("catPeliculas", viewName, "The view name should be 'catPeliculas'");
    }

    @Test
    void testVerSeries_ValidUserWithNoFilters() {
        Usuario mockUsuario = new Usuario();
        mockUsuario.setListaMeGustaSeries(List.of());
        HttpSession mockSession = mock(HttpSession.class);
        Model mockModel = mock(Model.class);

        when(mockSession.getAttribute("usuario")).thenReturn(mockUsuario);
        when(deustoStreamService.buscarSeriesFiltradas(null, null)).thenReturn(List.of());

        String viewName = webController.verSeries(mockModel, mockSession, null, null);

        verify(mockModel).addAttribute("series", List.of());
        verify(mockModel).addAttribute("seriesFavoritas", List.of());
        verify(mockModel).addAttribute("usuario", mockUsuario);
        verify(mockModel).addAttribute("generos", Generos.values());
        assertEquals("catSeries", viewName, "The view name should be 'catSeries'");
    }

    @Test
    void testVerSeries_ValidUserWithFilters() {
        Usuario mockUsuario = new Usuario();
        mockUsuario.setListaMeGustaSeries(List.of());
        HttpSession mockSession = mock(HttpSession.class);
        Model mockModel = mock(Model.class);

        Series serie1 = new Series();
        serie1.setTitulo("FilteredSerie1");
        Series serie2 = new Series();
        serie2.setTitulo("FilteredSerie2");

        when(mockSession.getAttribute("usuario")).thenReturn(mockUsuario);
        when(deustoStreamService.buscarSeriesFiltradas("Filtered", Generos.COMEDIA)).thenReturn(List.of(serie1, serie2));

        String viewName = webController.verSeries(mockModel, mockSession, "Filtered", Generos.COMEDIA);

        verify(mockModel).addAttribute("series", List.of(serie1, serie2));
        verify(mockModel).addAttribute("seriesFavoritas", List.of());
        verify(mockModel).addAttribute("usuario", mockUsuario);
        verify(mockModel).addAttribute("generos", Generos.values());
        assertEquals("catSeries", viewName, "The view name should be 'catSeries'");
    }

    @Test
    void testVerSeries_ValidUserWithFavorites() {
        Usuario mockUsuario = new Usuario();
        Series favoriteSerie = new Series();
        favoriteSerie.setTitulo("FavoriteSerie");
        mockUsuario.setListaMeGustaSeries(List.of(favoriteSerie));
        HttpSession mockSession = mock(HttpSession.class);
        Model mockModel = mock(Model.class);

        when(mockSession.getAttribute("usuario")).thenReturn(mockUsuario);
        when(deustoStreamService.buscarSeriesFiltradas(null, null)).thenReturn(List.of());

        String viewName = webController.verSeries(mockModel, mockSession, null, null);

        verify(mockModel).addAttribute("series", List.of());
        verify(mockModel).addAttribute("seriesFavoritas", List.of(favoriteSerie));
        verify(mockModel).addAttribute("usuario", mockUsuario);
        verify(mockModel).addAttribute("generos", Generos.values());
        assertEquals("catSeries", viewName, "The view name should be 'catSeries'");
    }
*/
}
