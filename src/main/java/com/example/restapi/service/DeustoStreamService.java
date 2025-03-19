package com.example.restapi.service;

import com.example.restapi.model.Pelicula;
import com.example.restapi.model.Usuario;
import com.example.restapi.repository.PeliculaRepository;
import com.example.restapi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeustoStreamService {
    private final UsuarioRepository usuarioRepository;
    private final PeliculaRepository peliculaRepository;

    @Autowired
    public DeustoStreamService(UsuarioRepository usuarioRepository, PeliculaRepository peliculaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.peliculaRepository = peliculaRepository;
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

    public List<Pelicula> getAllPeliculas() {
        return peliculaRepository.findAll();
    }
    public Optional<Pelicula> getPeliculaById(Long id) {
        return peliculaRepository.findById(id);
    }

    

}
