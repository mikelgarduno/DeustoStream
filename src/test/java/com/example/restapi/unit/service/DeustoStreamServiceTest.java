package com.example.restapi.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.example.restapi.model.Series;
import com.example.restapi.repository.CapituloRepository;
import com.example.restapi.repository.PeliculaRepository;
import com.example.restapi.repository.SerieRepository;
import com.example.restapi.repository.UsuarioRepository;
import com.example.restapi.service.DeustoStreamService;

public class DeustoStreamServiceTest {
    @Mock
    private PeliculaRepository peliculaRepository;

    @Mock
    private SerieRepository seriesRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private CapituloRepository capituloRepository;

    @InjectMocks
    private DeustoStreamService deustoStreamService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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
        // Mock the behavior of the repository to return the pelicula when save is called
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
    void testDeletePelicula() {
        Pelicula pelicula = new Pelicula("Inception", Generos.ACCION, 148, 2010, "Un sueño dentro de otro", "url");
        pelicula.setId(1L);
        // Mock the behavior of the repository to return the pelicula when findById is
        // called
        when(peliculaRepository.findById(1L)).thenReturn(Optional.of(pelicula));

        deustoStreamService.deletePelicula(1L);

        verify(peliculaRepository).delete(pelicula);
    }
    @Test
    void testDeletePeliculaNotFound() {
        // Mock the behavior of the repository to return an empty Optional when findById is called
        when(peliculaRepository.findById(1L)).thenReturn(Optional.empty());

        // Assert that the RuntimeException is thrown with the correct message
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            deustoStreamService.deletePelicula(1L);
        });

        assertEquals("Pelicula not found with id: 1", exception.getMessage());
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
    void testDeleteSerie() {
        Series serie = new Series("Stranger Things", 2016, "Niños vs Demogorgon", Generos.CIENCIA_FICCION,
                new ArrayList<>(), "url5");
        serie.setId(1L);
        when(seriesRepository.findById(1L)).thenReturn(Optional.of(serie));

        deustoStreamService.deleteSeries(1L);

        verify(seriesRepository).delete(serie);
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
}
