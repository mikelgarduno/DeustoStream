package com.example.restapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "peliculas")
public class Pelicula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Enumerated
    @Column(nullable = false)
    private Generos genero;

    @Column(nullable = false)
    private int duracion;

    @Column(nullable = false)
    private int anio;

    // No-argument constructor
    public Pelicula() {
    }

    // All-argument constructor (optional, for convenience)
    public Pelicula(String titulo, String director, Generos genero, int duracion, int anio) {
        this.titulo = titulo;
        this.genero = genero;
        this.duracion = duracion;
        this.anio = anio;
    }

    // Getters and setters
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

    public Generos getGenero() {
        return genero;
    }

    public void setGenero(Generos genero) {
        this.genero = genero;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    @Override
    public String toString() {
        return "Pelicula{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", genero=" + genero +
                ", duracion=" + duracion +
                ", anio=" + anio +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Pelicula pelicula = (Pelicula) o;
        return titulo.equals(pelicula.titulo) &&
                genero == pelicula.genero &&
                duracion == pelicula.duracion &&
                anio == pelicula.anio;
    }
}
