package com.example.restapi.model;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios") 
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false)
    private String correo;

    @Column(nullable = false)
    private String contrasenya;

    // No-argument constructor
    public Usuario() {
    }

    // All-argument constructor (optional, for convenience)
    public Usuario(String nombre, String apellido, String correo, String contrasenya) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.contrasenya = contrasenya;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasenya() {
        return contrasenya;
    }

    public void setContrasenya(String contrasenya) {
        this.contrasenya = contrasenya;
    }

    @Override
    public String toString() {
        return "Usuario [id=" + id + ", nombre=" + nombre + ", apellido=" + apellido + ", correo=" + correo + ", contrasenya=" + contrasenya + "]";
    }

    @Override
        public boolean equals(Object o){
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Usuario usuario = (Usuario) o;
            return nombre.equals(usuario.nombre) &&
            correo.equals(usuario.correo);
        }

        @Override
        public int hashCode() {
            return Objects.hash(correo, nombre);
        }
}