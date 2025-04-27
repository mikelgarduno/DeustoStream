package com.example.restapi.integration;

import com.example.restapi.config.inicializarDatos;
import com.example.restapi.model.Pelicula;
import com.example.restapi.model.Series;
import com.example.restapi.model.Usuario;
import com.example.restapi.repository.PeliculaRepository;
import com.example.restapi.repository.SerieRepository;
import com.example.restapi.repository.UsuarioRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class inicializadorDatosTest {

    @MockitoBean
    private UsuarioRepository usuarioRepository;

    @MockitoBean
    private PeliculaRepository peliculaRepository;

    @MockitoBean
    private SerieRepository serieRepository;

    @Autowired
    private inicializarDatos inicializarDatos;

    @Test
    void testRunInicializaDatosSinErrores() throws Exception {
        inicializarDatos.run();

        verify(usuarioRepository, atLeastOnce()).deleteAll();
        verify(peliculaRepository, atLeastOnce()).deleteAll();
        verify(serieRepository, atLeastOnce()).deleteAll();
        

        verify(peliculaRepository, atLeastOnce()).save(any(Pelicula.class));
        verify(serieRepository, atLeastOnce()).save(any(Series.class));
        verify(usuarioRepository, atLeast(2)).save(any(Usuario.class));
    }
}
