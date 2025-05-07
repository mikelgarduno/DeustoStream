package com.example.restapi.service;

import com.example.restapi.model.Perfil;
import com.example.restapi.model.Usuario;
import com.example.restapi.repository.PerfilRepository;
import com.example.restapi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PerfilRepository perfilRepository;

    @Autowired
    public AuthService(UsuarioRepository usuarioRepository, PerfilRepository perfilRepository) {
        this.usuarioRepository = usuarioRepository;
        this.perfilRepository = perfilRepository;
    }

    // valida las credenciales de un usuario
    public Optional<Usuario> login(String correo, String contrasenya) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            if (usuario.getContrasenya().equals(contrasenya)) {
                return Optional.of(usuario);
            }
        }
        return Optional.empty();
    }

    // registra un nuevo usuario si el correo no existe
    @Transactional
    public boolean register(Usuario nuevoUsuario) {
        Optional<Usuario> existente = usuarioRepository.findByCorreo(nuevoUsuario.getCorreo());
        if (existente.isPresent()) {
            return false; // correo ya registrado
        }

        // Guardar el nuevo usuario primero (para generar ID)
        nuevoUsuario = usuarioRepository.save(nuevoUsuario);

        // Crear el perfil básico
        Perfil perfil = new Perfil();
        perfil.setNombre("Perfil Principal");
        perfil.setListaMeGustaPeliculas(new ArrayList<>());
        perfil.setListaMeGustaSeries(new ArrayList<>());
        perfil.setUsuario(nuevoUsuario); // Asociar el usuario

        // Guardar el perfil
        perfilRepository.save(perfil);

        // Asignar el perfil al usuario
        List<Perfil> perfiles = new ArrayList<>();
        perfiles.add(perfil);
        nuevoUsuario.setPerfiles(perfiles);

        // Actualizar el usuario con su lista de perfiles
        usuarioRepository.save(nuevoUsuario);

        return true;
    }

    // verifica si un correo esta registrado para recuperar la cuenta
    public boolean existeCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo).isPresent();
    }

    // redireccion segun el tipo de usuario
    public String obtenerRedireccion(Usuario usuario) {

        String correo = usuario.getCorreo().toLowerCase();

        if (correo.endsWith("@deustostream.es")) {
            return "/admin";
        } else {
            return "/catalogo";
        }

    }
}
