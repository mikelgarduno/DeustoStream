package com.example.restapi.repository;

import com.example.restapi.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByCorreo(String correo); // método para buscar usuario por correo

    boolean existsByCorreo(String correo);// método para verificar si existe un correo
    
    @Query("SELECT u FROM Usuario u WHERE u.correo = :correo AND u.contrasenya = :contrasenya")
    Usuario findByCorreoAndContrasenya(@Param("correo") String correo, @Param("contrasenya") String contrasenya);
}
