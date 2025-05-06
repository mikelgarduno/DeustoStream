package com.example.restapi.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "perfiles") 
public class Perfil  implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column
    private String avatar;

    @Column
    private Integer edad;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnore
    private Usuario usuario;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "perfil_series",
        joinColumns = @JoinColumn(name = "perfil_id"),
        inverseJoinColumns = @JoinColumn(name = "serie_id")
    )
    private List<Series> listaMeGustaSeries = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "perfil_peliculas",
        joinColumns = @JoinColumn(name = "perfil_id"),
        inverseJoinColumns = @JoinColumn(name = "pelicula_id")
    )
    private List<Pelicula> listaMeGustaPeliculas = new ArrayList<>();

    // No-argument constructor
    public Perfil() {
    }

    // All-argument constructor (optional, for convenience)
    public Perfil(String nombre, String avatar) {
        this.nombre = nombre;
        this.avatar = avatar;
    }

    // Getters and Setters
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public Usuario getUsuario() {
        return usuario;
    }   

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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
        return "Perfil [id=" + id + ", nombre=" + nombre + ", avatar=" + avatar + ", edad=" + edad + ", usuario=" + usuario + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Perfil)) return false;
        Perfil perfil = (Perfil) o;
        return Objects.equals(id, perfil.id) && Objects.equals(nombre, perfil.nombre) && Objects.equals(avatar, perfil.avatar) && Objects.equals(edad, perfil.edad) && Objects.equals(usuario, perfil.usuario);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, avatar, edad, usuario);
    }

    
    // Helper method to remove association with a Pelicula before deleting it.
    public void removePelicula(Pelicula pelicula) {
        if (listaMeGustaPeliculas != null) {
            listaMeGustaPeliculas.remove(pelicula);
        }
    }
}