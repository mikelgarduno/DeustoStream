package com.example.restapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.restapi.model.Generos;
import com.example.restapi.model.Pelicula;

@Repository
public interface PeliculaRepository extends JpaRepository<Pelicula, Long> {
    List<Pelicula> findByTituloContainingIgnoreCase(String titulo);

    List<Pelicula> findByGenero(Generos genero);

    /** Películas del mismo género excluyendo la actual*/
    List<Pelicula> findTop6ByGeneroAndIdNot(Generos genero, Long id);

    List<Pelicula> findByTituloContainingIgnoreCaseAndGenero(String titulo, Generos genero);
}
