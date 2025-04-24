package com.example.restapi.controller;

import com.example.restapi.model.Capitulo;
import com.example.restapi.model.Pelicula;
import com.example.restapi.model.Series;
import com.example.restapi.model.Usuario;
import com.example.restapi.service.DeustoStreamService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/")
@Tag(name = "DeustoStream Controller", description = "API de DeustoStream")
public class DeustoStreamController {

    @Autowired
    private DeustoStreamService deustoStreamService;

    @GetMapping("registro")
    public List<Usuario> getAllUsuarios() {
        return deustoStreamService.getAllUsuarios();
    }

    @GetMapping("registro/{id}")
    public ResponseEntity<Usuario> getusuarioById(@PathVariable Long id) {
        Optional<Usuario> usuario = deustoStreamService.getUsuarioById(id);
        return usuario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("registro")
    public ResponseEntity<Usuario> createUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario createdUsuario = deustoStreamService.createUsuario(usuario);
            return ResponseEntity.ok(createdUsuario);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("registro/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable Long id, @RequestBody Usuario usuarioDetails) {
        try {
            Usuario updatedUsuario = deustoStreamService.updateUsuario(id, usuarioDetails);
            return ResponseEntity.ok(updatedUsuario);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("registro/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        Optional<Usuario> usuario = deustoStreamService.getUsuarioById(id);
        if (usuario.isPresent()) {
            deustoStreamService.deleteUsuario(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoints para Películas
    @Operation(summary = "Obtener todas las películas", description = "Devuelve la lista de todas las películas en la base de datos")
    @GetMapping("/peliculas")
    public List<Pelicula> getAllPeliculas() {
        return deustoStreamService.getAllPeliculas();
    }

    @Operation(summary = "Obtener una película por ID", description = "Devuelve una película si existe en la base de datos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Película encontrada"),
            @ApiResponse(responseCode = "404", description = "Película no encontrada")
    })
    @GetMapping("/peliculas/{id}")
    public ResponseEntity<Pelicula> getPeliculaById(@PathVariable Long id) {
        Optional<Pelicula> pelicula = deustoStreamService.getPeliculaById(id);
        return pelicula.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear una nueva película", description = "Añade una película a la base de datos")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Película creada"),
            @ApiResponse(responseCode = "400", description = "Error al crear película")
    })
    @PostMapping("/peliculas")
    public ResponseEntity<Pelicula> createPelicula(@RequestBody Pelicula pelicula) {
        // Verifica si la película tiene datos válidos
        if (pelicula == null || pelicula.getTitulo() == null || pelicula.getAnio() <= 0) {
            return ResponseEntity.badRequest().build();
        }
        // Guarda la película en la base de datos
        Pelicula nuevaPelicula = deustoStreamService.createPelicula(pelicula);

        // Devuelve la película recién creada
        return new ResponseEntity<>(nuevaPelicula, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar una película por ID")
    @PutMapping("/peliculas/{id}")
    public ResponseEntity<Pelicula> updatePelicula(@PathVariable Long id, @RequestBody Pelicula peliculaDetails) {
        try {
            Pelicula updatedPelicula = deustoStreamService.updatePelicula(id, peliculaDetails);
            return ResponseEntity.ok(updatedPelicula);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Eliminar una película por ID", description = "Elimina una pelicula de la base de datos ")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Pelicula eliminada"),
            @ApiResponse(responseCode = "404", description = "Pelicula no encontrada")
    })
    @DeleteMapping("/peliculas/{id}")
    public ResponseEntity<Void> deletePelicula(@PathVariable Long id) {
        Optional<Pelicula> pelicula = deustoStreamService.getPeliculaById(id);
        if (pelicula.isPresent()) {
            deustoStreamService.deletePelicula(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoints para Series
    @Operation(summary = "Obtener todas las series", description = "Devuelve la lista de todas las series disponibles")
    @GetMapping("/series")
    public List<Series> getAllSeries() {
        return deustoStreamService.getAllSeries();
    }

    @Operation(summary = "Obtener una serie por ID", description = "Devuelve una serie específica si está registrada en la base de datos")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Serie encontrada"),
            @ApiResponse(responseCode = "404", description = "Serie no encontrada")
    })
    @GetMapping("/series/{id}")
    public ResponseEntity<Series> getSeriesById(@PathVariable Long id) {
        Optional<Series> series = deustoStreamService.getSeriesById(id);
        return series.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear una nueva serie con capítulos", description = "Crea una nueva serie y genera los capítulos asociados")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Serie creada"),
            @ApiResponse(responseCode = "400", description = "Error al crear serie")
    })
    @PostMapping("/series")
    public ResponseEntity<Series> createSeries(@RequestBody Series series) {
        try {
            // Crear capítulos si el número de capítulos es mayor a 0
            if (series.getNumeroCapitulos() > 0) {
                List<Capitulo> capitulos = new ArrayList<>();
                for (int i = 0; i < series.getNumeroCapitulos(); i++) {
                    Capitulo capitulo = new Capitulo();
                    capitulo.setTitulo("Capítulo " + (i + 1) + " de " + series.getTitulo());
                    capitulo.setDuracion(30); // Duración por defecto
                    capitulo.setSerie(series);
                    capitulos.add(capitulo);
                }
                series.setCapitulos(capitulos);
            }

            Series createdSeries = deustoStreamService.createSeries(series);

            return ResponseEntity.status(HttpStatus.CREATED).body(createdSeries);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Actualizar una serie", description = "Modifica los datos de una serie existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Serie actualizada"),
            @ApiResponse(responseCode = "400", description = "Error al actualizar serie"),
            @ApiResponse(responseCode = "404", description = "Serie no encontrada")
    })

    @PutMapping("/series/{id}")
    public ResponseEntity<Series> updateSeries(@PathVariable Long id, @RequestBody Series seriesDetails) {
        try {
            Series updatedSeries = deustoStreamService.updateSeries(id, seriesDetails);
            return ResponseEntity.ok(updatedSeries);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Eliminar una serie", description = "Elimina una serie de la base de datos si existe")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Serie eliminada"),
            @ApiResponse(responseCode = "404", description = "Serie no encontrada")
    })
    @DeleteMapping("/series/{id}")
    public ResponseEntity<Void> deleteSeries(@PathVariable Long id) {
        Optional<Series> series = deustoStreamService.getSeriesById(id);
        if (series.isPresent()) {
            deustoStreamService.deleteSeries(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Añadir una película a la lista de favoritos", description = "Añade una película a la lista de favoritos de un usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Película añadida a favoritos"),
            @ApiResponse(responseCode = "404", description = "Usuario o película no encontrada")
    })
    @PostMapping("/usuarios/{usuarioId}/peliculas/{peliculaId}")
    public ResponseEntity<Usuario> addPeliculaToFavoritos(@PathVariable Long usuarioId, @PathVariable Long peliculaId,
            HttpSession session) {
        Optional<Usuario> usuario = deustoStreamService.getUsuarioById(usuarioId);
        Optional<Pelicula> pelicula = deustoStreamService.getPeliculaById(peliculaId);

        if (usuario.isPresent() && pelicula.isPresent()) {
            deustoStreamService.addPeliculaToFavoritos(usuario.get().getId(), pelicula.get().getId(), session);

            return ResponseEntity.ok(usuario.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Añadir una película a la lista de favoritos", description = "Añade una película a la lista de favoritos de un usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Película añadida a favoritos"),
            @ApiResponse(responseCode = "404", description = "Usuario o película no encontrada")
    })
    @PostMapping("/usuarios/{usuarioId}/series/{serieId}")
    public ResponseEntity<Usuario> addSerieToFavoritos(@PathVariable Long usuarioId, @PathVariable Long serieId,
            HttpSession session) {
        Optional<Usuario> usuario = deustoStreamService.getUsuarioById(usuarioId);
        Optional<Series> serie = deustoStreamService.getSeriesById(serieId);

        if (usuario.isPresent() && serie.isPresent()) {
            deustoStreamService.addSerieToFavoritos(usuario.get().getId(), serie.get().getId(), session);

            return ResponseEntity.ok(usuario.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Endpoints para Capítulos

    @Operation(summary = "Actualizar un capítulo", description = "Modifica la duración y título de un capítulo")
    @PutMapping("/capitulos/{id}")
    public ResponseEntity<Capitulo> updateCapitulo(@PathVariable Long id, @RequestBody Capitulo capituloDetails) {
        try {
            Capitulo actualizado = deustoStreamService.updateCapitulo(id, capituloDetails);
            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Eliminar un capítulo", description = "Elimina un capítulo por su ID")
    @DeleteMapping("/capitulos/{id}")
    public ResponseEntity<Void> deleteCapitulo(@PathVariable Long id) {
        Optional<Capitulo> cap = deustoStreamService.getCapituloById(id);
        if (cap.isPresent()) {
            deustoStreamService.deleteCapitulo(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Añadir un capítulo a una serie", description = "Crea un nuevo capítulo vacío (30 min por defecto) para la serie")
    @PostMapping("/series/{serieId}/capitulos")
    public ResponseEntity<Capitulo> addCapitulo(@PathVariable Long serieId, @RequestBody Capitulo capitulo) {
        Optional<Series> optionalSerie = deustoStreamService.getSeriesById(serieId);
        if (optionalSerie.isPresent()) {
            Series serie = optionalSerie.get();
            capitulo.setSerie(serie);
            if (capitulo.getTitulo() == null || capitulo.getTitulo().trim().isEmpty()) {
                int numero = (serie.getCapitulos() == null) ? 1 : serie.getCapitulos().size() + 1;
                capitulo.setTitulo("Capítulo " + numero + " de " + serie.getTitulo());
            }
            if (capitulo.getDuracion() == 0) {
                capitulo.setDuracion(30);
            }
            Capitulo nuevo = deustoStreamService.createCapitulo(capitulo);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // ---------- Películas relacionadas ----------
    @GetMapping("/peliculas/{id}/relacionadas")
    public List<Pelicula> getPeliculasRelacionadas(@PathVariable Long id) {
        return deustoStreamService.getPeliculasRelacionadas(id);
    }

    // ---------- Series relacionadas -------------
    @GetMapping("/series/{id}/relacionadas")
    public List<Series> getSeriesRelacionadas(@PathVariable Long id) {
        return deustoStreamService.getSeriesRelacionadas(id);
    }

}
