package com.example.restapi.service;

import com.example.restapi.model.Pelicula;
import com.example.restapi.model.Series;
import com.example.restapi.model.Usuario;
import com.example.restapi.repository.PeliculaRepository;
import com.example.restapi.repository.SerieRepository;
import com.example.restapi.repository.UsuarioRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeustoStreamService {
    private final UsuarioRepository usuarioRepository;
    private final PeliculaRepository peliculaRepository;
    private final SerieRepository serieRepository;

    @Autowired
    public DeustoStreamService(UsuarioRepository usuarioRepository, PeliculaRepository peliculaRepository,
            SerieRepository serieRepository) {
        this.usuarioRepository = usuarioRepository;
        this.peliculaRepository = peliculaRepository;
        this.serieRepository = serieRepository;
    }

    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> getUsuarioById(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario createUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario updateUsuario(Long id, Usuario usuarioDetalles) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            usuario.setNombre(usuarioDetalles.getNombre());
            usuario.setApellido(usuarioDetalles.getApellido());
            usuario.setCorreo(usuarioDetalles.getCorreo());
            usuario.setContrasenya(usuarioDetalles.getContrasenya());
            return usuarioRepository.save(usuario);
        } else {
            throw new RuntimeException("Usuario not found");
        }
    }

    public void deleteUsuario(Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
        } else {
            throw new RuntimeException("Usuario not found with id: " + id);
        }
    }

    // Métodos para Películas
    public List<Pelicula> getAllPeliculas() {
        return peliculaRepository.findAll();
    }

    public Optional<Pelicula> getPeliculaById(Long id) {
        return peliculaRepository.findById(id);
    }

    public Pelicula createPelicula(Pelicula pelicula) {
        return peliculaRepository.save(pelicula);
    }

    public Pelicula updatePelicula(Long id, Pelicula peliculaDetalles) {
        Optional<Pelicula> optionalPelicula = peliculaRepository.findById(id);
        if (optionalPelicula.isPresent()) {
            Pelicula pelicula = optionalPelicula.get();
            pelicula.setTitulo(peliculaDetalles.getTitulo());
            pelicula.setAnio(peliculaDetalles.getAnio());
            pelicula.setDuracion(peliculaDetalles.getDuracion());
            pelicula.setSinopsis(peliculaDetalles.getSinopsis());
            pelicula.setGenero(peliculaDetalles.getGenero());
            pelicula.setImagenUrl(peliculaDetalles.getImagenUrl());
            return peliculaRepository.save(pelicula);
        } else {
            throw new RuntimeException("Pelicula not found");
        }
    }

    public void deletePelicula(Long id) {
        Optional<Pelicula> optionalPelicula = peliculaRepository.findById(id);
        if (optionalPelicula.isPresent()) {
            Pelicula pelicula = optionalPelicula.get();
            // Eliminar la relación en listaMeGustaPeliculas de los usuarios
            List<Usuario> usuarios = usuarioRepository.findAll();
            for (Usuario usuario : usuarios) {
                if (usuario.getListaMeGustaPeliculas().contains(pelicula)) {
                    usuario.getListaMeGustaPeliculas().remove(pelicula);
                    usuarioRepository.save(usuario);
                }
            }
            peliculaRepository.delete(pelicula);
        } else {
            throw new RuntimeException("Pelicula not found with id: " + id);
        }
    }

    // Métodos para Series
    public List<Series> getAllSeries() {
        return serieRepository.findAll();
    }

    public Optional<Series> getSeriesById(Long id) {
        return serieRepository.findById(id);
    }

    public Series createSeries(Series series) {
        return serieRepository.save(series);
    }

    public Series updateSeries(Long id, Series seriesDetalles) {
        Optional<Series> optionalSeries = serieRepository.findById(id);
        if (optionalSeries.isPresent()) {
            Series series = optionalSeries.get();
            series.setTitulo(seriesDetalles.getTitulo());
            series.setAnio(seriesDetalles.getAnio());
            series.setDescripcion(seriesDetalles.getDescripcion());
            series.setGenero(seriesDetalles.getGenero());
            series.setImagenUrl(seriesDetalles.getImagenUrl());
            return serieRepository.save(series);
        } else {
            throw new RuntimeException("Series not found");
        }
    }

    public void deleteSeries(Long id) {
        if (serieRepository.existsById(id)) {
            serieRepository.deleteById(id);
        } else {
            throw new RuntimeException("Series not found with id: " + id);
        }
    }

    public void addPeliculaToFavoritos(Long usuarioId, Long peliculaId, HttpSession session) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(usuarioId);
        Optional<Pelicula> optionalPelicula = peliculaRepository.findById(peliculaId);

        if (optionalUsuario.isPresent() && optionalPelicula.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            Pelicula pelicula = optionalPelicula.get();

            if(usuario.getListaMeGustaPeliculas().contains(optionalPelicula.get())) {
                // Si la película ya está en la lista de favoritos, eliminarla
                usuario.getListaMeGustaPeliculas().remove(pelicula);
            }else {
                // Añadir la película a la lista de favoritos del usuario
                usuario.getListaMeGustaPeliculas().add(pelicula);
            }
            usuarioRepository.save(usuario);

            // Actualizar la sesión si es necesario
            session.setAttribute("usuario", usuario);

        } else {
            throw new RuntimeException("Usuario or Pelicula not found");
        }
    }

    public void addSerieToFavoritos(Long usuarioId, Long serieId, HttpSession session) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(usuarioId);
        Optional<Series> optionalSerie = serieRepository.findById(serieId);

        if (optionalUsuario.isPresent() && optionalSerie.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            Series serie = optionalSerie.get();

            if(usuario.getListaMeGustaSeries().contains(optionalSerie.get())) {
                // Si la serie ya está en la lista de favoritos, eliminarla
                usuario.getListaMeGustaSeries().remove(serie);
            }else {
                // Añadir la serie a la lista de favoritos del usuario
                usuario.getListaMeGustaSeries().add(serie);
            }
            usuarioRepository.save(usuario);

            // Actualizar la sesión si es necesario
            session.setAttribute("usuario", usuario);

        } else {
            throw new RuntimeException("Usuario or Serie not found");
        }

    }
}
