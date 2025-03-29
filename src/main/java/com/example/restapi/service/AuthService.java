package com.example.restapi.service;

import com.example.restapi.model.Usuario;
import com.example.restapi.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository; 

    //valida las credenciales de un usuario
    public Optional<Usuario> login (String correo, String contrasenya){
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo);
        if (usuarioOpt.isPresent()){
            Usuario usuario = usuarioOpt.get();
            if (usuario.getContrasenya().equals(contrasenya)){
                return Optional.of(usuario);
            }
        }
        return Optional.empty();
    }
    //registra un nuevo usuario si el correo no existe
    public boolean register (Usuario nuevoUsuario){
        Optional<Usuario> existente = usuarioRepository.findByCorreo(nuevoUsuario.getCorreo());
        if (existente.isPresent()){
            return false; //correo ya registrado
        }
        usuarioRepository.save(nuevoUsuario);
        return true;
    }

    //verifica si un correo esta registrado para recuperar la cuenta
    public boolean existeCorreo (String correo){
        return usuarioRepository.findByCorreo(correo).isPresent();
    }

    //redireccion segun el tipo de usuario
    public String obtenerRedireccion(Usuario usuario) {

        String correo = usuario.getCorreo().toLowerCase();

        if (correo.endsWith("@deustostream.es")) {
            return "/usuarios";
        } else {
            return "/catalogo";
        }

    }
}
