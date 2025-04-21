package com.example.restapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.restapi.model.Generos;
import com.example.restapi.model.Series;

public interface SerieRepository extends JpaRepository<Series, Long> {
    
  // Método para buscar por título (contenga texto sin importar mayúsculas/minúsculas) y género
  List<Series> findByTituloContainingIgnoreCaseAndGenero(String titulo, Generos genero);

  // Método para buscar por título solamente (sin importar mayúsculas/minúsculas)
  List<Series> findByTituloContainingIgnoreCase(String titulo);

  // Método para buscar por género solamente
  List<Series> findByGenero(Generos genero);
}