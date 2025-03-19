package com.example.restapi.controller;

import com.example.restapi.model.Pelicula;
import com.example.restapi.model.Series;
import com.example.restapi.model.Usuario;
import com.example.restapi.service.DeustoStreamService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
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
    @GetMapping("/peliculas")
    public List<Pelicula> getAllPeliculas() {
        return deustoStreamService.getAllPeliculas();
    }

    @GetMapping("/peliculas/{id}")
    public ResponseEntity<Pelicula> getPeliculaById(@PathVariable Long id) {
        Optional<Pelicula> pelicula = deustoStreamService.getPeliculaById(id);
        return pelicula.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/peliculas")
    public ResponseEntity<Pelicula> createPelicula(@RequestBody Pelicula pelicula) {
        try {
            Pelicula createdPelicula = deustoStreamService.createPelicula(pelicula);
            return ResponseEntity.ok(createdPelicula);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/peliculas/{id}")
    public ResponseEntity<Pelicula> updatePelicula(@PathVariable Long id, @RequestBody Pelicula peliculaDetails) {
        try {
            Pelicula updatedPelicula = deustoStreamService.updatePelicula(id, peliculaDetails);
            return ResponseEntity.ok(updatedPelicula);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

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
    @GetMapping("/series")
    public List<Series> getAllSeries() {
        return deustoStreamService.getAllSeries();
    }

    @GetMapping("/series/{id}")
    public ResponseEntity<Series> getSeriesById(@PathVariable Long id) {
        Optional<Series> series = deustoStreamService.getSeriesById(id);
        return series.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/series")
    public ResponseEntity<Series> createSeries(@RequestBody Series series) {
        try {
            Series createdSeries = deustoStreamService.createSeries(series);
            return ResponseEntity.ok(createdSeries);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/series/{id}")
    public ResponseEntity<Series> updateSeries(@PathVariable Long id, @RequestBody Series seriesDetails) {
        try {
            Series updatedSeries = deustoStreamService.updateSeries(id, seriesDetails);
            return ResponseEntity.ok(updatedSeries);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

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

    

        
}
