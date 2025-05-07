package com.example.restapi.unit.modelos;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import com.example.restapi.model.Perfil;
import com.example.restapi.model.Pelicula;
import com.example.restapi.model.Usuario;
import com.example.restapi.model.Generos;

class PerfilTest {

    @Test
    void testToString() {
        Perfil perfil = new Perfil("Pepe marcos", "avatar1.png");
        perfil.setId(1L);
        String toString = perfil.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("Pepe marcos"));
    }

    @Test
    void testGettersAndSetters() {
        Perfil perfil = new Perfil("Pepe marcos", "avatar1.png");
        perfil.setId(1L);
        perfil.setEdad(25);
        assertNotNull(perfil.getId());
        assertNotNull(perfil.getNombre());
        assertNotNull(perfil.getAvatar());
        assertNotNull(perfil.getEdad());
    }

    @Test
    void testConstructor() {
        Perfil perfil = new Perfil("Pepe marcos", "avatar1.png");
        assertNotNull(perfil);
        assertNotNull(perfil.getNombre());
        assertNotNull(perfil.getAvatar());
    }

    @Test
    void testEquals() {
        Usuario usuario1 = new Usuario();
        usuario1.setId(1L);
        Usuario usuario2 = new Usuario();
        usuario2.setId(2L);
        usuario1.setNombre("pepe");
        usuario2.setNombre("maria");
    
        Perfil perfil1 = new Perfil();
        perfil1.setId(1L);
        perfil1.setNombre("Pepe marcos");
        perfil1.setAvatar("avatar1.png");
        perfil1.setEdad(30);
        perfil1.setUsuario(usuario1);
    
        Perfil perfil2 = new Perfil();
        perfil2.setId(1L);
        perfil2.setNombre("Pepe marcos");
        perfil2.setAvatar("avatar1.png");
        perfil2.setEdad(30);
        perfil2.setUsuario(usuario1);
    
        Perfil perfilIdDiferente = new Perfil();
        perfilIdDiferente.setId(2L);
        perfilIdDiferente.setNombre("Pepe marcos");
        perfilIdDiferente.setAvatar("avatar1.png");
        perfilIdDiferente.setEdad(30);
        perfilIdDiferente.setUsuario(usuario1);
    
        Perfil perfilNombreDiferente = new Perfil();
        perfilNombreDiferente.setId(1L);
        perfilNombreDiferente.setNombre("Juan perez");
        perfilNombreDiferente.setAvatar("avatar1.png");
        perfilNombreDiferente.setEdad(30);
        perfilNombreDiferente.setUsuario(usuario1);
    
        Perfil perfilAvatarDiferente = new Perfil();
        perfilAvatarDiferente.setId(1L);
        perfilAvatarDiferente.setNombre("Pepe marcos");
        perfilAvatarDiferente.setAvatar("avatar2.png");
        perfilAvatarDiferente.setEdad(30);
        perfilAvatarDiferente.setUsuario(usuario1);
    
        Perfil perfilEdadDiferente = new Perfil();
        perfilEdadDiferente.setId(1L);
        perfilEdadDiferente.setNombre("Pepe marcos");
        perfilEdadDiferente.setAvatar("avatar1.png");
        perfilEdadDiferente.setEdad(40);
        perfilEdadDiferente.setUsuario(usuario1);
    
        Perfil perfilUsuarioDiferente = new Perfil();
        perfilUsuarioDiferente.setId(1L);
        perfilUsuarioDiferente.setNombre("Pepe marcos"); // Asegúrate de establecer el nombre
        perfilUsuarioDiferente.setAvatar("avatar1.png"); // Asegúrate de establecer el avatar
        perfilUsuarioDiferente.setEdad(30);             // Asegúrate de establecer la edad
        perfilUsuarioDiferente.setUsuario(usuario2);
    
        Perfil perfilNull = null;
    
        assertTrue(perfil1.equals(perfil2));
        assertTrue(perfil2.equals(perfil1));
        assertTrue(perfil1.equals(perfil1));
    
        assertFalse(perfil1.equals(perfilIdDiferente));
        assertFalse(perfil1.equals(perfilNombreDiferente));
        assertFalse(perfil1.equals(perfilAvatarDiferente));
        assertFalse(perfil1.equals(perfilEdadDiferente));
        assertFalse(perfil1.equals(perfilUsuarioDiferente));
        assertFalse(perfil1.equals(perfilNull));
    }

    @Test
    void testHashCode() {
        Perfil perfil1 = new Perfil("Pepe marcos", "avatar1.png");
        perfil1.setId(1L);
        Perfil perfil2 = new Perfil("Pepe marcos", "avatar1.png");
        perfil2.setId(1L);

        assertTrue(perfil1.hashCode() == perfil2.hashCode()); 
    }

    @Test
    void testRemovePelicula() {
        Perfil perfil = new Perfil("Pepe marcos", "avatar1.png");
        Pelicula pelicula = new Pelicula("Inception", Generos.ACCION, 148, 2010, "Sueños", "url");
        perfil.getListaMeGustaPeliculas().add(pelicula);

        perfil.removePelicula(pelicula);

        assertTrue(perfil.getListaMeGustaPeliculas().isEmpty());
    }

    @Test
    void testRemovePeliculaWithNullList() {
        Perfil perfil = new Perfil("Pepe marcos", "avatar1.png");
        perfil.setListaMeGustaPeliculas(null);
        // Calling removal when the list is null should not cause any errors.
        perfil.removePelicula(new Pelicula("Inception", Generos.ACCION, 148, 2010, "Sueños", "url"));
        assertTrue(true);
    }

    @Test
    void testGetUsuario() {
        Perfil perfil = new Perfil("Pepe marcos", "avatar1.png");
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        perfil.setUsuario(usuario);

        assertNotNull(perfil.getUsuario());
        assertEquals(1L, perfil.getUsuario().getId());
    }
}
