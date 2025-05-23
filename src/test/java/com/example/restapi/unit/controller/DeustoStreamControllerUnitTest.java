package com.example.restapi.unit.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.restapi.controller.DeustoStreamController;
import com.example.restapi.model.Capitulo;
import com.example.restapi.model.Generos;
import com.example.restapi.model.Pelicula;
import com.example.restapi.model.Perfil;
import com.example.restapi.model.Series;
import com.example.restapi.model.Usuario;
import com.example.restapi.service.DeustoStreamService;

import jakarta.servlet.http.HttpSession;

/**
 * Tests unitarios del {@link DeustoStreamController}.
 * <p>
 * Contiene todos los casos originales del usuario **más** los añadidos para
 * cubrir los puntos rojos restantes (series, películas y capítulos) en Jacoco.
 * Sin pruebas de usuarios ni favoritos.
 * </p>
 */
@ExtendWith(MockitoExtension.class)
class DeustoStreamControllerUnitTest {

    @Mock
    private DeustoStreamService deustoStreamService;

    @InjectMocks
    private DeustoStreamController deustoStreamController;

    @Mock
    private HttpSession session;


    /* --------------------------------------------------------------------- */
    /* ------------------------ USUARIOS ----------------------------------- */
    /* --------------------------------------------------------------------- */
    @Test
    void testGetAllUsuarios() {
        Usuario usuario1 = new Usuario();
        usuario1.setNombre("user1");
        usuario1.setCorreo("correo@correo.es");
        usuario1.setContrasenya("pass1");
        Usuario usuario2 = new Usuario();
        usuario2.setNombre("user2");
        usuario2.setCorreo("correo1@correo.es");
        usuario2.setContrasenya("pass2");
        List<Usuario> usuarios = java.util.Arrays.asList(usuario1, usuario2);
        when(deustoStreamService.getAllUsuarios()).thenReturn(usuarios);
        List<Usuario> result = deustoStreamController.getAllUsuarios();
        assertEquals(2, result.size());
        assertEquals("user1", result.get(0).getNombre());
    }

    @Test
    void testGetUsuarioById_Found() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Ana");
        when(deustoStreamService.getUsuarioById(1L)).thenReturn(Optional.of(usuario));

        ResponseEntity<Usuario> response = deustoStreamController.getusuarioById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals("Ana", response.getBody().getNombre());
    }

    @Test
    void testGetUsuarioById_NotFound() {
        when(deustoStreamService.getUsuarioById(2L)).thenReturn(Optional.empty());

        ResponseEntity<Usuario> response = deustoStreamController.getusuarioById(2L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());
    }


    @Test
    void testCreateUsuario_ValidData() {
        Usuario usuario = new Usuario();
        usuario.setNombre("NuevoUsuario");
        usuario.setCorreo("nuevo@correo.com");
        usuario.setContrasenya("password");
        when(deustoStreamService.createUsuario(usuario)).thenReturn(usuario);

        ResponseEntity<Usuario> response = deustoStreamController.createUsuario(usuario);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("NuevoUsuario", response.getBody().getNombre());
    }

    @Test
    void testCreateUsuario_ExceptionThrown() {
        Usuario usuario = new Usuario();
        usuario.setNombre("ErrorUsuario");
        usuario.setCorreo("error@correo.com");
        usuario.setContrasenya("password");
        when(deustoStreamService.createUsuario(usuario)).thenThrow(new RuntimeException());

        ResponseEntity<Usuario> response = deustoStreamController.createUsuario(usuario);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    void testUpdateUsuario_ValidData() {
        Usuario usuarioDetails = new Usuario();
        usuarioDetails.setNombre("UpdatedName");
        usuarioDetails.setCorreo("updated@correo.com");
        usuarioDetails.setContrasenya("newpass");
        Usuario updatedUsuario = new Usuario();
        updatedUsuario.setId(1L);
        updatedUsuario.setNombre("UpdatedName");
        updatedUsuario.setCorreo("updated@correo.com");
        updatedUsuario.setContrasenya("newpass");

        when(deustoStreamService.updateUsuario(1L, usuarioDetails)).thenReturn(updatedUsuario);

        ResponseEntity<Usuario> response = deustoStreamController.updateUsuario(1L, usuarioDetails);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("UpdatedName", response.getBody().getNombre());
        assertEquals("updated@correo.com", response.getBody().getCorreo());
    }

    @Test
    void testUpdateUsuario_BadRequest() {
        Usuario usuarioDetails = new Usuario();
        usuarioDetails.setNombre("ErrorName");
        usuarioDetails.setCorreo("error@correo.com");
        usuarioDetails.setContrasenya("errorpass");

        when(deustoStreamService.updateUsuario(2L, usuarioDetails)).thenThrow(new RuntimeException());

        ResponseEntity<Usuario> response = deustoStreamController.updateUsuario(2L, usuarioDetails);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    void testDeleteUsuario_Found() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        when(deustoStreamService.getUsuarioById(1L)).thenReturn(Optional.of(usuario));

        ResponseEntity<Void> response = deustoStreamController.deleteUsuario(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDeleteUsuario_NotFound() {
        when(deustoStreamService.getUsuarioById(2L)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = deustoStreamController.deleteUsuario(2L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /* --------------------------------------------------------------------- */
    /* ------------------------ PELÍCULAS ---------------------------------- */
    /* --------------------------------------------------------------------- */

    @Test
    void testGetAllPeliculas() {
        List<Pelicula> peliculas = List.of(
                new Pelicula("Inception", Generos.ACCION, 148, 2010, "Sueños", "url"),
                new Pelicula("Matrix", Generos.CIENCIA_FICCION, 136, 1999, "Neo", "url2"));
        when(deustoStreamService.getAllPeliculas()).thenReturn(peliculas);
        List<Pelicula> result = deustoStreamController.getAllPeliculas();
        assertEquals(2, result.size());
        assertEquals("Inception", result.get(0).getTitulo());
    }

    @Test
    void testGetPeliculaById_Found() {
        Pelicula pelicula = new Pelicula("Inception", Generos.ACCION, 148, 2010, "Sueños", "url");
        when(deustoStreamService.getPeliculaById(1L)).thenReturn(Optional.of(pelicula));
        ResponseEntity<Pelicula> response = deustoStreamController.getPeliculaById(1L);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Inception", response.getBody().getTitulo());
    }

    @Test
    void testGetPeliculaById_NotFound() {
        when(deustoStreamService.getPeliculaById(2L)).thenReturn(Optional.empty());
        ResponseEntity<Pelicula> response = deustoStreamController.getPeliculaById(2L);
        assertEquals(404, response.getStatusCode().value());
        assertEquals(null, response.getBody());
    }

    @Test
    void testCreatePelicula_ValidData() {
        Pelicula pelicula = new Pelicula("Interstellar", Generos.CIENCIA_FICCION, 169, 2014, "Espacio", "url3");
        when(deustoStreamService.createPelicula(pelicula)).thenReturn(pelicula);
        ResponseEntity<Pelicula> response = deustoStreamController.createPelicula(pelicula);
        assertEquals(201, response.getStatusCode().value());
        assertEquals("Interstellar", response.getBody().getTitulo());
    }

    @Test
    void testCreatePelicula_InvalidData_NullPelicula() {
        ResponseEntity<Pelicula> response = deustoStreamController.createPelicula(null);
        assertEquals(400, response.getStatusCode().value());
        assertEquals(null, response.getBody());
    }

    @Test
    void testCreatePelicula_InvalidData_MissingTitulo() {
        Pelicula pelicula = new Pelicula(null, Generos.ACCION, 120, 2022, "Sin título", "url");
        ResponseEntity<Pelicula> response = deustoStreamController.createPelicula(pelicula);
        assertEquals(400, response.getStatusCode().value());
        assertEquals(null, response.getBody());
    }

    @Test
    void testCreatePelicula_InvalidData_InvalidAnio() {
        Pelicula pelicula = new Pelicula("Titulo", Generos.ACCION, 120, -1, "Descripción", "url");
        ResponseEntity<Pelicula> response = deustoStreamController.createPelicula(pelicula);
        assertEquals(400, response.getStatusCode().value());
        assertEquals(null, response.getBody());
    }

    @Test
    void testUpdatePelicula_ValidData() {
        Pelicula details = new Pelicula("Updated Title", Generos.DRAMA, 150, 2021, "Updated", "url");
        when(deustoStreamService.updatePelicula(1L, details)).thenReturn(details);
        ResponseEntity<Pelicula> response = deustoStreamController.updatePelicula(1L, details);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Updated Title", response.getBody().getTitulo());
    }

    @Test
    void testUpdatePelicula_NotFound() {
        Pelicula details = new Pelicula("Nonexistent", Generos.DRAMA, 120, 2020, "No", "url");
        when(deustoStreamService.updatePelicula(99L, details)).thenThrow(new RuntimeException());
        ResponseEntity<Pelicula> response = deustoStreamController.updatePelicula(99L, details);
        assertEquals(400, response.getStatusCode().value());
        assertEquals(null, response.getBody());
    }

    @Test
    void testDeletePelicula_Found() {
        when(deustoStreamService.getPeliculaById(1L)).thenReturn(Optional.of(new Pelicula()));
        ResponseEntity<Void> response = deustoStreamController.deletePelicula(1L);
        assertEquals(204, response.getStatusCode().value());
    }

    @Test
    void testDeletePelicula_NotFound() {
        when(deustoStreamService.getPeliculaById(99L)).thenReturn(Optional.empty());
        ResponseEntity<Void> response = deustoStreamController.deletePelicula(99L);
        assertEquals(404, response.getStatusCode().value());
    }

    /* --------------------------------------------------------------------- */
    /* -------------------------- SERIES ----------------------------------- */
    /* --------------------------------------------------------------------- */

    @Test
    void testGetAllSeries() {
        List<Series> seriesList = List.of(new Series("Breaking Bad", 2008, "Desc", Generos.DRAMA, null, "url"),
                new Series("GoT", 2011, "Desc", Generos.FANTASIA, null, "url2"));
        when(deustoStreamService.getAllSeries()).thenReturn(seriesList);
        List<Series> result = deustoStreamController.getAllSeries();
        assertEquals(2, result.size());
    }

    @Test
    void testGetSeriesById_Found() {
        Series serie = new Series("Breaking Bad", 2008, "Desc", Generos.DRAMA, null, "url");
        when(deustoStreamService.getSeriesById(1L)).thenReturn(Optional.of(serie));
        ResponseEntity<Series> response = deustoStreamController.getSeriesById(1L);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Breaking Bad", response.getBody().getTitulo());
    }

    @Test
    void testGetSeriesById_NotFound() {
        when(deustoStreamService.getSeriesById(2L)).thenReturn(Optional.empty());
        ResponseEntity<Series> response = deustoStreamController.getSeriesById(2L);
        assertEquals(404, response.getStatusCode().value());
        assertEquals(null, response.getBody());
    }

    @Test
    void testCreateSeries_AutoGenerarCapitulos() {
        Series input = new Series();
        input.setTitulo("The Witcher");
        input.setNumeroCapitulos(2);
        Series output = new Series();
        output.setTitulo("The Witcher");
        output.setNumeroCapitulos(2);
        output.setCapitulos(List.of(new Capitulo("Capítulo 1 de The Witcher", 30),
                new Capitulo("Capítulo 2 de The Witcher", 30)));
        when(deustoStreamService.createSeries(any(Series.class))).thenReturn(output);
        ResponseEntity<Series> response = deustoStreamController.createSeries(input);
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getCapitulos().size());
        assertEquals(2, response.getBody().getCapitulos().size());
        assertEquals("Capítulo 1 de The Witcher", response.getBody().getCapitulos().get(0).getTitulo());
    }

    @Test
    void testCreateSeries_InvalidData_NullSeries() {
        ResponseEntity<Series> response = deustoStreamController.createSeries(null);
        assertEquals(400, response.getStatusCode().value());
        assertEquals(null, response.getBody());
    }

    @Test
    void testCreateSeries_NoCapitulos() {
        Series input = new Series();
        input.setTitulo("Stranger Things");
        input.setNumeroCapitulos(0);
        input.setGenero(Generos.TERROR);
        input.setAnio(2016);
        
        Series output = new Series();
        output.setTitulo("Stranger Things");
        output.setNumeroCapitulos(0);
        output.setGenero(Generos.TERROR);
        output.setAnio(2016);
        output.setCapitulos(null);
        
        when(deustoStreamService.createSeries(any(Series.class))).thenReturn(output);
        
        ResponseEntity<Series> response = deustoStreamController.createSeries(input);
        
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().getNumeroCapitulos());
        assertEquals(null, response.getBody().getCapitulos());
    }

    @Test
    void testCreateSeries_WithExistingCapitulos() {
        Series input = new Series();
        input.setTitulo("Dark");
        input.setNumeroCapitulos(3);
        List<Capitulo> existingCapitulos = List.of(
                new Capitulo("Capítulo existente", 45));
        input.setCapitulos(existingCapitulos);
        
        Series output = new Series();
        output.setTitulo("Dark");
        output.setNumeroCapitulos(3);
        List<Capitulo> generatedCapitulos = List.of(
                new Capitulo("Capítulo 1 de Dark", 30),
                new Capitulo("Capítulo 2 de Dark", 30),
                new Capitulo("Capítulo 3 de Dark", 30));
        output.setCapitulos(generatedCapitulos);
        
        when(deustoStreamService.createSeries(any(Series.class))).thenReturn(output);
        
        ResponseEntity<Series> response = deustoStreamController.createSeries(input);
        
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode().value());
        assertEquals(3, response.getBody().getCapitulos().size());
        assertEquals("Capítulo 1 de Dark", response.getBody().getCapitulos().get(0).getTitulo());
    }

    @Test
    void testUpdateSeries_ValidData() {
        Series details = new Series("Updated Series", 2020, "Updated", Generos.DRAMA, null, "url");
        when(deustoStreamService.updateSeries(1L, details)).thenReturn(details);
        ResponseEntity<Series> response = deustoStreamController.updateSeries(1L, details);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Updated Series", response.getBody().getTitulo());
    }

    @Test
    void testUpdateSeries_NotFound() {
        Series details = new Series("No", 2020, "No", Generos.DRAMA, null, "url");
        when(deustoStreamService.updateSeries(99L, details)).thenThrow(new RuntimeException());
        ResponseEntity<Series> response = deustoStreamController.updateSeries(99L, details);
        assertEquals(400, response.getStatusCode().value());
        assertEquals(null, response.getBody());
    }

    @Test
    void testDeleteSeries_Found() {
        when(deustoStreamService.getSeriesById(1L)).thenReturn(Optional.of(new Series()));
        ResponseEntity<Void> response = deustoStreamController.deleteSeries(1L);
        assertEquals(204, response.getStatusCode().value());
    }

    @Test
    void testDeleteSeries_NotFound() {
        when(deustoStreamService.getSeriesById(99L)).thenReturn(Optional.empty());
        ResponseEntity<Void> response = deustoStreamController.deleteSeries(99L);
        assertEquals(404, response.getStatusCode().value());
    }

    /* --------------------------------------------------------------------- */
    /* ------------------------ CAPÍTULOS ---------------------------------- */
    /* --------------------------------------------------------------------- */

    @Test
    void testAddCapitulo_SerieExiste() {
        Series serie = new Series("Lost", 2004, "Desc", Generos.DRAMA, null, "url");
        Capitulo saved = new Capitulo("Capítulo 1 de Lost", 30);
        when(deustoStreamService.getSeriesById(1L)).thenReturn(Optional.of(serie));
        when(deustoStreamService.createCapitulo(any(Capitulo.class))).thenReturn(saved);
        ResponseEntity<Capitulo> response = deustoStreamController.addCapitulo(1L, new Capitulo());
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Capítulo 1 de Lost", response.getBody().getTitulo());
    }

    @Test
    void testAddCapitulo_SerieNoExiste() {
        when(deustoStreamService.getSeriesById(42L)).thenReturn(Optional.empty());
        ResponseEntity<Capitulo> response = deustoStreamController.addCapitulo(42L, new Capitulo());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testUpdateCapitulo_Ok() {
        Capitulo details = new Capitulo("Nuevo", 45);
        when(deustoStreamService.updateCapitulo(eq(1L), any(Capitulo.class))).thenReturn(details);
        ResponseEntity<Capitulo> response = deustoStreamController.updateCapitulo(1L, details);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Nuevo", response.getBody().getTitulo());
    }

    @Test
    void testUpdateCapitulo_BadRequest() {
        when(deustoStreamService.updateCapitulo(eq(1L), any(Capitulo.class))).thenThrow(new RuntimeException());
        ResponseEntity<Capitulo> response = deustoStreamController.updateCapitulo(1L, new Capitulo());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testDeleteCapitulo_Found() {
        when(deustoStreamService.getCapituloById(1L)).thenReturn(Optional.of(new Capitulo()));
        ResponseEntity<Void> response = deustoStreamController.deleteCapitulo(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDeleteCapitulo_NotFound() {
        when(deustoStreamService.getCapituloById(1L)).thenReturn(Optional.empty());
        ResponseEntity<Void> response = deustoStreamController.deleteCapitulo(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    /* --------------------------------------------------------------------- */
    /* --------------- RELACIONADAS (Películas / Series) -------------------- */
    /* --------------------------------------------------------------------- */

    @Test
    void testGetPeliculasRelacionadas() {
        List<Pelicula> rel = List.of(new Pelicula("Matrix", Generos.CIENCIA_FICCION, 136, 1999, "Neo", "url"));
        when(deustoStreamService.getPeliculasRelacionadas(1L)).thenReturn(rel);
        List<Pelicula> result = deustoStreamController.getPeliculasRelacionadas(1L);
        assertEquals(1, result.size());
        assertEquals("Matrix", result.get(0).getTitulo());
    }

    @Test
    void testGetSeriesRelacionadas() {
        List<Series> rel = List.of(new Series("Lost", 2004, "Desc", Generos.DRAMA, null, "url"));
        when(deustoStreamService.getSeriesRelacionadas(1L)).thenReturn(rel);
        List<Series> result = deustoStreamController.getSeriesRelacionadas(1L);
        assertEquals(1, result.size());
        assertEquals("Lost", result.get(0).getTitulo());
    }

    /* --------------------------------------------------------------------- */
    /* ------------------- Favoritos y Perfiles ---------------------------- */
    /* --------------------------------------------------------------------- */

    @Test
    void testAddPeliculaToFavoritos_Success() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        Pelicula pelicula = new Pelicula("Inception", Generos.ACCION, 148, 2010, "Sueños", "url");
        when(deustoStreamService.getUsuarioById(1L)).thenReturn(Optional.of(usuario));
        when(deustoStreamService.getPeliculaById(1L)).thenReturn(Optional.of(pelicula));

        ResponseEntity<Usuario> response = deustoStreamController.addPeliculaToFavoritos(1L, 1L, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void testAddPeliculaToFavoritos_UserNotFound() {
        when(deustoStreamService.getUsuarioById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Usuario> response = deustoStreamController.addPeliculaToFavoritos(1L, 1L, null);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testAddPeliculaToFavoritos_PeliculaNotFound() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        when(deustoStreamService.getUsuarioById(1L)).thenReturn(Optional.of(usuario));
        when(deustoStreamService.getPeliculaById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Usuario> response = deustoStreamController.addPeliculaToFavoritos(1L, 1L, null);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testAddSerieToFavoritos_Success() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        Series serie = new Series("Breaking Bad", 2008, "Desc", Generos.DRAMA, null, "url");
        when(deustoStreamService.getUsuarioById(1L)).thenReturn(Optional.of(usuario));
        when(deustoStreamService.getSeriesById(1L)).thenReturn(Optional.of(serie));

        ResponseEntity<Usuario> response = deustoStreamController.addSerieToFavoritos(1L, 1L, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void testAddSerieToFavoritos_UserNotFound() {
        when(deustoStreamService.getUsuarioById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Usuario> response = deustoStreamController.addSerieToFavoritos(1L, 1L, null);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testAddSerieToFavoritos_SerieNotFound() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        when(deustoStreamService.getUsuarioById(1L)).thenReturn(Optional.of(usuario));
        when(deustoStreamService.getSeriesById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Usuario> response = deustoStreamController.addSerieToFavoritos(1L, 1L, null);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testCambiarPerfil_Success() {
        Perfil perfil = new Perfil();
        perfil.setId(1L);
        when(deustoStreamService.getPerfilById(1L)).thenReturn(Optional.of(perfil));

        ResponseEntity<Perfil> response = deustoStreamController.cambiarPerfil(1L, session);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void testCambiarPerfil_NotFound() {
        when(deustoStreamService.getPerfilById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Perfil> response = deustoStreamController.cambiarPerfil(1L, null);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
