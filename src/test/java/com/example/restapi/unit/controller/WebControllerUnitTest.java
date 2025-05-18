package com.example.restapi.unit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import com.example.restapi.service.DeustoStreamService;
import com.example.restapi.client.WebController;
import com.example.restapi.model.Generos;
import com.example.restapi.model.Pelicula;
import com.example.restapi.model.Perfil;
import com.example.restapi.model.Series;
import com.example.restapi.model.Usuario;
import com.example.restapi.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

class WebControllerUnitTest {

    @Mock
    private DeustoStreamService deustoStreamService;

    @Mock
    private AuthService authService;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

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
    void testMostrarPanelAdmin() {
        Model mockModel = mock(Model.class);

        List<Pelicula> mockPeliculas = List.of(new Pelicula());
        List<Series> mockSeries = List.of(new Series());
        List<Usuario> mockUsuarios = List.of(new Usuario());

        when(deustoStreamService.getAllPeliculas()).thenReturn(mockPeliculas);
        when(deustoStreamService.getAllSeries()).thenReturn(mockSeries);
        when(deustoStreamService.getAllUsuarios()).thenReturn(mockUsuarios);

        String viewName = webController.mostrarPanelAdmin(mockModel);

        verify(mockModel).addAttribute("peliculas", mockPeliculas);
        verify(mockModel).addAttribute("series", mockSeries);
        verify(mockModel).addAttribute("usuarios", mockUsuarios);
        assertEquals("panelAdmin", viewName, "The view name should be 'panelAdmin'");
    }

    @Test
    void testAccesoDenegado() {
        String viewName = webController.accesoDenegado();
        assertEquals("accesoDenegado", viewName, "The view name should be 'accesoDenegado'");
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

    @Test
    void testCatalogo_NoUserInSession() {
        HttpSession mockSession = mock(HttpSession.class);
        Model mockModel = mock(Model.class);

        when(mockSession.getAttribute("usuario")).thenReturn(null);

        assertThrows(NullPointerException.class, () -> {
            webController.catalogo(null, null, null, null, mockModel, mockSession);
        });
    }

    @Test
    void testCatalogo_ValidUserWithNoFilters() {
        Usuario mockUsuario = new Usuario();
        Perfil mockPerfil = new Perfil();
        mockPerfil.setListaMeGustaPeliculas(List.of());
        mockPerfil.setListaMeGustaSeries(List.of());
        mockUsuario.setPerfiles(List.of(mockPerfil));

        HttpSession mockSession = mock(HttpSession.class);
        Model mockModel = mock(Model.class);

        when(mockSession.getAttribute("usuario")).thenReturn(mockUsuario);
        when(deustoStreamService.buscarPeliculasFiltradas(null, null)).thenReturn(List.of());
        when(deustoStreamService.buscarSeriesFiltradas(null, null)).thenReturn(List.of());

        String viewName = webController.catalogo(null, null, null, null, mockModel, mockSession);

        verify(mockModel).addAttribute("peliculas", List.of());
        verify(mockModel).addAttribute("series", List.of());
        verify(mockModel).addAttribute("peliculasFavoritas", List.of());
        verify(mockModel).addAttribute("seriesFavoritas", List.of());
        verify(mockModel).addAttribute("usuario", mockUsuario);
        verify(mockModel).addAttribute(eq("generos"), any());
        assertEquals("catalogo", viewName, "The view name should be 'catalogo'");
    }

    @Test
    void testCatalogo_ValidUserWithFilters() {
        Usuario mockUsuario = new Usuario();
        Perfil mockPerfil = new Perfil();
        mockPerfil.setListaMeGustaPeliculas(List.of());
        mockPerfil.setListaMeGustaSeries(List.of());
        mockUsuario.setPerfiles(List.of(mockPerfil));

        HttpSession mockSession = mock(HttpSession.class);
        Model mockModel = mock(Model.class);

        Pelicula pelicula1 = new Pelicula();
        pelicula1.setTitulo("FilteredPelicula");
        Series serie1 = new Series();
        serie1.setTitulo("FilteredSerie");

        when(mockSession.getAttribute("usuario")).thenReturn(mockUsuario);
        when(deustoStreamService.buscarPeliculasFiltradas("test", null)).thenReturn(List.of(pelicula1));
        when(deustoStreamService.buscarSeriesFiltradas("test", null)).thenReturn(List.of(serie1));

        String viewName = webController.catalogo("test", null, null, null, mockModel, mockSession);

        verify(mockModel).addAttribute("peliculas", List.of(pelicula1));
        verify(mockModel).addAttribute("series", List.of(serie1));
        verify(mockModel).addAttribute("peliculasFavoritas", List.of());
        verify(mockModel).addAttribute("seriesFavoritas", List.of());
        verify(mockModel).addAttribute("usuario", mockUsuario);
        verify(mockModel).addAttribute(eq("generos"), any());
        assertEquals("catalogo", viewName, "The view name should be 'catalogo'");
    }

    @Test
    void testCatalogo_FilterByDuration() {
        Usuario mockUsuario = new Usuario();
        Perfil mockPerfil = new Perfil();
        mockPerfil.setListaMeGustaPeliculas(List.of());
        mockPerfil.setListaMeGustaSeries(List.of());
        mockUsuario.setPerfiles(List.of(mockPerfil));

        HttpSession mockSession = mock(HttpSession.class);
        Model mockModel = mock(Model.class);

        Pelicula pelicula1 = new Pelicula("Short Movie", Generos.ACCION, 90, 2010, "Desc", "url");
        Pelicula pelicula2 = new Pelicula("Long Movie", Generos.ACCION, 150, 2010, "Desc", "url");

        when(mockSession.getAttribute("usuario")).thenReturn(mockUsuario);
        when(deustoStreamService.buscarPeliculasFiltradas(null, null))
                .thenReturn(new java.util.ArrayList<>(List.of(pelicula1, pelicula2)));
        when(deustoStreamService.buscarSeriesFiltradas(null, null)).thenReturn(new java.util.ArrayList<>(List.of()));

        String viewName = webController.catalogo(null, null, "100", null, mockModel, mockSession);

        verify(mockModel).addAttribute("peliculas", List.of(pelicula1));
        verify(mockModel).addAttribute("series", List.of());
        assertEquals("catalogo", viewName);
    }

    @Test
    void testCatalogo_FilterByYearBefore2000() {
        Usuario mockUsuario = new Usuario();
        Perfil mockPerfil = new Perfil();
        mockPerfil.setListaMeGustaPeliculas(List.of());
        mockPerfil.setListaMeGustaSeries(List.of());
        mockUsuario.setPerfiles(List.of(mockPerfil));

        HttpSession mockSession = mock(HttpSession.class);
        Model mockModel = mock(Model.class);

        Pelicula pelicula1 = new Pelicula("Old Movie", Generos.ACCION, 90, 1995, "Desc", "url");
        Pelicula pelicula2 = new Pelicula("New Movie", Generos.ACCION, 90, 2005, "Desc", "url");

        when(mockSession.getAttribute("usuario")).thenReturn(mockUsuario);
        when(deustoStreamService.buscarPeliculasFiltradas(null, null))
                .thenReturn(new java.util.ArrayList<>(List.of(pelicula1, pelicula2)));
        when(deustoStreamService.buscarSeriesFiltradas(null, null)).thenReturn(new java.util.ArrayList<>(List.of()));

        String viewName = webController.catalogo(null, null, null, "2000", mockModel, mockSession);

        verify(mockModel).addAttribute("peliculas", List.of(pelicula2));
        verify(mockModel).addAttribute("series", List.of());
        assertEquals("catalogo", viewName);
    }

    @Test
    void testCatalogo_FilterByYearAfter2000() {
        Usuario mockUsuario = new Usuario();
        Perfil mockPerfil = new Perfil();
        mockPerfil.setListaMeGustaPeliculas(List.of());
        mockPerfil.setListaMeGustaSeries(List.of());
        mockUsuario.setPerfiles(List.of(mockPerfil));

        HttpSession mockSession = mock(HttpSession.class);
        Model mockModel = mock(Model.class);

        Pelicula pelicula1 = new Pelicula("Old Movie", Generos.ACCION, 90, 1995, "Desc", "url");
        Pelicula pelicula2 = new Pelicula("New Movie", Generos.ACCION, 90, 2005, "Desc", "url");

        when(mockSession.getAttribute("usuario")).thenReturn(mockUsuario);
        when(deustoStreamService.buscarPeliculasFiltradas(null, null))
                .thenReturn(new java.util.ArrayList<>(List.of(pelicula1, pelicula2)));
        when(deustoStreamService.buscarSeriesFiltradas(null, null)).thenReturn(new java.util.ArrayList<>(List.of()));

        String viewName = webController.catalogo(null, null, null, "1999", mockModel, mockSession);

        verify(mockModel).addAttribute("peliculas", List.of(pelicula1));
        verify(mockModel).addAttribute("series", List.of());
        assertEquals("catalogo", viewName);
    }

    @Test
    void testCatalogo_FilterSeriesByYearBefore2000() {
        Usuario mockUsuario = new Usuario();
        Perfil mockPerfil = new Perfil();
        mockPerfil.setListaMeGustaSeries(List.of());
        mockUsuario.setPerfiles(List.of(mockPerfil));

        HttpSession mockSession = mock(HttpSession.class);
        Model mockModel = mock(Model.class);

        Series serie1 = new Series("Old Series", 1995, "Desc", Generos.DRAMA, null, "url");
        Series serie2 = new Series("New Series", 2005, "Desc", Generos.DRAMA, null, "url");

        when(mockSession.getAttribute("usuario")).thenReturn(mockUsuario);
        when(deustoStreamService.buscarPeliculasFiltradas(null, null)).thenReturn(List.of());
        when(deustoStreamService.buscarSeriesFiltradas(null, null))
                .thenReturn(new java.util.ArrayList<>(List.of(serie1, serie2)));

        String viewName = webController.catalogo(null, null, null, "2000", mockModel, mockSession);

        verify(mockModel).addAttribute("series", List.of(serie2));
        verify(mockModel).addAttribute("peliculas", List.of());
        assertEquals("catalogo", viewName);
    }

    @Test
    void testCatalogo_FilterSeriesByYearAfter2000() {
        Usuario mockUsuario = new Usuario();
        Perfil mockPerfil = new Perfil();
        mockPerfil.setListaMeGustaSeries(List.of());
        mockUsuario.setPerfiles(List.of(mockPerfil));

        HttpSession mockSession = mock(HttpSession.class);
        Model mockModel = mock(Model.class);

        Series serie1 = new Series("Old Series", 1995, "Desc", Generos.DRAMA, null, "url");
        Series serie2 = new Series("New Series", 2005, "Desc", Generos.DRAMA, null, "url");

        when(mockSession.getAttribute("usuario")).thenReturn(mockUsuario);
        when(deustoStreamService.buscarPeliculasFiltradas(null, null)).thenReturn(List.of());
        when(deustoStreamService.buscarSeriesFiltradas(null, null))
                .thenReturn(new java.util.ArrayList<>(List.of(serie1, serie2)));

        String viewName = webController.catalogo(null, null, null, "1999", mockModel, mockSession);

        verify(mockModel).addAttribute("series", List.of(serie1));
        verify(mockModel).addAttribute("peliculas", List.of());
        assertEquals("catalogo", viewName);
    }

    @Test
    void testCatalogo_ValidUserWithFavorites() {
        Usuario mockUsuario = new Usuario();
        Perfil mockPerfil = new Perfil();

        Pelicula favoritePelicula = new Pelicula();
        favoritePelicula.setTitulo("FavoritePelicula");
        Series favoriteSerie = new Series();
        favoriteSerie.setTitulo("FavoriteSerie");

        mockPerfil.setListaMeGustaPeliculas(List.of(favoritePelicula));
        mockPerfil.setListaMeGustaSeries(List.of(favoriteSerie));
        mockUsuario.setPerfiles(List.of(mockPerfil));

        HttpSession mockSession = mock(HttpSession.class);
        Model mockModel = mock(Model.class);

        when(mockSession.getAttribute("usuario")).thenReturn(mockUsuario);
        when(deustoStreamService.buscarPeliculasFiltradas(null, null)).thenReturn(List.of());
        when(deustoStreamService.buscarSeriesFiltradas(null, null)).thenReturn(List.of());

        String viewName = webController.catalogo(null, null, null, null, mockModel, mockSession);

        verify(mockModel).addAttribute("peliculas", List.of());
        verify(mockModel).addAttribute("series", List.of());
        verify(mockModel).addAttribute("peliculasFavoritas", List.of(favoritePelicula));
        verify(mockModel).addAttribute("seriesFavoritas", List.of(favoriteSerie));
        verify(mockModel).addAttribute("usuario", mockUsuario);
        verify(mockModel).addAttribute(eq("generos"), any());
        assertEquals("catalogo", viewName, "The view name should be 'catalogo'");
    }

    @Test
    void testCatalogo_UserWithNoProfiles() {
        Usuario mockUsuario = new Usuario();
        mockUsuario.setPerfiles(List.of());

        HttpSession mockSession = mock(HttpSession.class);
        Model mockModel = mock(Model.class);

        when(mockSession.getAttribute("usuario")).thenReturn(mockUsuario);

        assertThrows(IndexOutOfBoundsException.class, () -> {
            webController.catalogo(null, null, null, null, mockModel, mockSession);
        });
    }

    @Test
    void testVerPeliculas_NoUserInSession() {
        HttpSession mockSession = mock(HttpSession.class);
        Model mockModel = mock(Model.class);

        when(mockSession.getAttribute("usuario")).thenReturn(null);

        assertThrows(NullPointerException.class, () -> {
            webController.verPeliculas(mockModel, mockSession, null, null);
        });
    }

    @Test
    void testVerPeliculas_ValidUserNoFilters() {
        Usuario mockUsuario = new Usuario();
        Perfil mockPerfil = new Perfil();
        mockPerfil.setListaMeGustaPeliculas(List.of());
        mockUsuario.setPerfiles(List.of(mockPerfil));

        HttpSession mockSession = mock(HttpSession.class);
        Model mockModel = mock(Model.class);

        when(mockSession.getAttribute("usuario")).thenReturn(mockUsuario);
        when(deustoStreamService.buscarPeliculasFiltradas(null, null)).thenReturn(List.of());

        String viewName = webController.verPeliculas(mockModel, mockSession, null, null);

        verify(mockModel).addAttribute("peliculas", List.of());
        verify(mockModel).addAttribute("peliculasFavoritas", List.of());
        verify(mockModel).addAttribute("usuario", mockUsuario);
        verify(mockModel).addAttribute(eq("generos"), any());
        assertEquals("catPeliculas", viewName);
    }

    @Test
    void testVerPeliculas_ValidUserWithFilters() {
        Usuario mockUsuario = new Usuario();
        Perfil mockPerfil = new Perfil();
        mockPerfil.setListaMeGustaPeliculas(List.of());
        mockUsuario.setPerfiles(List.of(mockPerfil));

        HttpSession mockSession = mock(HttpSession.class);
        Model mockModel = mock(Model.class);

        Pelicula peliculaFiltrada = new Pelicula();
        peliculaFiltrada.setTitulo("FilteredPelicula");

        when(mockSession.getAttribute("usuario")).thenReturn(mockUsuario);
        when(deustoStreamService.buscarPeliculasFiltradas("test", null)).thenReturn(List.of(peliculaFiltrada));

        String viewName = webController.verPeliculas(mockModel, mockSession, "test", null);

        verify(mockModel).addAttribute("peliculas", List.of(peliculaFiltrada));
        verify(mockModel).addAttribute("peliculasFavoritas", List.of());
        verify(mockModel).addAttribute("usuario", mockUsuario);
        verify(mockModel).addAttribute(eq("generos"), any());
        assertEquals("catPeliculas", viewName);
    }

    @Test
    void testVerSeries_NoUserInSession() {
        HttpSession mockSession = mock(HttpSession.class);
        Model mockModel = mock(Model.class);

        when(mockSession.getAttribute("usuario")).thenReturn(null);

        assertThrows(NullPointerException.class, () -> {
            webController.verSeries(mockModel, mockSession, null, null);
        });
    }

    @Test
    void testVerSeries_ValidUserNoFilters() {
        Usuario mockUsuario = new Usuario();
        Perfil mockPerfil = new Perfil();
        mockPerfil.setListaMeGustaSeries(List.of());
        mockUsuario.setPerfiles(List.of(mockPerfil));

        HttpSession mockSession = mock(HttpSession.class);
        Model mockModel = mock(Model.class);

        when(mockSession.getAttribute("usuario")).thenReturn(mockUsuario);
        when(deustoStreamService.buscarSeriesFiltradas(null, null)).thenReturn(List.of());

        String viewName = webController.verSeries(mockModel, mockSession, null, null);

        verify(mockModel).addAttribute("series", List.of());
        verify(mockModel).addAttribute("seriesFavoritas", List.of());
        verify(mockModel).addAttribute("usuario", mockUsuario);
        verify(mockModel).addAttribute(eq("generos"), any());
        assertEquals("catSeries", viewName);
    }

    @Test
    void testVerSeries_ValidUserWithFilters() {
        Usuario mockUsuario = new Usuario();
        Perfil mockPerfil = new Perfil();
        mockPerfil.setListaMeGustaSeries(List.of());
        mockUsuario.setPerfiles(List.of(mockPerfil));

        HttpSession mockSession = mock(HttpSession.class);
        Model mockModel = mock(Model.class);

        Series serieFiltrada = new Series();
        serieFiltrada.setTitulo("FilteredSeries");

        when(mockSession.getAttribute("usuario")).thenReturn(mockUsuario);
        when(deustoStreamService.buscarSeriesFiltradas("test", null)).thenReturn(List.of(serieFiltrada));

        String viewName = webController.verSeries(mockModel, mockSession, "test", null);

        verify(mockModel).addAttribute("series", List.of(serieFiltrada));
        verify(mockModel).addAttribute("seriesFavoritas", List.of());
        verify(mockModel).addAttribute("usuario", mockUsuario);
        verify(mockModel).addAttribute(eq("generos"), any());
        assertEquals("catSeries", viewName);
    }

    @Test
    void mostrarDetallePelicula_usuario() {
        Pelicula peli = new Pelicula("Matrix", Generos.CIENCIA_FICCION, 136, 1999, "Neo", "img");
        when(deustoStreamService.getPeliculaById(1L)).thenReturn(Optional.of(peli));
        when(deustoStreamService.getPeliculasRelacionadas(1L)).thenReturn(List.of(peli));

        Model model = new ExtendedModelMap();
        String view = webController.mostrarDetallePelicula(1L, model);

        assertEquals("detallePelicula", view);
        assertSame(peli, model.getAttribute("pelicula"));
        assertNotNull(model.getAttribute("relacionadas"));
    }

    @Test
    void mostrarDetallePelicula_admin() {
        Pelicula peli = new Pelicula("Matrix", Generos.CIENCIA_FICCION, 136, 1999, "Neo", "img");
        when(deustoStreamService.getPeliculaById(1L)).thenReturn(Optional.of(peli));
        when(deustoStreamService.getPeliculasRelacionadas(1L)).thenReturn(List.of(peli));

        Model model = new ExtendedModelMap();
        String view = webController.mostrarDetallePeliculaAdmin(1L, model);

        assertEquals("detallePeliculaAdmin", view);
        assertSame(peli, model.getAttribute("pelicula"));
        assertNotNull(model.getAttribute("relacionadas"));
    }

    @Test
    void mostrarDetallePelicula_usuario_peliculaNoEncontrada() {
        when(deustoStreamService.getPeliculaById(1L)).thenReturn(Optional.empty());

        Model model = new ExtendedModelMap();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            webController.mostrarDetallePelicula(1L, model);
        });

        assertEquals("Película no encontrada", exception.getMessage());
    }

    @Test
    void mostrarDetallePelicula_admin_peliculaNoEncontrada() {
        when(deustoStreamService.getPeliculaById(1L)).thenReturn(Optional.empty());

        Model model = new ExtendedModelMap();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            webController.mostrarDetallePeliculaAdmin(1L, model);
        });

        assertEquals("Película no encontrada", exception.getMessage());
    }

    @Test
    void mostrarDetalleSerie_usuario() {
        Series serie = new Series("Lost", 2004, "Desc", Generos.DRAMA, null, "img");
        when(deustoStreamService.getSeriesById(1L)).thenReturn(Optional.of(serie));
        when(deustoStreamService.getSeriesRelacionadas(1L)).thenReturn(List.of(serie));

        Model model = new ExtendedModelMap();
        String view = webController.mostrarDetalleSerieUsuario(1L, model);

        assertEquals("detalleSerie", view);
        assertSame(serie, model.getAttribute("serie"));
        assertNotNull(model.getAttribute("relacionadas"));
    }

    @Test
    void mostrarDetalleSerie_usuario_serieNoEncontrada() {
        when(deustoStreamService.getSeriesById(1L)).thenReturn(Optional.empty());

        Model model = new ExtendedModelMap();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            webController.mostrarDetalleSerieUsuario(1L, model);
        });

        assertEquals("Serie no encontrada", exception.getMessage());
    }

    @Test
    void mostrarDetalleSerie_admin() {
        Series serie = new Series("Lost", 2004, "Desc", Generos.DRAMA, null, "img");
        when(deustoStreamService.getSeriesById(1L)).thenReturn(Optional.of(serie));

        Model model = new ExtendedModelMap();
        String view = webController.mostrarDetalleSerieAdmin(1L, model);

        assertEquals("detalleSerieAdmin", view);
        assertSame(serie, model.getAttribute("serie"));
    }

    @Test
    void mostrarDetalleSerie_admin_serieNoEncontrada() {
        when(deustoStreamService.getSeriesById(1L)).thenReturn(Optional.empty());

        Model model = new ExtendedModelMap();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            webController.mostrarDetalleSerieAdmin(1L, model);
        });

        assertEquals("Serie no encontrada", exception.getMessage());
    }

    /* --------------------------------------------------------------------- */
    /* ------------------- Favoritos y Perfiles ---------------------------- */
    /* --------------------------------------------------------------------- */

    @Test
    void testMostrarGuardados_UserLoggedIn() {
        Usuario mockUsuario = new Usuario();
        Perfil mockPerfil = new Perfil();
        Pelicula pelicula = new Pelicula("Favorite Movie", Generos.ACCION, 120, 2010, "Desc", "url");
        Series serie = new Series("Favorite Series", 2010, "Desc", Generos.DRAMA, null, "url");
        mockPerfil.setListaMeGustaPeliculas(List.of(pelicula));
        mockPerfil.setListaMeGustaSeries(List.of(serie));
        mockUsuario.setPerfiles(List.of(mockPerfil));

        HttpSession mockSession = mock(HttpSession.class);
        Model mockModel = mock(Model.class);

        when(mockSession.getAttribute("usuario")).thenReturn(mockUsuario);
        when(mockSession.getAttribute("perfil")).thenReturn(mockPerfil);

        String viewName = webController.mostrarGuardados(mockModel, mockSession);

        verify(mockModel).addAttribute("peliculasFavoritas", List.of(pelicula));
        verify(mockModel).addAttribute("seriesFavoritas", List.of(serie));
        assertEquals("guardados", viewName);
    }

    @Test
    void testMostrarGuardados_UserNotLoggedIn() {
        HttpSession mockSession = mock(HttpSession.class);
        Model mockModel = mock(Model.class);

        when(mockSession.getAttribute("usuario")).thenReturn(null);

        String viewName = webController.mostrarGuardados(mockModel, mockSession);

        verify(mockModel).addAttribute("error", "Debes iniciar sesión para ver tus películas favoritas.");
        assertEquals("guardados", viewName);
    }

    @Test
    void testMostrarPerfil_UserLoggedIn() {
        Usuario mockUsuario = new Usuario();
        Perfil mockPerfil = new Perfil();
        mockPerfil.setAvatar("avatar1.png");
        mockUsuario.setPerfiles(List.of(mockPerfil));

        HttpSession mockSession = mock(HttpSession.class);
        Model mockModel = mock(Model.class);

        when(mockSession.getAttribute("usuario")).thenReturn(mockUsuario);
        when(mockSession.getAttribute("perfil")).thenReturn(mockPerfil);

        String viewName = webController.mostrarPerfil(mockModel, mockSession);

        verify(mockModel).addAttribute("usuario", mockUsuario);
        verify(mockModel).addAttribute("perfiles", mockUsuario.getPerfiles());
        verify(mockModel).addAttribute("avatar", "avatar1.png");
        assertEquals("perfil", viewName);
    }

    @Test
    void testMostrarPerfil_UserNotLoggedIn() {
        HttpSession mockSession = mock(HttpSession.class);
        Model mockModel = mock(Model.class);

        when(mockSession.getAttribute("usuario")).thenReturn(null);

        String viewName = webController.mostrarPerfil(mockModel, mockSession);

        verify(mockModel).addAttribute("error", "Debes iniciar sesión para ver tu perfil.");
        assertEquals("redirect:/login", viewName);
    }

    @Test
    void testCambiarPerfil_Success() {
        Perfil mockPerfil = new Perfil();
        mockPerfil.setId(1L);

        HttpSession mockSession = mock(HttpSession.class);

        when(deustoStreamService.getPerfilById(1L)).thenReturn(Optional.of(mockPerfil));

        String viewName = webController.cambiarPerfil(1L, mockSession);

        verify(mockSession).setAttribute("perfil", mockPerfil);
        assertEquals("redirect:/catalogo", viewName);
    }

    @Test
    void testCambiarPerfil_NotFound() {
        HttpSession mockSession = mock(HttpSession.class);

        when(deustoStreamService.getPerfilById(1L)).thenReturn(Optional.empty());

        String viewName = webController.cambiarPerfil(1L, mockSession);

        assertEquals("redirect:/acceso-denegado", viewName);
    }

    @Test
    void testProcesarRegistro_Success() {
        Usuario mockUsuario = new Usuario();
        mockUsuario.setCorreo("test@deustostream.es");
        mockUsuario.setContrasenya("password");

        HttpSession mockSession = mock(HttpSession.class);
        Model mockModel = mock(Model.class);

        when(authService.register(mockUsuario)).thenReturn(true);
        when(authService.login(mockUsuario.getCorreo(), mockUsuario.getContrasenya()))
                .thenReturn(Optional.of(mockUsuario));
        when(authService.obtenerRedireccion(mockUsuario)).thenReturn("/catalogo");

        String viewName = webController.procesarRegistro(mockUsuario, mockSession, mockModel);

        verify(mockSession).setAttribute("usuario", mockUsuario);
        assertEquals("redirect:/catalogo", viewName);
    }

    @Test
    void testProcesarRegistro_Failure() {
        Usuario mockUsuario = new Usuario();
        mockUsuario.setCorreo("test@deustostream.es");
        mockUsuario.setContrasenya("password");

        HttpSession mockSession = mock(HttpSession.class);
        Model mockModel = mock(Model.class);

        when(authService.register(mockUsuario)).thenReturn(false);

        String viewName = webController.procesarRegistro(mockUsuario, mockSession, mockModel);

        verify(mockModel).addAttribute("error", "El correo ya está registrado.");
        assertEquals("registro", viewName);
    }

    @Test
    void testProcesarLogin_SuccessWithoutRemember() {
        // datos de prueba
        String correo = "user@example.com";
        String contrasenya = "correct";
        Usuario usuario = new Usuario();
        String destino = "/home";

        when(authService.login(correo, contrasenya))
                .thenReturn(Optional.of(usuario));
        when(authService.obtenerRedireccion(usuario))
                .thenReturn(destino);

        // sin marcar "remember me"
        String viewName = webController.procesarLogin(
                correo,
                contrasenya,
                null,
                response,
                session,
                model);

        // session y redirección
        verify(session).setAttribute("usuario", usuario);
        assertEquals("redirect:" + destino, viewName);
    }

    @Test
    void testProcesarLogin_SuccessWithRemember() {
        String correo = "user2@example.com";
        String contrasenya = "correct2";
        String guardar = "on";
        Usuario usuario = new Usuario();
        String destino = "/dashboard";

        when(authService.login(correo, contrasenya))
                .thenReturn(Optional.of(usuario));
        when(authService.obtenerRedireccion(usuario))
                .thenReturn(destino);

        String viewName = webController.procesarLogin(
                correo,
                contrasenya,
                guardar, // marcamos "remember me"
                response,
                session,
                model);

        verify(session).setAttribute("usuario", usuario);
        assertEquals("redirect:" + destino, viewName);
    }

    @Test
    void testProcesarLogin_Failure() {

        String correo = "test@deustostream.es";
        String contrasenya = "wrongpassword";

        when(authService.login(correo, contrasenya))
                .thenReturn(Optional.empty());

        String viewName = webController.procesarLogin(
                correo,
                contrasenya,
                null,
                response,
                session,
                model);

        verify(model).addAttribute("error", "Correo o contraseña incorrectas");
        assertEquals("login", viewName);
    }

    @Test
    void testVerSuscripcion_UserLoggedIn() {
        Usuario mockUsuario = new Usuario();
        HttpSession mockSession = mock(HttpSession.class);
        Model mockModel = mock(Model.class);

        when(mockSession.getAttribute("usuario")).thenReturn(mockUsuario);

        String viewName = webController.verSuscripcion(mockModel, mockSession);

        verify(mockModel).addAttribute("usuario", mockUsuario);
        assertEquals("suscripcion", viewName);
    }

    @Test
    void testVerSuscripcion_UserNotLoggedIn() {
        HttpSession mockSession = mock(HttpSession.class);
        Model mockModel = mock(Model.class);

        when(mockSession.getAttribute("usuario")).thenReturn(null);

        String viewName = webController.verSuscripcion(mockModel, mockSession);

        assertEquals("redirect:/login", viewName);
    }

    @Test
    void testCerrarSesion() {
        HttpSession mockSession = mock(HttpSession.class);

        String viewName = webController.cerrarSesion(mockSession);

        verify(mockSession).invalidate();
        assertEquals("redirect:/login", viewName);
    }

    @Test
    void testMostrarFormularioLogin_ExistingTokenInSession() {
        // Setup
        String existingToken = "existing-token-123";
        HttpSession mockSession = mock(HttpSession.class);
        when(mockSession.getAttribute("qr-token")).thenReturn(existingToken);
        
        // Mock QrLoginService
        com.example.restapi.service.QrLoginService qrLoginService = mock(com.example.restapi.service.QrLoginService.class);
        
        // Set QrLoginService through reflection
        try {
            java.lang.reflect.Field field = WebController.class.getDeclaredField("qrLoginService");
            field.setAccessible(true);
            field.set(webController, qrLoginService);
        } catch (Exception e) {
            fail("Failed to set qrLoginService: " + e.getMessage());
        }
        
        String viewName = webController.mostrarFormularioLogin(model, mockSession, "", "", false);
        
        // Verify
        verify(mockSession).getAttribute("qr-token");
        verify(mockSession, never()).setAttribute(eq("qr-token"), any());
        verify(qrLoginService, never()).registrarToken(anyString());
        verify(model).addAttribute("qrToken", existingToken);
        assertEquals("login", viewName);
    }

    @Test
    void testMostrarFormularioLogin_CreateNewToken() {
        // Setup
        HttpSession mockSession = mock(HttpSession.class);
        when(mockSession.getAttribute("qr-token")).thenReturn(null);
        
        // Mock QrLoginService
        com.example.restapi.service.QrLoginService qrLoginService = mock(com.example.restapi.service.QrLoginService.class);
        
        // Set QrLoginService through reflection
        try {
            java.lang.reflect.Field field = WebController.class.getDeclaredField("qrLoginService");
            field.setAccessible(true);
            field.set(webController, qrLoginService);
        } catch (Exception e) {
            fail("Failed to set qrLoginService: " + e.getMessage());
        }
        
        String viewName = webController.mostrarFormularioLogin(model, mockSession, "", "", false);
        
        // Verify
        verify(mockSession).getAttribute("qr-token");
        verify(mockSession).setAttribute(eq("qr-token"), anyString());
        verify(qrLoginService).registrarToken(anyString());
        verify(model).addAttribute(eq("qrToken"), anyString());
        assertEquals("login", viewName);
    }

    @Test
    void testMostrarFormularioLogin_WithSavedCredentials() {
        // Setup
        String correoValor = "saved@example.com";
        String contrasenyaValor = "savedPassword123";
        boolean guardarContrasenya = true;
        HttpSession mockSession = mock(HttpSession.class);
        when(mockSession.getAttribute("qr-token")).thenReturn("some-token");
        
        // Mock QrLoginService
        com.example.restapi.service.QrLoginService qrLoginService = mock(com.example.restapi.service.QrLoginService.class);
        
        // Set QrLoginService through reflection
        try {
            java.lang.reflect.Field field = WebController.class.getDeclaredField("qrLoginService");
            field.setAccessible(true);
            field.set(webController, qrLoginService);
        } catch (Exception e) {
            fail("Failed to set qrLoginService: " + e.getMessage());
        }
        
        String viewName = webController.mostrarFormularioLogin(model, mockSession, correoValor, contrasenyaValor, guardarContrasenya);
        
        // Verify
        verify(model).addAttribute("correoValor", correoValor);
        verify(model).addAttribute("contrasenyaValor", contrasenyaValor);
        verify(model).addAttribute("guardarContrasenya", guardarContrasenya);
        verify(model).addAttribute(eq("qrToken"), anyString());
        assertEquals("login", viewName);
    }

    @Test
    void testMostrarFormularioLogin_QrServiceThrowsException() {
        // Setup
        HttpSession mockSession = mock(HttpSession.class);
        when(mockSession.getAttribute("qr-token")).thenReturn(null);
        
        // Mock QrLoginService that throws exception
        com.example.restapi.service.QrLoginService qrLoginService = mock(com.example.restapi.service.QrLoginService.class);
        doThrow(new RuntimeException("QR Service failure")).when(qrLoginService).registrarToken(anyString());
        
        // Set QrLoginService through reflection
        try {
            java.lang.reflect.Field field = WebController.class.getDeclaredField("qrLoginService");
            field.setAccessible(true);
            field.set(webController, qrLoginService);
        } catch (Exception e) {
            fail("Failed to set qrLoginService: " + e.getMessage());
        }
        
        // Test & verify
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            webController.mostrarFormularioLogin(model, mockSession, "", "", false);
        });
        
        assertEquals("QR Service failure", exception.getMessage());
        verify(mockSession).setAttribute(eq("qr-token"), anyString());
    }

    @Test
    void testMostrarFormularioLogin_ModelAttributesCombination() {
        // Setup
        HttpSession mockSession = mock(HttpSession.class);
        when(mockSession.getAttribute("qr-token")).thenReturn("token-xyz");
        
        // Mock QrLoginService
        com.example.restapi.service.QrLoginService qrLoginService = mock(com.example.restapi.service.QrLoginService.class);
        
        // Set QrLoginService through reflection
        try {
            java.lang.reflect.Field field = WebController.class.getDeclaredField("qrLoginService");
            field.setAccessible(true);
            field.set(webController, qrLoginService);
        } catch (Exception e) {
            fail("Failed to set qrLoginService: " + e.getMessage());
        }
        
        // Execute with different combinations
        webController.mostrarFormularioLogin(model, mockSession, "email@test.com", "", true);
        
        // Verify
        verify(model).addAttribute("correoValor", "email@test.com");
        verify(model).addAttribute("contrasenyaValor", "");
        verify(model).addAttribute("guardarContrasenya", true);
        verify(model).addAttribute("qrToken", "token-xyz");
    }


    @Test
    void testMostrarFormularioRegistro() {
        String viewName = webController.mostrarFormularioRegistro();
        assertEquals("registro", viewName);
    }

    @Test
    void testValorarPelicula_UserLoggedIn() {
        Long peliculaId = 10L;
        int puntuacion = 5;
        String comentario = "Muy buena";
        Usuario mockUsuario = new Usuario();
        Perfil mockPerfil = new Perfil();

        HttpSession mockSession = mock(HttpSession.class);
        when(mockSession.getAttribute("usuario")).thenReturn(mockUsuario);
        when(mockSession.getAttribute("perfil")).thenReturn(mockPerfil);

        String viewName = webController.valorarPelicula(peliculaId, puntuacion, comentario, mockSession);

        verify(deustoStreamService).valorarPelicula(peliculaId, mockPerfil, puntuacion, comentario);
        assertEquals("redirect:/pelicula/" + peliculaId, viewName);
    }

    @Test
    void testValorarPelicula_UserNotLoggedIn() {
        Long peliculaId = 10L;
        int puntuacion = 4;
        String comentario = "Entretenida";

        HttpSession mockSession = mock(HttpSession.class);
        when(mockSession.getAttribute("usuario")).thenReturn(null);

        String viewName = webController.valorarPelicula(peliculaId, puntuacion, comentario, mockSession);

        verify(deustoStreamService, never()).valorarPelicula(anyLong(), any(), anyInt(), anyString());
        assertEquals("redirect:/login", viewName);
    }

    @Test
    void testValorarSerie_UserLoggedIn() {
        Long serieId = 20L;
        int puntuacion = 3;
        String comentario = "Regular";
        Usuario mockUsuario = new Usuario();
        Perfil mockPerfil = new Perfil();

        HttpSession mockSession = mock(HttpSession.class);
        when(mockSession.getAttribute("usuario")).thenReturn(mockUsuario);
        when(mockSession.getAttribute("perfil")).thenReturn(mockPerfil);

        String viewName = webController.valorarSerie(serieId, puntuacion, comentario, mockSession);

        verify(deustoStreamService).valorarSerie(serieId, mockPerfil, puntuacion, comentario);
        assertEquals("redirect:/serie/" + serieId, viewName);
    }

    @Test
    void testValorarSerie_UserNotLoggedIn() {
        Long serieId = 20L;
        int puntuacion = 2;
        String comentario = "No me gustó";

        HttpSession mockSession = mock(HttpSession.class);
        when(mockSession.getAttribute("usuario")).thenReturn(null);

        String viewName = webController.valorarSerie(serieId, puntuacion, comentario, mockSession);

        verify(deustoStreamService, never()).valorarSerie(anyLong(), any(), anyInt(), anyString());
        assertEquals("redirect:/login", viewName);
    }

     @Test
    void mostrarDetalleUsuario_usuario() {
        Usuario usuario = new Usuario();
        when(deustoStreamService.getUsuarioById(1L))
            .thenReturn(Optional.of(usuario));

        Model model = new ExtendedModelMap();
        HttpSession session = mock(HttpSession.class);
        String view = webController.mostrarDetalleUsuario(1L, model, session);

        assertEquals("detalleUsuario", view);
        assertSame(usuario, model.getAttribute("usuario"));
    }

    @Test
    void mostrarDetalleUsuario_usuario_usuarioNoEncontrado() {
        when(deustoStreamService.getUsuarioById(1L))
            .thenReturn(Optional.empty());

        Model model = new ExtendedModelMap();
        HttpSession session = mock(HttpSession.class);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            webController.mostrarDetalleUsuario(1L, model, session);
        });
        assertEquals("Usuario no encontrado: 1", ex.getMessage());
    }

    @Test
    void mostrarDetalleUsuario_admin() {
        Usuario usuario = new Usuario();
        when(deustoStreamService.getUsuarioById(1L))
            .thenReturn(Optional.of(usuario));

        Model model = new ExtendedModelMap();
        String view = webController.mostrarDetalleUsuarioAdmin(1L, model);

        assertEquals("detalleUsuarioAdmin", view);
        assertSame(usuario, model.getAttribute("usuario"));
    }

    @Test
    void mostrarDetalleUsuario_admin_usuarioNoEncontrado() {
        when(deustoStreamService.getUsuarioById(1L))
            .thenReturn(Optional.empty());

        Model model = new ExtendedModelMap();

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            webController.mostrarDetalleUsuarioAdmin(1L, model);
        });
        assertEquals("Usuario no encontrado: 1", ex.getMessage());
    }

}
