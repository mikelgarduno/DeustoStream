package com.example.restapi.model;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import com.example.restapi.model.Usuario;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "usuario_series",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "serie_id")
    )
    private List<Series> listaMeGustaSeries; // Relación ManyToMany con Series

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "usuario_peliculas",
        joinColumns = @JoinColumn(name = "usuario_id", nullable = false),
        inverseJoinColumns = @JoinColumn(name = "pelicula_id", nullable = false)
    )
    private List<Pelicula> listaMeGustaPeliculas; // Relación ManyToMany con Peliculas

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

    public List<Series> getListaMeGustaSeries() {
        return listaMeGustaSeries;
    }

    public void setListaMeGustaSeries(List<Series> listaMeGustaSeries) {
        this.listaMeGustaSeries = listaMeGustaSeries;
    }

    public List<Pelicula> getListaMeGustaPeliculas() {
        return listaMeGustaPeliculas;
    }

    public void setListaMeGustaPeliculas(List<Pelicula> listaMeGustaPeliculas) {
        this.listaMeGustaPeliculas = listaMeGustaPeliculas;
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
    
    // Helper method to remove association with a Pelicula before deleting it.
    public void removePelicula(Pelicula pelicula) {
        if (listaMeGustaPeliculas != null) {
            listaMeGustaPeliculas.remove(pelicula);
        }
    }
}