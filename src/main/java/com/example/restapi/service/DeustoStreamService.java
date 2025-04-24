package com.example.restapi.service;

import com.example.restapi.model.Capitulo;
import com.example.restapi.model.Generos;
import com.example.restapi.model.Pelicula;
import com.example.restapi.model.Series;
import com.example.restapi.model.Usuario;
import com.example.restapi.model.Perfil;
import com.example.restapi.repository.CapituloRepository;
import com.example.restapi.repository.PeliculaRepository;
import com.example.restapi.repository.SerieRepository;
import com.example.restapi.repository.UsuarioRepository;
import com.example.restapi.repository.PerfilRepository;

import jakarta.servlet.http.HttpSession;

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

    @Autowired
    public DeustoStreamService(UsuarioRepository usuarioRepository, PeliculaRepository peliculaRepository,
            SerieRepository serieRepository, CapituloRepository capituloRepository, PerfilRepository perfilRepository) {
        this.usuarioRepository = usuarioRepository;
        this.peliculaRepository = peliculaRepository;
        this.serieRepository = serieRepository;
        this.capituloRepository = capituloRepository;
        this.perfilRepository = perfilRepository;
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
        if (pelicula == null || pelicula.getTitulo() == null || pelicula.getAnio() <= 0) {
            throw new RuntimeException("Datos inválidos para la película");
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
            throw new RuntimeException("Pelicula not found");
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
            throw new RuntimeException("Pelicula not found with id: " + id);
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
            throw new RuntimeException("Series not found");
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
            throw new RuntimeException("Series not found with id: " + id);
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

            if (usuario.getPerfiles().get(0).getListaMeGustaPeliculas().contains(optionalPelicula.get())) {
                // Si la película ya está en la lista de favoritos, eliminarla
                usuario.getPerfiles().get(0).getListaMeGustaPeliculas().remove(pelicula);
            } else {
                // Añadir la película a la lista de favoritos del usuario
                usuario.getPerfiles().get(0).getListaMeGustaPeliculas().add(pelicula);
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

            if (usuario.getPerfiles().get(0).getListaMeGustaSeries().contains(optionalSerie.get())) {
                // Si la serie ya está en la lista de favoritos, eliminarla
                usuario.getPerfiles().get(0).getListaMeGustaSeries().remove(serie);
            } else {
                // Añadir la serie a la lista de favoritos del usuario
                usuario.getPerfiles().get(0).getListaMeGustaSeries().add(serie);
            }
            usuarioRepository.save(usuario);

            // Actualizar la sesión si es necesario
            session.setAttribute("usuario", usuario);

        } else {
            throw new RuntimeException("Usuario or Serie not found");
        }

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
            throw new RuntimeException("Capítulo no encontrado");
        }
    }

    public void deleteCapitulo(Long id) {
        if (capituloRepository.existsById(id)) {
            capituloRepository.deleteById(id);
        } else {
            throw new RuntimeException("Capítulo no encontrado con id: " + id);
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
}
