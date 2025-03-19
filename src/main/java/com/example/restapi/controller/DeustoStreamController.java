package com.example.restapi.controller;

import com.example.restapi.model.Pelicula;
//import com.example.restapi.model.Book;
import com.example.restapi.model.Usuario;
//import com.example.restapi.service.BookService;
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

    @GetMapping("/peliculas")
    public List<Pelicula> getAllPeliculas() {
        return deustoStreamService.getAllPeliculas();
    }

    @GetMapping("/peliculas/{id}")
    public ResponseEntity<Pelicula> getPeliculaById(@PathVariable Long id) {
        Optional<Pelicula> pelicula = deustoStreamService.getPeliculaById(id);
        return pelicula.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
        
}
