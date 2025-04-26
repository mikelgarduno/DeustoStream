package com.example.restapi.unit.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.restapi.model.Usuario;
import com.example.restapi.repository.UsuarioRepository;
import com.example.restapi.service.AuthService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_UsuarioExistente_DeberiaDevolverUsuario() {
        Usuario usuario = new Usuario("Nombre", "Apellido", "correo@ejemplo.com", "contrasenya123");

        when(usuarioRepository.findByCorreo("correo@ejemplo.com"))
                .thenReturn(Optional.of(usuario));

        Optional<Usuario> resultado = authService.login("correo@ejemplo.com", "contrasenya123");

        assertTrue(resultado.isPresent());
        assertEquals("correo@ejemplo.com", resultado.get().getCorreo());
        verify(usuarioRepository).findByCorreo("correo@ejemplo.com");
    }

    @Test
    void login_UsuarioNoExistente_DeberiaDevolverVacio() {
        when(usuarioRepository.findByCorreo("noexiste@ejemplo.com"))
                .thenReturn(Optional.empty());

        Optional<Usuario> resultado = authService.login("noexiste@ejemplo.com", "1234");

        assertFalse(resultado.isPresent());
    }

    @Test
    void existeCorreo_CorreoExistente_DeberiaDevolverTrue() {
        when(usuarioRepository.findByCorreo("correo@ejemplo.com"))
                .thenReturn(Optional.of(new Usuario()));

        boolean existe = authService.existeCorreo("correo@ejemplo.com");

        assertTrue(existe);
    }

    @Test
    void existeCorreo_CorreoNoExistente_DeberiaDevolverFalse() {
        when(usuarioRepository.findByCorreo("nuevo@ejemplo.com"))
                .thenReturn(Optional.empty());

        boolean existe = authService.existeCorreo("nuevo@ejemplo.com");

        assertFalse(existe);
    }
}
