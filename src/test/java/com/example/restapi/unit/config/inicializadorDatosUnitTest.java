package com.example.restapi.unit.config;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.restapi.config.inicializarDatos;
import com.example.restapi.model.Generos;
import com.example.restapi.model.Pelicula;
import com.example.restapi.model.Perfil;
import com.example.restapi.model.Series;
import com.example.restapi.model.Usuario;
import com.example.restapi.repository.PeliculaRepository;
import com.example.restapi.repository.PerfilRepository;
import com.example.restapi.repository.SerieRepository;
import com.example.restapi.repository.UsuarioRepository;

public class inicializadorDatosUnitTest {

    @Mock
    private PeliculaRepository peliculaRepository;
    @Mock
    private SerieRepository serieRepository;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private PerfilRepository perfilRepository;

    @InjectMocks
    private inicializarDatos inicializarDatos;

    @BeforeEach
    void setUp() {
        // Inicializamos los mocks
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testRun_ShouldNotThrowException() throws Exception {
        // Asegurarse de que no se lance ninguna excepción al ejecutar el método
        try {
            inicializarDatos.run();
        } catch (Exception e) {
            throw new AssertionError("La ejecución del método run() debería ser exitosa sin excepciones.");
        }
    }
}
