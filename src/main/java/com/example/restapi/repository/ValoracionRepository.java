package com.example.restapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.restapi.model.Pelicula;
import com.example.restapi.model.Series;
import com.example.restapi.model.Usuario;
import com.example.restapi.model.Valoracion;

public interface ValoracionRepository extends JpaRepository<Valoracion, Long> {
    List<Valoracion> findByPelicula(Pelicula pelicula);
    List<Valoracion> findBySerie(Series serie);
    List<Valoracion> findByUsuario(Usuario usuario);
}

