package com.example.restapi.service;

import com.example.restapi.model.Capitulo;
import com.example.restapi.model.Generos;
import com.example.restapi.model.Pelicula;
import com.example.restapi.model.Series;
import com.example.restapi.model.Usuario;
import com.example.restapi.model.Valoracion;
import com.example.restapi.model.Perfil;
import com.example.restapi.repository.CapituloRepository;
import com.example.restapi.repository.PeliculaRepository;
import com.example.restapi.repository.SerieRepository;
import com.example.restapi.repository.UsuarioRepository;
import com.example.restapi.repository.ValoracionRepository;
import com.example.restapi.repository.PerfilRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Collections;

@Service
public class DeustoStreamService {

    private final PerfilRepository perfilRepository;
    private final UsuarioRepository usuarioRepository;
    private final PeliculaRepository peliculaRepository;
    private final SerieRepository serieRepository;
    private final CapituloRepository capituloRepository;
    private final ValoracionRepository valoracionRepository;

    private static final String PER_STRING = "perfil";

    @Autowired
    public DeustoStreamService(UsuarioRepository usuarioRepository, PeliculaRepository peliculaRepository,
            SerieRepository serieRepository, CapituloRepository capituloRepository, PerfilRepository perfilRepository, ValoracionRepository valoracionRepository) {
        this.usuarioRepository = usuarioRepository;
        this.peliculaRepository = peliculaRepository;
        this.serieRepository = serieRepository;
        this.capituloRepository = capituloRepository;
        this.perfilRepository = perfilRepository;
        this.valoracionRepository = valoracionRepository;
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
            usuario.setTipoSuscripcion(usuarioDetalles.getTipoSuscripcion()); // Agregado
            return usuarioRepository.save(usuario);
        } else {
            throw new EntityNotFoundException("Usuario not found with id: " + id);
        }
    }

    public void deleteUsuario(Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Usuario not found with id: " + id);
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
        if (pelicula == null || pelicula.getTitulo() == null || pelicula.getAnio() <= 0) {
            throw new IllegalArgumentException("Datos inválidos para la película: título no puede ser nulo y año debe ser positivo");
        }
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
            throw new EntityNotFoundException("Pelicula not found with id: " + id);
        }
    }

    public void deletePelicula(Long id) {
        Optional<Pelicula> optionalPelicula = peliculaRepository.findById(id);
        if (optionalPelicula.isPresent()) {
            Pelicula pelicula = optionalPelicula.get();
            // Eliminar la relación en listaMeGustaPeliculas de los perfiles
            List<Perfil> perfiles = perfilRepository.findAll();
            for (Perfil perfil : perfiles) {
                if (perfil.getListaMeGustaPeliculas().contains(pelicula)) {
                    perfil.getListaMeGustaPeliculas().remove(pelicula);
                    perfilRepository.save(perfil);
                }
            }
            peliculaRepository.delete(pelicula);
        } else {
            throw new EntityNotFoundException("Pelicula not found with id: " + id);
        }
    }

    public List<Pelicula> buscarPeliculasFiltradas(String titulo, Generos genero) {
        if (titulo != null && !titulo.isEmpty() && genero != null && !genero.toString().isEmpty()) {
            return peliculaRepository.findByTituloContainingIgnoreCaseAndGenero(titulo, genero);
        } else if (titulo != null && !titulo.isEmpty()) {
            return peliculaRepository.findByTituloContainingIgnoreCase(titulo);
        } else if (genero != null && !genero.toString().isEmpty()) {
            return peliculaRepository.findByGenero(genero);
        } else {
            return peliculaRepository.findAll();
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
            throw new EntityNotFoundException("Series not found with id: " + id);
        }
    }

    public void deleteSeries(Long id) {
        Optional<Series> optionalSeries = serieRepository.findById(id);
        if (optionalSeries.isPresent()) {
            Series series = optionalSeries.get();
            // Eliminar la relación en listaMeGustaSeries de los usuarios
            List<Perfil> perfiles = perfilRepository.findAll();
            for (Perfil perfil : perfiles) {
                if (perfil.getListaMeGustaSeries().contains(series)) {
                    perfil.getListaMeGustaSeries().remove(series);
                    perfilRepository.save(perfil);
                }
            }
            serieRepository.delete(series);
        } else {
            throw new EntityNotFoundException("Series not found with id: " + id);
        }
    }

    public List<Series> buscarSeriesFiltradas(String titulo, Generos genero) {
        if (titulo != null && !titulo.isEmpty() && genero != null && !genero.toString().isEmpty()) {
            return serieRepository.findByTituloContainingIgnoreCaseAndGenero(titulo, genero);
        } else if (titulo != null && !titulo.isEmpty()) {
            return serieRepository.findByTituloContainingIgnoreCase(titulo);
        } else if (genero != null && !genero.toString().isEmpty()) {
            return serieRepository.findByGenero(genero);
        } else {
            return serieRepository.findAll();
        }
    }

    // Métodos para inicializar datos
    public void addPeliculaToFavoritos(Long usuarioId, Long peliculaId, HttpSession session) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(usuarioId);
        Optional<Pelicula> optionalPelicula = peliculaRepository.findById(peliculaId);

        if (optionalUsuario.isPresent() && optionalPelicula.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            Pelicula pelicula = optionalPelicula.get();
            Perfil perfil = session.getAttribute(PER_STRING) != null ? (Perfil) session.getAttribute(PER_STRING)
                    : usuario.getPerfiles().get(0);

            if (perfil.getListaMeGustaPeliculas().contains(optionalPelicula.get())) {
                // Si la película ya está en la lista de favoritos, eliminarla
                perfil.getListaMeGustaPeliculas().remove(pelicula);
            } else {
                // Añadir la película a la lista de favoritos del usuario
                perfil.getListaMeGustaPeliculas().add(pelicula);
            }
            perfilRepository.save(perfil);

        } else {
            throw new EntityNotFoundException("Usuario with id: " + usuarioId + " or Pelicula with id: " + peliculaId + " not found");
        }
    }

    public void addSerieToFavoritos(Long usuarioId, Long serieId, HttpSession session) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(usuarioId);
        Optional<Series> optionalSerie = serieRepository.findById(serieId);

        if (optionalUsuario.isPresent() && optionalSerie.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            Series serie = optionalSerie.get();
            Perfil perfil = session.getAttribute(PER_STRING) != null ? (Perfil) session.getAttribute(PER_STRING)
                    : usuario.getPerfiles().get(0);

            if (perfil.getListaMeGustaSeries().contains(optionalSerie.get())) {
                // Si la serie ya está en la lista de favoritos, eliminarla
                perfil.getListaMeGustaSeries().remove(serie);
            } else {
                // Añadir la serie a la lista de favoritos del usuario
                perfil.getListaMeGustaSeries().add(serie);
            }
            perfilRepository.save(perfil);

        } else {
            throw new EntityNotFoundException("Usuario with id: " + usuarioId + " or Serie with id: " + serieId + " not found");
        }

    }

    // DeustoStreamService.java
    public Usuario guardarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    // Métodos para capítulos
    public List<Capitulo> getAllCapitulos() {
        return capituloRepository.findAll();
    }

    public Optional<Capitulo> getCapituloById(Long id) {
        return capituloRepository.findById(id);
    }

    public Capitulo createCapitulo(Capitulo capitulo) {
        Capitulo nuevo = capituloRepository.save(capitulo);

        Series serie = capitulo.getSerie();
        if (serie != null) {
            int cantidad = capituloRepository.countBySerieId(serie.getId());
            serie.setNumeroCapitulos(cantidad);
            serieRepository.save(serie);
        }

        return nuevo;
    }

    public Capitulo updateCapitulo(Long id, Capitulo detalles) {
        Optional<Capitulo> optional = capituloRepository.findById(id);
        if (optional.isPresent()) {
            Capitulo cap = optional.get();
            cap.setDuracion(detalles.getDuracion());

            // actualizar el título si se pasa uno nuevo no vacío
            if (detalles.getTitulo() != null && !detalles.getTitulo().trim().isEmpty()) {
                cap.setTitulo(detalles.getTitulo());
            }

            return capituloRepository.save(cap);
        } else {
            throw new EntityNotFoundException("Capítulo no encontrado con id: " + id);
        }
    }

    public void deleteCapitulo(Long id) {
        if (capituloRepository.existsById(id)) {
            capituloRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException("Capítulo no encontrado con id: " + id);
        }
    }

    // Películas relacionadas
    public List<Pelicula> getPeliculasRelacionadas(Long peliculaId) {
        return peliculaRepository.findById(peliculaId)
                .map(p -> peliculaRepository.findTop6ByGeneroAndIdNot(p.getGenero(), p.getId()))
                .orElse(Collections.emptyList());
    }

    // Series relacionadas
    public List<Series> getSeriesRelacionadas(Long serieId) {
        return serieRepository.findById(serieId)
                .map(s -> serieRepository.findTop6ByGeneroAndIdNot(s.getGenero(), s.getId()))
                .orElse(Collections.emptyList());
    }

    // Obtener perfil por id
    public Optional<Perfil> getPerfilById(Long id) {
        return perfilRepository.findById(id);
    }

    public void valorarPelicula(Long peliculaId, Perfil perfil, int puntuacion, String comentario) {
        Optional<Pelicula> optionalPelicula = peliculaRepository.findById(peliculaId);

        if(optionalPelicula.isPresent() && perfil.getUsuario() != null) {
            Pelicula pelicula = optionalPelicula.get();

            Valoracion v = new Valoracion();
            v.setPelicula(pelicula);
            v.setPerfil(perfil);
            v.setPuntuacion(puntuacion);
            v.setComentario(comentario);

            valoracionRepository.save(v);
        }
        else {
            throw new EntityNotFoundException("Película or Usuario not found for the rating operation");
        }
    }

    public void valorarSerie(Long idSerie, Perfil perfil, int puntuacion, String comentario) {
        Optional<Series> optionalSerie = serieRepository.findById(idSerie);

        if(optionalSerie.isPresent() && perfil.getUsuario() != null) {
            Series serie = optionalSerie.get();

            Valoracion v = new Valoracion();
            v.setSerie(serie);
            v.setPerfil(perfil);
            v.setPuntuacion(puntuacion);
            v.setComentario(comentario);

            valoracionRepository.save(v);
        }
        else {
            throw new EntityNotFoundException("Series or Usuario not found for the rating operation");
        }
    }
    
    public List<Valoracion> getValoracionesPelicula(Long idPelicula) {
        Optional<Pelicula> optionalPelicula = peliculaRepository.findById(idPelicula);
        if (optionalPelicula.isEmpty()) {
            throw new EntityNotFoundException("Pelicula not found with id: " + idPelicula);
        }
        return valoracionRepository.findByPelicula(optionalPelicula.get());
    }

    public List<Valoracion> getValoracionesSerie(Long idSerie) {
        Optional<Series> optionalSerie = serieRepository.findById(idSerie);
        if (optionalSerie.isEmpty()) {
            throw new EntityNotFoundException("Series not found with id: " + idSerie);
        }
        return valoracionRepository.findBySerie(optionalSerie.get());
    }

    @Transactional
    public void crearPerfil(Perfil perfil, Long idUsuario) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(idUsuario);
        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            perfil.setUsuario(usuario);
            usuario.getPerfiles().add(perfil);
            perfilRepository.save(perfil);
        } else {
            throw new EntityNotFoundException("Usuario not found with id: " + idUsuario);
        }
    }

    @Transactional
    public void eliminarPerfil(Long idPerfil, Long idUsuario) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(idUsuario);
        Optional<Perfil> optionalPerfil = perfilRepository.findById(idPerfil);
        if (optionalPerfil.isPresent()) {
            Perfil perfil = optionalPerfil.get();

            valoracionRepository.deleteByPerfil_Id(idPerfil);

            // Eliminar la relación entre perfil y usuario
            if (optionalUsuario.isPresent()) {
                Usuario usuario = optionalUsuario.get();
                usuario.getPerfiles().remove(perfil);
                usuarioRepository.save(usuario);
            }
            
    
            // Finalmente, eliminar el perfil
            perfilRepository.delete(perfil);
        } else {
            throw new EntityNotFoundException("Perfil not found with id: " + idPerfil);
        }
    }

}
