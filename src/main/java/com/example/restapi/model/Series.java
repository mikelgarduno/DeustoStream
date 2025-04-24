package com.example.restapi.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Series {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private int anio;
    private String descripcion;

    private int numeroCapitulos; 

    @Enumerated(EnumType.STRING)
    private Generos genero;

    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference 
    private List<Capitulo> capitulos;

    @ManyToMany(mappedBy = "listaMeGustaSeries")
    private List<Perfil> perfiles; // Relación ManyToMany con Usuario

    @Column(nullable = false)
    private String imagenUrl; // Nueva propiedad

    // Constructor vacío (necesario para JPA)
    public Series() {
    }

    // Constructor con parámetros
    public Series(String titulo, int anio, String descripcion, Generos genero, List<Capitulo> capitulos, String imagenUrl) {
        this.titulo = titulo;
        this.anio = anio;
        this.descripcion = descripcion;
        this.genero = genero;
        this.capitulos = capitulos != null ? capitulos : new ArrayList<>();
        this.imagenUrl = imagenUrl; // Inicializar imagenUrl
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Generos getGenero() {
        return genero;
    }

    public void setGenero(Generos genero) {
        this.genero = genero;
    }

    public List<Capitulo> getCapitulos() {
        return capitulos;
    }

    public void setCapitulos(List<Capitulo> capitulos) {
        this.capitulos = capitulos;
    }

     public int getNumeroCapitulos() {
        return numeroCapitulos;
    }

    public void setNumeroCapitulos(int numeroCapitulos) {
        this.numeroCapitulos = numeroCapitulos;
    }
    
    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public List<Perfil> getPerfiles() {
        return perfiles;
    }

    public void setPerfil(List<Perfil> perfiles) {
        this.perfiles = perfiles;
    }

    @Override
    public String toString() {
        return "Series{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", anio=" + anio +
                ", descripcion='" + descripcion + '\'' +
                ", genero=" + genero +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Series series = (Series) o;
        return anio == series.anio &&
                Objects.equals(id, series.id) &&
                Objects.equals(titulo, series.titulo) &&
                Objects.equals(descripcion, series.descripcion) &&
                genero == series.genero;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titulo, anio, descripcion, genero);
    }
}
