package com.example.restapi.integration;

import com.example.restapi.model.Usuario;
import com.example.restapi.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    public void setUp() {
        usuarioRepository.deleteAll();

        Usuario user = new Usuario("Test", "User","test@correo.com", "1234");
        usuarioRepository.save(user);
    }

    @Test
    public void loginCorrecto_redireccionaCatalogo() throws Exception {
        mockMvc.perform(post("/login")
                .param("correo", "test@correo.com")
                .param("contrasenya", "1234"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/catalogo"));
    }

    @Test
    public void loginFallido_muestraError() throws Exception {
        mockMvc.perform(post("/login")
        .param("correo", "test@correo.com")
        .param("contrasenya", "mala"))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("error"))
        .andExpect(view().name("login"));
    }

    @Test
    public void registroNuevoUsuario_redireccionaLogin() throws Exception {
        mockMvc.perform(post("/registro")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param("nombre","Nuevo")
        .param("apellido","apellido")
        .param("correo","nuevo@correo.com")
        .param("contrasenya", "pass123"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/catalogo"));
    }

    @Test
    public void registroFallido_correoRepetido() throws Exception {
        mockMvc.perform(post("/registro")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param("nombre", "Test")
        .param("apellidos", "apellidos")
        .param("correo", "test@correo.com")
        .param("contrasenya", "1234"))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("error"))
        .andExpect(view().name("registro"));
    }

}
