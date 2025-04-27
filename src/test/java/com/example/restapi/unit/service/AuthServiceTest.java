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
    @Mock
    private com.example.restapi.repository.PerfilRepository perfilRepository;

    @Test
    void register_CorreoNoExistente_DeberiaRegistrarUsuarioYPerfil() {
        Usuario nuevoUsuario = new Usuario("Nombre", "Apellido", "nuevo@ejemplo.com", "pass");
        when(usuarioRepository.findByCorreo("nuevo@ejemplo.com")).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(perfilRepository.save(any(com.example.restapi.model.Perfil.class))).thenAnswer(invocation -> invocation.getArgument(0));

        boolean resultado = authService.register(nuevoUsuario);

        assertTrue(resultado);
        verify(usuarioRepository).findByCorreo("nuevo@ejemplo.com");
        verify(usuarioRepository, times(2)).save(any(Usuario.class));
        verify(perfilRepository).save(any(com.example.restapi.model.Perfil.class));
    }

    @Test
    void register_CorreoExistente_DeberiaRetornarFalseYNoRegistrar() {
        Usuario existente = new Usuario("Nombre", "Apellido", "existe@ejemplo.com", "pass");
        when(usuarioRepository.findByCorreo("existe@ejemplo.com")).thenReturn(Optional.of(existente));

        Usuario nuevoUsuario = new Usuario("Nombre", "Apellido", "existe@ejemplo.com", "pass");
        boolean resultado = authService.register(nuevoUsuario);

        assertFalse(resultado);
        verify(usuarioRepository).findByCorreo("existe@ejemplo.com");
        verify(usuarioRepository, never()).save(any(Usuario.class));
        verify(perfilRepository, never()).save(any(com.example.restapi.model.Perfil.class));
    }

    @Test
    void obtenerRedireccion_Admin_DeberiaRedirigirAAdmin() {
        Usuario usuario = new Usuario();
        usuario.setCorreo("admin@deustostream.es");
        String redireccion = authService.obtenerRedireccion(usuario);
        assertEquals("/admin", redireccion);
    }

    @Test
    void obtenerRedireccion_UsuarioNormal_DeberiaRedirigirACatalogo() {
        Usuario usuario = new Usuario();
        usuario.setCorreo("user@ejemplo.com");
        String redireccion = authService.obtenerRedireccion(usuario);
        assertEquals("/catalogo", redireccion);
    }

    @Test
    void obtenerRedireccion_CorreoConMayusculas_DeberiaRedirigirAAdmin() {
        Usuario usuario = new Usuario();
        usuario.setCorreo("ADMIN@DEUSTOSTREAM.ES");
        String redireccion = authService.obtenerRedireccion(usuario);
        assertEquals("/admin", redireccion);
    }

    @Test
    void obtenerRedireccion_CorreoConEspacios_DeberiaRedirigirACatalogo() {
        Usuario usuario = new Usuario();
        usuario.setCorreo("  user@ejemplo.com  ");
        String redireccion = authService.obtenerRedireccion(usuario);
        assertEquals("/catalogo", redireccion);
    }

    @Test
    void obtenerRedireccion_CorreoNulo_DeberiaLanzarNullPointerException() {
        Usuario usuario = new Usuario();
        usuario.setCorreo(null);
        assertThrows(NullPointerException.class, () -> authService.obtenerRedireccion(usuario));
    }

    @Test
    void obtenerRedireccion_CorreoVacio_DeberiaRedirigirACatalogo() {
        Usuario usuario = new Usuario();
        usuario.setCorreo("");
        String redireccion = authService.obtenerRedireccion(usuario);
        assertEquals("/catalogo", redireccion);
    }

    @Test
    void obtenerRedireccion_CorreoSoloDominioAdmin_DeberiaRedirigirAAdmin() {
        Usuario usuario = new Usuario();
        usuario.setCorreo("@deustostream.es");
        String redireccion = authService.obtenerRedireccion(usuario);
        assertEquals("/admin", redireccion);
    }
}
