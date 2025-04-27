package com.example.restapi.unit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
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
            webController.catalogo(null, null,null,null, mockModel, mockSession);
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

        String viewName = webController.catalogo(null, null,null,null, mockModel, mockSession);

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

        String viewName = webController.catalogo("test", null,null,null, mockModel, mockSession);

        verify(mockModel).addAttribute("peliculas", List.of(pelicula1));
        verify(mockModel).addAttribute("series", List.of(serie1));
        verify(mockModel).addAttribute("peliculasFavoritas", List.of());
        verify(mockModel).addAttribute("seriesFavoritas", List.of());
        verify(mockModel).addAttribute("usuario", mockUsuario);
        verify(mockModel).addAttribute(eq("generos"), any());
        assertEquals("catalogo", viewName, "The view name should be 'catalogo'");
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

        String viewName = webController.catalogo(null, null,null,null, mockModel, mockSession);

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
            webController.catalogo(null, null,null,null, mockModel, mockSession);
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
    void mostrarDetalleSerie_admin() {
        Series serie = new Series("Lost", 2004, "Desc", Generos.DRAMA, null, "img");
        when(deustoStreamService.getSeriesById(1L)).thenReturn(Optional.of(serie));

        Model model = new ExtendedModelMap();
        String view = webController.mostrarDetalleSerieAdmin(1L, model);

        assertEquals("detalleSerieAdmin", view);
        assertSame(serie, model.getAttribute("serie"));
    }
}
