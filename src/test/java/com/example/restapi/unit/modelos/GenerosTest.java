package com.example.restapi.unit.modelos;

import com.example.restapi.model.Generos;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GenerosTest {

    @Test
    void testGenerosEnumValues() {
        Generos[] expectedValues = {
                Generos.ACCION,
                Generos.COMEDIA,
                Generos.DRAMA,
                Generos.TERROR,
                Generos.CIENCIA_FICCION,
                Generos.FANTASIA,
                Generos.ROMANCE,
                Generos.DOCUMENTAL,
                Generos.ANIMACION,
                Generos.AVENTURA,
                Generos.MISTERIO,
                Generos.MUSICAL,
                Generos.HISTORICO
        };

        assertArrayEquals(expectedValues, Generos.values());
    }

    @Test
    void testGenerosEnumValueOf() {
        assertEquals(Generos.ACCION, Generos.valueOf("ACCION"));
        assertEquals(Generos.COMEDIA, Generos.valueOf("COMEDIA"));
        assertEquals(Generos.DRAMA, Generos.valueOf("DRAMA"));
        assertEquals(Generos.TERROR, Generos.valueOf("TERROR"));
        assertEquals(Generos.CIENCIA_FICCION, Generos.valueOf("CIENCIA_FICCION"));
        assertEquals(Generos.FANTASIA, Generos.valueOf("FANTASIA"));
        assertEquals(Generos.ROMANCE, Generos.valueOf("ROMANCE"));
        assertEquals(Generos.DOCUMENTAL, Generos.valueOf("DOCUMENTAL"));
        assertEquals(Generos.ANIMACION, Generos.valueOf("ANIMACION"));
        assertEquals(Generos.AVENTURA, Generos.valueOf("AVENTURA"));
        assertEquals(Generos.MISTERIO, Generos.valueOf("MISTERIO"));
        assertEquals(Generos.MUSICAL, Generos.valueOf("MUSICAL"));
        assertEquals(Generos.HISTORICO, Generos.valueOf("HISTORICO"));
    }
}
