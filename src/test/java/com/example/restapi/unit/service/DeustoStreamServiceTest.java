package com.example.restapi.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.restapi.model.Capitulo;
import com.example.restapi.model.Generos;
import com.example.restapi.model.Pelicula;
import com.example.restapi.model.Perfil;
import com.example.restapi.model.Series;
import com.example.restapi.model.Usuario;
import com.example.restapi.model.Valoracion;
import com.example.restapi.repository.CapituloRepository;
import com.example.restapi.repository.PeliculaRepository;
import com.example.restapi.repository.PerfilRepository;
import com.example.restapi.repository.SerieRepository;
import com.example.restapi.repository.UsuarioRepository;
import com.example.restapi.repository.ValoracionRepository;
import com.example.restapi.service.DeustoStreamService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import static org.mockito.Mockito.*;

public class DeustoStreamServiceTest {
    @Mock
    private PeliculaRepository peliculaRepository;

    @Mock
    private SerieRepository seriesRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private CapituloRepository capituloRepository;

    @Mock
    private PerfilRepository perfilRepository;

    @Mock
    private ValoracionRepository valoracionRepository;

    @Mock
    private HttpSession session;

    @InjectMocks
    private DeustoStreamService deustoStreamService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsuarios_ReturnsList() {
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        usuario1.setNombre("Ana");
        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario2.setNombre("Luis");
        List<Usuario> usuarios = Arrays.asList(usuario1, usuario2);

        when(usuarioRepository.findAll()).thenReturn(usuarios);

        List<Usuario> result = deustoStreamService.getAllUsuarios();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Ana", result.get(0).getNombre());
        assertEquals("Luis", result.get(1).getNombre());
    }

    @Test
    void testGetAllUsuarios_EmptyList() {
        when(usuarioRepository.findAll()).thenReturn(new ArrayList<>());

        List<Usuario> result = deustoStreamService.getAllUsuarios();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetUsuarioById_Found() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Ana");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Optional<Usuario> result = deustoStreamService.getUsuarioById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("Ana", result.get().getNombre());
    }

    @Test
    void testGetUsuarioById_NotFound() {
        when(usuarioRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<Usuario> result = deustoStreamService.getUsuarioById(2L);

        assertTrue(result.isEmpty());
    }

    @Test
    void testCreateUsuario_Success() {
        Usuario usuario = new Usuario();
        usuario.setNombre("Luis");

        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario result = deustoStreamService.createUsuario(usuario);

        assertNotNull(result);
        assertEquals("Luis", result.getNombre());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void testUpdateUsuario_Success() {
        Usuario existing = new Usuario();
        existing.setId(1L);
        existing.setNombre("Ana");
        existing.setApellido("Viejo");
        existing.setCorreo("viejo@mail.com");
        existing.setContrasenya("oldpass");
        existing.setTipoSuscripcion("BASICO");

        Usuario detalles = new Usuario();
        detalles.setNombre("Luis");
        detalles.setApellido("Nuevo");
        detalles.setCorreo("nuevo@mail.com");
        detalles.setContrasenya("newpass");
        detalles.setTipoSuscripcion("PREMIUM");

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));

        Usuario result = deustoStreamService.updateUsuario(1L, detalles);

        assertEquals("Luis", result.getNombre());
        assertEquals("Nuevo", result.getApellido());
        assertEquals("nuevo@mail.com", result.getCorreo());
        assertEquals("newpass", result.getContrasenya());
        assertEquals("PREMIUM", result.getTipoSuscripcion());
        verify(usuarioRepository).save(existing);
    }

    @Test
    void testUpdateUsuario_NotFound() {
        Usuario detalles = new Usuario();
        detalles.setNombre("Luis");
        detalles.setApellido("Nuevo");
        detalles.setCorreo("nuevo@mail.com");
        detalles.setContrasenya("newpass");
        detalles.setTipoSuscripcion("PREMIUM");

        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            deustoStreamService.updateUsuario(99L, detalles);
        });
        assertEquals("Usuario not found with id: 99", ex.getMessage());
    }

    @Test
    void testDeleteUsuario_Successfully() {
        // Mock usuarioRepository.existsById to return true
        when(usuarioRepository.existsById(1L)).thenReturn(true);

        // Call the method under test
        deustoStreamService.deleteUsuario(1L);

        // Verify that deleteById was called
        verify(usuarioRepository).deleteById(1L);
    }

    @Test
    void testDeleteUsuario_NotFound_ThrowsException() {
        // Mock usuarioRepository.existsById to return false
        when(usuarioRepository.existsById(2L)).thenReturn(false);

        // Assert that the RuntimeException is thrown with the correct message
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            deustoStreamService.deleteUsuario(2L);
        });

        assertEquals("Usuario not found with id: 2", exception.getMessage());
        verify(usuarioRepository, never()).deleteById(anyLong());
    }

    @Test
    void testGetPeliculaById() {
        Pelicula pelicula = new Pelicula("Inception", Generos.ACCION, 148, 2010, "Un sueño dentro de otro", "url");
        pelicula.setId(1L);
        // Mock the behavior of the repository to return the pelicula when findById is
        // called
        when(peliculaRepository.findById(1L)).thenReturn(Optional.of(pelicula));

        Optional<Pelicula> optionalResult = deustoStreamService.getPeliculaById(1L);
        assertNotNull(optionalResult);
        assertTrue(optionalResult.isPresent());
        Pelicula result = optionalResult.get();
        assertEquals("Inception", result.getTitulo());
        assertEquals("Inception", result.getTitulo());
        assertEquals(Generos.ACCION, result.getGenero());
        assertEquals(148, result.getDuracion());
        assertEquals(2010, result.getAnio());
        assertEquals("Un sueño dentro de otro", result.getSinopsis());
        assertEquals("url", result.getImagenUrl());
    }

    @Test
    void testGetPeliculaByIdNotFound() {
        // Mock the behavior of the repository to return an empty Optional when findById
        // is called
        when(peliculaRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Pelicula> optionalResult = deustoStreamService.getPeliculaById(1L);
        assertNotNull(optionalResult);
        assertTrue(optionalResult.isEmpty());
    }

    @Test
    void testGetAllPeliculas() {
        // Mock the behavior of the repository to return a list of peliculas when
        // findAll is called
        Pelicula pelicula1 = new Pelicula("Inception", Generos.ACCION, 148, 2010, "Un sueño dentro de otro", "url");
        Pelicula pelicula2 = new Pelicula("The Matrix", Generos.ACCION, 136, 1999, "Un mundo virtual", "url2");
        List<Pelicula> peliculas = Arrays.asList(pelicula1, pelicula2);

        when(peliculaRepository.findAll()).thenReturn(peliculas);

        List<Pelicula> result = deustoStreamService.getAllPeliculas();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Inception", result.get(0).getTitulo());
        assertEquals("The Matrix", result.get(1).getTitulo());
    }

    @Test
    void testCreatePelicula() {
        Pelicula pelicula = new Pelicula("Inception", Generos.ACCION, 148, 2010, "Un sueño dentro de otro", "url");
        // Mock the behavior of the repository to return the pelicula when save is
        // called
        when(peliculaRepository.save(pelicula)).thenReturn(pelicula);

        Pelicula result = deustoStreamService.createPelicula(pelicula);

        assertNotNull(result);
        assertEquals("Inception", result.getTitulo());
        assertEquals(Generos.ACCION, result.getGenero());
        assertEquals(148, result.getDuracion());
        assertEquals(2010, result.getAnio());
        assertEquals("Un sueño dentro de otro", result.getSinopsis());
        assertEquals("url", result.getImagenUrl());
    }

    @Test
    void testCreatePelicula_NullPelicula() {
        assertThrows(RuntimeException.class, () -> {
            deustoStreamService.createPelicula(null);
        });
    }

    @Test
    void testCreatePelicula_NullTitulo() {
        Pelicula pelicula = new Pelicula(null, Generos.ACCION, 148, 2010, "Sinopsis", "url");
        assertThrows(RuntimeException.class, () -> {
            deustoStreamService.createPelicula(pelicula);
        });
    }

    @Test
    void testCreatePelicula_InvalidAnio() {
        Pelicula pelicula = new Pelicula("Titulo", Generos.ACCION, 148, -1, "Sinopsis", "url");
        assertThrows(RuntimeException.class, () -> {
            deustoStreamService.createPelicula(pelicula);
        });
    }

    @Test
    void testUpdatePelicula() {
        Pelicula original = new Pelicula("Old Title", Generos.ACCION, 100, 2000, "Vieja sinopsis", "oldurl");
        original.setId(1L);
        Pelicula updated = new Pelicula("New Title", Generos.DRAMA, 120, 2023, "Nueva sinopsis", "newurl");

        when(peliculaRepository.findById(1L)).thenReturn(Optional.of(original));
        when(peliculaRepository.save(any(Pelicula.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Pelicula result = deustoStreamService.updatePelicula(1L, updated);

        assertEquals("New Title", result.getTitulo());
        assertEquals(Generos.DRAMA, result.getGenero());
        verify(peliculaRepository).save(original);
    }

    @Test
    void testUpdatePelicula_NotFound() {
        Pelicula updated = new Pelicula("New Title", Generos.DRAMA, 120, 2023, "Nueva sinopsis", "newurl");

        when(peliculaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            deustoStreamService.updatePelicula(99L, updated);
        });
    }

    @Test
    void testDeletePelicula_Successfully() {
        // Create a Pelicula instance
        Pelicula pelicula = new Pelicula("Test Pelicula", Generos.CIENCIA_FICCION, 120, 2022, "Test sinopsis",
                "testurl");
        pelicula.setId(1L);

        // Create two Perfil instances: one that has the pelicula and one that does not
        Perfil perfilWithPelicula = new Perfil("User1", "avatar1.png");
        perfilWithPelicula.setId(100L);
        List<Pelicula> peliculasFavoritas = new ArrayList<>();
        peliculasFavoritas.add(pelicula);
        perfilWithPelicula.setListaMeGustaPeliculas(peliculasFavoritas);

        Perfil perfilWithoutPelicula = new Perfil("User2", "avatar2.png");
        perfilWithoutPelicula.setId(101L);
        perfilWithoutPelicula.setListaMeGustaPeliculas(new ArrayList<>());

        List<Perfil> perfiles = Arrays.asList(perfilWithPelicula, perfilWithoutPelicula);

        // Mock repository methods
        when(peliculaRepository.findById(1L)).thenReturn(Optional.of(pelicula));
        when(perfilRepository.findAll()).thenReturn(perfiles);

        // Call the method under test
        deustoStreamService.deletePelicula(1L);

        // Verify that the pelicula was removed from the perfil that contained it
        assertFalse(perfilWithPelicula.getListaMeGustaPeliculas().contains(pelicula));
        verify(perfilRepository).save(perfilWithPelicula);

        // Verify that the pelicula repository's delete method was called
        verify(peliculaRepository).delete(pelicula);

        // Verify that perfilWithoutPelicula was not unnecessarily saved
        verify(perfilRepository, never()).save(perfilWithoutPelicula);
    }

    @Test
    void testDeletePeliculaNotFound() {
        // Mock the behavior of the repository to return an empty Optional when findById
        // is called
        when(peliculaRepository.findById(1L)).thenReturn(Optional.empty());

        // Assert that the RuntimeException is thrown with the correct message
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            deustoStreamService.deletePelicula(1L);
        });

        assertEquals("Pelicula not found with id: 1", exception.getMessage());
    }

    @Test
    void testBuscarPeliculasFiltradas_TituloYGenero() {
        Pelicula pelicula1 = new Pelicula("Inception", Generos.ACCION, 148, 2010, "Un sueño dentro de otro", "url");
        Pelicula pelicula2 = new Pelicula("Inception 2", Generos.ACCION, 150, 2012, "Secuela", "url2");
        List<Pelicula> peliculas = Arrays.asList(pelicula1, pelicula2);

        when(peliculaRepository.findByTituloContainingIgnoreCaseAndGenero("Inception", Generos.ACCION))
                .thenReturn(peliculas);

        List<Pelicula> result = deustoStreamService.buscarPeliculasFiltradas("Inception", Generos.ACCION);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Inception", result.get(0).getTitulo());
        assertEquals("Inception 2", result.get(1).getTitulo());
    }

    @Test
    void testBuscarPeliculasFiltradas_SoloTitulo() {
        Pelicula pelicula1 = new Pelicula("Inception", Generos.ACCION, 148, 2010, "Un sueño dentro de otro", "url");
        List<Pelicula> peliculas = Arrays.asList(pelicula1);

        when(peliculaRepository.findByTituloContainingIgnoreCase("Inception")).thenReturn(peliculas);

        List<Pelicula> result = deustoStreamService.buscarPeliculasFiltradas("Inception", null);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Inception", result.get(0).getTitulo());
    }

    @Test
    void testBuscarPeliculasFiltradas_SoloGenero() {
        Pelicula pelicula1 = new Pelicula("Inception", Generos.ACCION, 148, 2010, "Un sueño dentro de otro", "url");
        Pelicula pelicula2 = new Pelicula("The Matrix", Generos.ACCION, 136, 1999, "Un mundo virtual", "url2");
        List<Pelicula> peliculas = Arrays.asList(pelicula1, pelicula2);

        when(peliculaRepository.findByGenero(Generos.ACCION)).thenReturn(peliculas);

        List<Pelicula> result = deustoStreamService.buscarPeliculasFiltradas(null, Generos.ACCION);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Inception", result.get(0).getTitulo());
        assertEquals("The Matrix", result.get(1).getTitulo());
    }

    @Test
    void testBuscarPeliculasFiltradas_SinFiltros() {
        Pelicula pelicula1 = new Pelicula("Inception", Generos.ACCION, 148, 2010, "Un sueño dentro de otro", "url");
        Pelicula pelicula2 = new Pelicula("The Matrix", Generos.ACCION, 136, 1999, "Un mundo virtual", "url2");
        List<Pelicula> peliculas = Arrays.asList(pelicula1, pelicula2);

        when(peliculaRepository.findAll()).thenReturn(peliculas);

        List<Pelicula> result = deustoStreamService.buscarPeliculasFiltradas(null, null);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Inception", result.get(0).getTitulo());
        assertEquals("The Matrix", result.get(1).getTitulo());
    }

    @Test
    void testGetAllSeries() {
        // Mock the behavior of the repository to return a list of series when findAll
        // is called
        Series serie1 = new Series("Breaking Bad", 2008,
                "Un profesor de química se convierte en fabricante de metanfetaminas", Generos.ACCION,
                new ArrayList<>(), "url3");
        Series serie2 = new Series("Game of Thrones", 2011, "Luchas por el trono de hierro en un mundo de fantasía",
                Generos.FANTASIA, new ArrayList<>(), "url4");
        List<Series> series = Arrays.asList(serie1, serie2);

        when(seriesRepository.findAll()).thenReturn(series);

        List<Series> result = deustoStreamService.getAllSeries();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Breaking Bad", result.get(0).getTitulo());
        assertEquals("Game of Thrones", result.get(1).getTitulo());
    }

    @Test
    void testGetSerieById() {
        Series serie = new Series("Breaking Bad", 2008,
                "Un profesor de química se convierte en fabricante de metanfetaminas", Generos.ACCION,
                new ArrayList<>(), "url3");
        serie.setId(1L);
        // Mock the behavior of the repository to return the serie when findById is
        // called
        when(seriesRepository.findById(1L)).thenReturn(Optional.of(serie));

        Optional<Series> optionalResult = deustoStreamService.getSeriesById(1L);
        assertNotNull(optionalResult);
        assertTrue(optionalResult.isPresent());
        Series result = optionalResult.get();
        assertEquals("Breaking Bad", result.getTitulo());
        assertEquals(2008, result.getAnio());
        assertEquals("Un profesor de química se convierte en fabricante de metanfetaminas", result.getDescripcion());
        assertEquals(Generos.ACCION, result.getGenero());
    }

    @Test
    void testGetSerieByIdNotFound() {
        // Mock the behavior of the repository to return an empty Optional when findById
        // is called
        when(seriesRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Series> optionalResult = deustoStreamService.getSeriesById(1L);
        assertNotNull(optionalResult);
        assertTrue(optionalResult.isEmpty());
    }

    @Test
    void testCreateSeries() {
        Series serie = new Series("Breaking Bad", 2008,
                "Un profesor de química se convierte en fabricante de metanfetaminas", Generos.ACCION,
                new ArrayList<>(), "url3");
        // Mock the behavior of the repository to return the serie when save is called
        when(seriesRepository.save(serie)).thenReturn(serie);

        Series result = deustoStreamService.createSeries(serie);

        assertNotNull(result);
        assertEquals("Breaking Bad", result.getTitulo());
        assertEquals(2008, result.getAnio());
        assertEquals("Un profesor de química se convierte en fabricante de metanfetaminas", result.getDescripcion());
        assertEquals(Generos.ACCION, result.getGenero());
    }

    @Test
    void testUpdateSeries() {
        Series original = new Series("Old Title", 2000, "Vieja sinopsis", Generos.ACCION, new ArrayList<>(), "oldurl");
        original.setId(1L);
        Series updated = new Series("New Title", 2023, "Nueva sinopsis", Generos.DRAMA, new ArrayList<>(), "newurl");

        when(seriesRepository.findById(1L)).thenReturn(Optional.of(original));
        when(seriesRepository.save(any(Series.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Series result = deustoStreamService.updateSeries(1L, updated);

        assertEquals("New Title", result.getTitulo());
        assertEquals(2023, result.getAnio());
        verify(seriesRepository).save(original);
    }

    @Test
    void testUpdateSeries_NotFound() {
        Series updated = new Series("New Title", 2023, "Nueva sinopsis", Generos.DRAMA, new ArrayList<>(), "newurl");

        when(seriesRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            deustoStreamService.updateSeries(99L, updated);
        });
    }

    @Test
    void testDeleteSeries_Successfully() {
        // Create a Series instance
        Series series = new Series("Test Series", 2022, "Test Description", Generos.DRAMA, new ArrayList<>(),
                "testurl");
        series.setId(1L);

        // Create two Perfil instances: one that includes the series and one that does
        // not
        Perfil perfilWithSeries = new Perfil("User1", "avatar1.png");
        perfilWithSeries.setId(10L);
        List<Series> favoritosConSerie = new ArrayList<>();
        favoritosConSerie.add(series);
        perfilWithSeries.setListaMeGustaSeries(favoritosConSerie);

        Perfil perfilWithoutSeries = new Perfil("User2", "avatar2.png");
        perfilWithoutSeries.setId(11L);
        perfilWithoutSeries.setListaMeGustaSeries(new ArrayList<>());

        List<Perfil> perfiles = Arrays.asList(perfilWithSeries, perfilWithoutSeries);

        // Mock the repository behavior
        when(seriesRepository.findById(1L)).thenReturn(Optional.of(series));
        when(perfilRepository.findAll()).thenReturn(perfiles);

        // Call the method under test
        deustoStreamService.deleteSeries(1L);

        // Verify that for the profile containing the series, the series was removed and
        // saved
        assertFalse(perfilWithSeries.getListaMeGustaSeries().contains(series));
        verify(perfilRepository).save(perfilWithSeries);

        // Verify that the profile without the series was not modified
        verify(perfilRepository, never()).save(perfilWithoutSeries);

        // Verify that the series was deleted from the repository
        verify(seriesRepository).delete(series);
    }

    @Test
    void testDeleteSeriesNotFound() {
        // Mock the behavior of the repository to return an empty Optional when findById
        // is called
        when(seriesRepository.findById(1L)).thenReturn(Optional.empty());

        // Assert that the RuntimeException is thrown with the correct message
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            deustoStreamService.deleteSeries(1L);
        });

        assertEquals("Series not found with id: 1", exception.getMessage());
    }

    @Test
    void testBuscarSeriesFiltradas_TituloYGenero() {
        Series serie1 = new Series("Breaking Bad", 2008,
                "Un profesor de química se convierte en fabricante de metanfetaminas", Generos.ACCION,
                new ArrayList<>(), "url1");
        Series serie2 = new Series("Breaking Bad 2", 2010, "Secuela", Generos.ACCION, new ArrayList<>(), "url2");
        List<Series> series = Arrays.asList(serie1, serie2);

        when(seriesRepository.findByTituloContainingIgnoreCaseAndGenero("Breaking Bad", Generos.ACCION))
                .thenReturn(series);

        List<Series> result = deustoStreamService.buscarSeriesFiltradas("Breaking Bad", Generos.ACCION);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Breaking Bad", result.get(0).getTitulo());
        assertEquals("Breaking Bad 2", result.get(1).getTitulo());
    }

    @Test
    void testBuscarSeriesFiltradas_SoloTitulo() {
        Series serie1 = new Series("Breaking Bad", 2008,
                "Un profesor de química se convierte en fabricante de metanfetaminas", Generos.ACCION,
                new ArrayList<>(), "url1");
        List<Series> series = Arrays.asList(serie1);

        when(seriesRepository.findByTituloContainingIgnoreCase("Breaking Bad")).thenReturn(series);

        List<Series> result = deustoStreamService.buscarSeriesFiltradas("Breaking Bad", null);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Breaking Bad", result.get(0).getTitulo());
    }

    @Test
    void testBuscarSeriesFiltradas_SoloGenero() {
        Series serie1 = new Series("Breaking Bad", 2008,
                "Un profesor de química se convierte en fabricante de metanfetaminas", Generos.ACCION,
                new ArrayList<>(), "url1");
        Series serie2 = new Series("Game of Thrones", 2011, "Luchas por el trono de hierro en un mundo de fantasía",
                Generos.ACCION, new ArrayList<>(), "url2");
        List<Series> series = Arrays.asList(serie1, serie2);

        when(seriesRepository.findByGenero(Generos.ACCION)).thenReturn(series);

        List<Series> result = deustoStreamService.buscarSeriesFiltradas(null, Generos.ACCION);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Breaking Bad", result.get(0).getTitulo());
        assertEquals("Game of Thrones", result.get(1).getTitulo());
    }

    @Test
    void testBuscarSeriesFiltradas_SinFiltros() {
        Series serie1 = new Series("Breaking Bad", 2008,
                "Un profesor de química se convierte en fabricante de metanfetaminas", Generos.ACCION,
                new ArrayList<>(), "url1");
        Series serie2 = new Series("Game of Thrones", 2011, "Luchas por el trono de hierro en un mundo de fantasía",
                Generos.FANTASIA, new ArrayList<>(), "url2");
        List<Series> series = Arrays.asList(serie1, serie2);

        when(seriesRepository.findAll()).thenReturn(series);

        List<Series> result = deustoStreamService.buscarSeriesFiltradas(null, null);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Breaking Bad", result.get(0).getTitulo());
        assertEquals("Game of Thrones", result.get(1).getTitulo());
    }

    @Test
    void testGuardarUsuario_ReturnsSavedUsuario() {
        Usuario usuario = new Usuario("Luis", "angel", "luis@mail.com", "1234");
        usuario.setId(2L);

        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario result = deustoStreamService.guardarUsuario(usuario);

        assertNotNull(result);
        assertEquals("Luis", result.getNombre());
        assertEquals("luis@mail.com", result.getCorreo());
        assertEquals("1234", result.getContrasenya());
        assertEquals(2L, result.getId());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void testGuardarUsuario_NullUsuario_ThrowsException() {
        when(usuarioRepository.save(null)).thenThrow(new RuntimeException("Usuario is null"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            deustoStreamService.guardarUsuario(null);
        });

        assertEquals("Usuario is null", exception.getMessage());
        verify(usuarioRepository).save(null);
    }

    @Test
    void testGetAllCapitulos() {
        Capitulo c1 = new Capitulo("Cap 1", 30);
        Capitulo c2 = new Capitulo("Cap 2", 40);
        List<Capitulo> lista = Arrays.asList(c1, c2);

        when(capituloRepository.findAll()).thenReturn(lista);

        List<Capitulo> result = deustoStreamService.getAllCapitulos();

        assertEquals(2, result.size());
        assertEquals("Cap 1", result.get(0).getTitulo());
    }

    @Test
    void testGetCapituloById() {
        Capitulo capitulo = new Capitulo("Cap especial", 50);
        capitulo.setId(1L);

        when(capituloRepository.findById(1L)).thenReturn(Optional.of(capitulo));

        Optional<Capitulo> result = deustoStreamService.getCapituloById(1L);

        assertTrue(result.isPresent());
        assertEquals("Cap especial", result.get().getTitulo());
    }

    @Test
    void testCreateCapituloWithSerie() {
        Series serie = new Series();
        serie.setId(10L);

        Capitulo capitulo = new Capitulo("Capitulo con serie", 45);
        capitulo.setSerie(serie);

        when(capituloRepository.save(capitulo)).thenReturn(capitulo);
        when(capituloRepository.countBySerieId(10L)).thenReturn(1);

        Capitulo result = deustoStreamService.createCapitulo(capitulo);

        assertNotNull(result);
        verify(seriesRepository).save(serie);
        assertEquals("Capitulo con serie", result.getTitulo());
    }

    @Test
    void testUpdateCapitulo() {
        Capitulo existente = new Capitulo("Viejo título", 25);
        existente.setId(1L);

        Capitulo actualizado = new Capitulo("Nuevo título", 40);

        when(capituloRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(capituloRepository.save(any(Capitulo.class))).thenAnswer(inv -> inv.getArgument(0));

        Capitulo result = deustoStreamService.updateCapitulo(1L, actualizado);

        assertEquals("Nuevo título", result.getTitulo());
        assertEquals(40, result.getDuracion());
    }

    @Test
    void testUpdateCapituloNotFound() {
        Capitulo cap = new Capitulo("No existe", 60);

        when(capituloRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            deustoStreamService.updateCapitulo(999L, cap);
        });
    }

    @Test
    void testDeleteCapituloExists() {
        when(capituloRepository.existsById(5L)).thenReturn(true);

        deustoStreamService.deleteCapitulo(5L);

        verify(capituloRepository).deleteById(5L);
    }

    @Test
    void testDeleteCapituloNotFound() {
        when(capituloRepository.existsById(100L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> {
            deustoStreamService.deleteCapitulo(100L);
        });
    }

    @Test
    void getPeliculasRelacionadas_devuelveLista() {
        Pelicula peli = new Pelicula("Matrix", Generos.CIENCIA_FICCION, 136, 1999, "Neo", "img");
        peli.setId(1L);

        when(peliculaRepository.findById(1L)).thenReturn(Optional.of(peli));
        when(peliculaRepository.findTop6ByGeneroAndIdNot(peli.getGenero(), peli.getId()))
                .thenReturn(List.of(peli));

        List<Pelicula> result = deustoStreamService.getPeliculasRelacionadas(1L);

        assertEquals(1, result.size());
    }

    @Test
    void getPeliculasRelacionadas_noExisteDevuelveVacio() {
        when(peliculaRepository.findById(99L)).thenReturn(Optional.empty());
        assertTrue(deustoStreamService.getPeliculasRelacionadas(99L).isEmpty());
    }

    @Test
    void getSeriesRelacionadas_devuelveLista() {
        Series serie = new Series("Lost", 2004, "Desc", Generos.DRAMA, null, "img");
        serie.setId(1L);

        when(seriesRepository.findById(1L)).thenReturn(Optional.of(serie));
        when(seriesRepository.findTop6ByGeneroAndIdNot(serie.getGenero(), serie.getId()))
                .thenReturn(List.of(serie));

        List<Series> result = deustoStreamService.getSeriesRelacionadas(1L);

        assertEquals(1, result.size());
    }

    @Test
    void getSeriesRelacionadas_noExisteDevuelveVacio() {
        when(seriesRepository.findById(99L)).thenReturn(Optional.empty());
        assertTrue(deustoStreamService.getSeriesRelacionadas(99L).isEmpty());
    }

    /* --------------------------------------------------------------------- */
    /* ------------------- Favoritos y Perfiles ---------------------------- */
    /* --------------------------------------------------------------------- */

    @Test
    void testAddPeliculaToFavoritos_AddsSuccessfully() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        Perfil perfil = new Perfil();
        perfil.setId(1L);
        perfil.setListaMeGustaPeliculas(new ArrayList<>());
        usuario.setPerfiles(List.of(perfil));

        Pelicula pelicula = new Pelicula("Inception", Generos.ACCION, 148, 2010, "Sueños", "url");
        pelicula.setId(1L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(peliculaRepository.findById(1L)).thenReturn(Optional.of(pelicula));
        when(session.getAttribute("perfil")).thenReturn(perfil);

        deustoStreamService.addPeliculaToFavoritos(1L, 1L, session);

        assertTrue(perfil.getListaMeGustaPeliculas().contains(pelicula));
        verify(perfilRepository).save(perfil);
    }

    @Test
    void testAddPeliculaToFavoritos_RemovesIfAlreadyExists() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        Perfil perfil = new Perfil();
        perfil.setId(1L);
        Pelicula pelicula = new Pelicula("Inception", Generos.ACCION, 148, 2010, "Sueños", "url");
        pelicula.setId(1L);
        perfil.setListaMeGustaPeliculas(new ArrayList<>(List.of(pelicula)));
        usuario.setPerfiles(List.of(perfil));

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(peliculaRepository.findById(1L)).thenReturn(Optional.of(pelicula));
        when(session.getAttribute("perfil")).thenReturn(perfil);

        deustoStreamService.addPeliculaToFavoritos(1L, 1L, session);

        assertFalse(perfil.getListaMeGustaPeliculas().contains(pelicula));
        verify(perfilRepository).save(perfil);
    }

    @Test
    void testAddPeliculaToFavoritos_UserNotFound_ThrowsException() {
        Pelicula pelicula = new Pelicula("Inception", Generos.ACCION, 148, 2010, "Sueños", "url");
        pelicula.setId(1L);
        Long usuarioId = 1L;
        Long peliculaId = 1L;

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.empty());
        when(peliculaRepository.findById(peliculaId)).thenReturn(Optional.of(pelicula));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            deustoStreamService.addPeliculaToFavoritos(usuarioId, peliculaId, session);
        });
        assertEquals("Usuario with id: " + usuarioId + " or Pelicula with id: " + peliculaId + " not found", exception.getMessage());
    }

    @Test
    void testAddPeliculaToFavoritos_PeliculaNotFound_ThrowsException() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        Perfil perfil = new Perfil();
        perfil.setId(1L);
        usuario.setPerfiles(List.of(perfil));

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(peliculaRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            deustoStreamService.addPeliculaToFavoritos(1L, 1L, session);
        });
        assertEquals("Usuario with id: 1 or Pelicula with id: 1 not found", exception.getMessage());
    }

    @Test
    void testAddSerieToFavoritos_AddsSuccessfully() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        Perfil perfil = new Perfil();
        perfil.setId(1L);
        perfil.setListaMeGustaSeries(new ArrayList<>());
        usuario.setPerfiles(List.of(perfil));

        Series serie = new Series("Breaking Bad", 2008, "Desc", Generos.DRAMA, null, "url");
        serie.setId(1L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(seriesRepository.findById(1L)).thenReturn(Optional.of(serie));
        when(session.getAttribute("perfil")).thenReturn(perfil);

        deustoStreamService.addSerieToFavoritos(1L, 1L, session);

        assertTrue(perfil.getListaMeGustaSeries().contains(serie));
        verify(perfilRepository).save(perfil);
    }

    @Test
    void testAddSerieToFavoritos_RemovesIfAlreadyExists() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        Perfil perfil = new Perfil();
        perfil.setId(1L);
        Series serie = new Series("Breaking Bad", 2008, "Desc", Generos.DRAMA, null, "url");
        serie.setId(1L);
        perfil.setListaMeGustaSeries(new ArrayList<>(List.of(serie)));
        usuario.setPerfiles(List.of(perfil));

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(seriesRepository.findById(1L)).thenReturn(Optional.of(serie));
        when(session.getAttribute("perfil")).thenReturn(perfil);

        deustoStreamService.addSerieToFavoritos(1L, 1L, session);

        assertFalse(perfil.getListaMeGustaSeries().contains(serie));
        verify(perfilRepository).save(perfil);
    }

    @Test
    void testAddSerieToFavoritos_UserNotFound_ThrowsException() {
        Series serie = new Series("Breaking Bad", 2008, "Desc", Generos.DRAMA, null, "url");
        serie.setId(1L);
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());
        when(seriesRepository.findById(1L)).thenReturn(Optional.of(serie));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            deustoStreamService.addSerieToFavoritos(1L, 1L, session);
        });
        assertEquals("Usuario with id: 1 or Serie with id: 1 not found", exception.getMessage());
    }
    @Test
    void testAddSerieToFavoritos_SerieNotFound_ThrowsException() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        Perfil perfil = new Perfil();
        perfil.setId(1L);
        usuario.setPerfiles(List.of(perfil));
        Long usuarioId = 1L;
        Long serieId = 1L;

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(seriesRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            deustoStreamService.addSerieToFavoritos(usuarioId, serieId, session);
        });
        assertEquals("Usuario with id: " + usuarioId + " or Serie with id: " + serieId + " not found", exception.getMessage());
    }

    @Test
    void testGetPerfilById_Found() {
        Perfil perfil = new Perfil("Pepe", "avatar1.png");
        perfil.setId(1L);

        when(perfilRepository.findById(1L)).thenReturn(Optional.of(perfil));

        Optional<Perfil> result = deustoStreamService.getPerfilById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testGetPerfilById_NotFound() {
        when(perfilRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Perfil> result = deustoStreamService.getPerfilById(1L);

        assertTrue(result.isEmpty());
    }

    @Test
    void testValorarPelicula_Success() {
        Pelicula pelicula = new Pelicula("Inception", Generos.ACCION, 148, 2010, "Sueños", "url");
        pelicula.setId(1L);
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        Perfil perfil = new Perfil();
        perfil.setId(2L);
        perfil.setUsuario(usuario);

        when(peliculaRepository.findById(1L)).thenReturn(Optional.of(pelicula));

        deustoStreamService.valorarPelicula(1L, perfil, 5, "Muy buena");

        verify(valoracionRepository).save(any(Valoracion.class));
    }

    @Test
    void testValorarPelicula_PeliculaNotFound_ThrowsException() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        Perfil perfil = new Perfil();
        perfil.setUsuario(usuario);

        when(peliculaRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            deustoStreamService.valorarPelicula(1L, perfil, 4, "Comentario");
        });
        assertEquals("Película or Usuario not found for the rating operation", ex.getMessage());
    }

    @Test
    void testValorarPelicula_PerfilSinUsuario_ThrowsEntityNotFoundException() {
        Pelicula pelicula = new Pelicula("Inception", Generos.ACCION, 148, 2010, "Sueños", "url");
        pelicula.setId(1L);
        Perfil perfil = new Perfil();
        perfil.setUsuario(null);

        when(peliculaRepository.findById(1L)).thenReturn(Optional.of(pelicula));

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            deustoStreamService.valorarPelicula(1L, perfil, 3, "Comentario");
        });
        assertEquals("Película or Usuario not found for the rating operation", ex.getMessage());
    }

    @Test
    void testValorarSerie_Success() {
        Series serie = new Series("Lost", 2004, "Desc", Generos.DRAMA, null, "img");
        serie.setId(1L);
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        Perfil perfil = new Perfil();
        perfil.setId(2L);
        perfil.setUsuario(usuario);

        when(seriesRepository.findById(1L)).thenReturn(Optional.of(serie));

        deustoStreamService.valorarSerie(1L, perfil, 4, "Muy buena serie");

        verify(valoracionRepository).save(any(Valoracion.class));
    }

    @Test
    void testValorarSerie_SerieNotFound_ThrowsEntityNotFoundException() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        Perfil perfil = new Perfil();
        perfil.setUsuario(usuario);

        when(seriesRepository.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            deustoStreamService.valorarSerie(1L, perfil, 2, "Comentario");
        });
        assertEquals("Series or Usuario not found for the rating operation", ex.getMessage());
    }

    @Test
    void testGetValoracionesPelicula_Success() {
        Pelicula pelicula = new Pelicula("Inception", Generos.ACCION, 148, 2010, "Sueños", "url");
        pelicula.setId(1L);
        Valoracion v1 = new Valoracion();
        Valoracion v2 = new Valoracion();
        List<Valoracion> valoraciones = Arrays.asList(v1, v2);

        when(peliculaRepository.findById(1L)).thenReturn(Optional.of(pelicula));
        when(valoracionRepository.findByPelicula(pelicula)).thenReturn(valoraciones);

        List<Valoracion> result = deustoStreamService.getValoracionesPelicula(1L);

        assertEquals(2, result.size());
        verify(valoracionRepository).findByPelicula(pelicula);
    }

    @Test
    void testGetValoracionesPelicula_PeliculaNotFound_ThrowsException() {
        when(peliculaRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            deustoStreamService.getValoracionesPelicula(99L);
        });
        assertEquals("Pelicula not found with id: 99", ex.getMessage());
    }

    @Test
    void testGetValoracionesSerie_Success() {
        Series serie = new Series("Lost", 2004, "Desc", Generos.DRAMA, null, "img");
        serie.setId(1L);
        Valoracion v1 = new Valoracion();
        List<Valoracion> valoraciones = List.of(v1);

        when(seriesRepository.findById(1L)).thenReturn(Optional.of(serie));
        when(valoracionRepository.findBySerie(serie)).thenReturn(valoraciones);

        List<Valoracion> result = deustoStreamService.getValoracionesSerie(1L);

        assertEquals(1, result.size());
        verify(valoracionRepository).findBySerie(serie);
    }

    @Test
    void testGetValoracionesSerie_SerieNotFound_ThrowsEntityNotFoundException() {
        when(seriesRepository.findById(88L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            deustoStreamService.getValoracionesSerie(88L);
        });
        assertEquals("Series not found with id: 88", ex.getMessage());
    }

    @Test
    void testGetValoracionesSerie_EmptyList() {
        Series serie = new Series("Breaking Bad", 2008, "Desc", Generos.DRAMA, null, "url");
        serie.setId(5L);
        
        when(seriesRepository.findById(5L)).thenReturn(Optional.of(serie));
        when(valoracionRepository.findBySerie(serie)).thenReturn(new ArrayList<>());
        
        List<Valoracion> result = deustoStreamService.getValoracionesSerie(5L);
        
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(valoracionRepository).findBySerie(serie);
    }

    @Test
    void testGetValoracionesSerie_MultipleValoraciones() {
        Series serie = new Series("Game of Thrones", 2011, "Desc", Generos.FANTASIA, null, "url");
        serie.setId(10L);
        
        Perfil perfil1 = new Perfil("User1", "avatar1.png");
        Perfil perfil2 = new Perfil("User2", "avatar2.png");
        
        Valoracion v1 = new Valoracion();
        v1.setPerfil(perfil1);
        v1.setSerie(serie);
        v1.setPuntuacion(5);
        v1.setComentario("Excelente");
        
        Valoracion v2 = new Valoracion();
        v2.setPerfil(perfil2);
        v2.setSerie(serie);
        v2.setPuntuacion(3);
        v2.setComentario("Regular");
        
        List<Valoracion> valoraciones = Arrays.asList(v1, v2);
        
        when(seriesRepository.findById(10L)).thenReturn(Optional.of(serie));
        when(valoracionRepository.findBySerie(serie)).thenReturn(valoraciones);
        
        List<Valoracion> result = deustoStreamService.getValoracionesSerie(10L);
        
        assertEquals(2, result.size());
        assertEquals(5, result.get(0).getPuntuacion());
        assertEquals("Regular", result.get(1).getComentario());
        verify(valoracionRepository).findBySerie(serie);
    }

    @Test
    void testCreateUsuario_ConSuscripcion() {
        // Arrange
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre("Carlos");
        nuevoUsuario.setApellido("Ramírez");
        nuevoUsuario.setCorreo("carlos@example.com");
        nuevoUsuario.setContrasenya("abcd1234");
        nuevoUsuario.setTipoSuscripcion("Estándar");

        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Usuario resultado = deustoStreamService.createUsuario(nuevoUsuario);

        // Assert
        assertNotNull(resultado);
        assertEquals("Estándar", resultado.getTipoSuscripcion());
    }

    @Test
    void testUpdateUsuario_SuscripcionActualizada() {
        // Arrange
        Long userId = 1L;
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(userId);
        usuarioExistente.setNombre("Ana");
        usuarioExistente.setApellido("Pérez");
        usuarioExistente.setCorreo("ana@example.com");
        usuarioExistente.setContrasenya("1234");
        usuarioExistente.setTipoSuscripcion("Básico");

        Usuario usuarioActualizado = new Usuario();
        usuarioActualizado.setNombre("Ana");
        usuarioActualizado.setApellido("Pérez");
        usuarioActualizado.setCorreo("ana@example.com");
        usuarioActualizado.setContrasenya("1234");
        usuarioActualizado.setTipoSuscripcion("Premium");

        when(usuarioRepository.findById(userId)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Usuario resultado = deustoStreamService.updateUsuario(userId, usuarioActualizado);

        // Assert
        assertNotNull(resultado);
        assertEquals("Premium", resultado.getTipoSuscripcion());
    }

    @Test
    void testUpdateUsuario_NoExiste() {
        Long userId = 999L;
        Usuario datos = new Usuario();
        datos.setTipoSuscripcion("Premium");
    
        when(usuarioRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            deustoStreamService.updateUsuario(userId, datos);
        });
    }

    @Test
    void testCreateUsuario_SinSuscripcion() {
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre("Luis");
        nuevoUsuario.setTipoSuscripcion(null); // o no seteado

        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario u = invocation.getArgument(0);
            if (u.getTipoSuscripcion() == null) {
                u.setTipoSuscripcion("Gratis"); // simulamos comportamiento por defecto
            }
            return u;
        });

        Usuario resultado = deustoStreamService.createUsuario(nuevoUsuario);

        assertEquals("Gratis", resultado.getTipoSuscripcion());
    }

    @Test
    void testCreateUsuario_TipoSuscripcionInvalido() {
        Usuario usuario = new Usuario();
        usuario.setNombre("Carlos");
        usuario.setTipoSuscripcion("SUPERGOLD");
    
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));
    
        Usuario resultado = deustoStreamService.createUsuario(usuario);
    
        assertNotNull(resultado);
        assertEquals("SUPERGOLD", resultado.getTipoSuscripcion());
    }

    @Test
    void testCrearPerfil_Success() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        Perfil perfil = new Perfil();

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(perfilRepository.save(perfil)).thenReturn(perfil);

        deustoStreamService.crearPerfil(perfil, 1L);

        assertEquals(usuario, perfil.getUsuario());
        assertTrue(usuario.getPerfiles().contains(perfil));
        verify(perfilRepository).save(perfil);
    }

    @Test
    void testCrearPerfil_UserNotFound() {
        Perfil perfil = new Perfil();
        when(usuarioRepository.findById(2L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
            () -> deustoStreamService.crearPerfil(perfil, 2L));
        assertTrue(ex.getMessage().contains("Usuario not found with id: 2"));
        verify(perfilRepository, never()).save(any(Perfil.class));
    }

    @Test
    void testEliminarPerfil_Success_UserPresent() {
        Usuario usuario = new Usuario();
        usuario.setId(3L);
        Perfil perfil = new Perfil();
        perfil.setId(5L);
        usuario.getPerfiles().add(perfil);

        when(perfilRepository.findById(5L)).thenReturn(Optional.of(perfil));
        when(usuarioRepository.findById(3L)).thenReturn(Optional.of(usuario));

        deustoStreamService.eliminarPerfil(5L, 3L);

        verify(valoracionRepository).deleteByPerfil_Id(5L);
        assertFalse(usuario.getPerfiles().contains(perfil));
        verify(usuarioRepository).save(usuario);
        verify(perfilRepository).delete(perfil);
    }

    @Test
    void testEliminarPerfil_Success_UserAbsent() {
        Perfil perfil = new Perfil();
        perfil.setId(6L);

        when(perfilRepository.findById(6L)).thenReturn(Optional.of(perfil));
        when(usuarioRepository.findById(4L)).thenReturn(Optional.empty());

        deustoStreamService.eliminarPerfil(6L, 4L);

        verify(valoracionRepository).deleteByPerfil_Id(6L);
        verify(usuarioRepository, never()).save(any(Usuario.class));
        verify(perfilRepository).delete(perfil);
    }

    @Test
    void testEliminarPerfil_PerfilNotFound() {
        when(perfilRepository.findById(7L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
            () -> deustoStreamService.eliminarPerfil(7L, 1L));
        assertTrue(ex.getMessage().contains("Perfil not found with id: 7"));
        verify(valoracionRepository, never()).deleteByPerfil_Id(anyLong());
        verify(perfilRepository, never()).delete(any(Perfil.class));
    }

}
