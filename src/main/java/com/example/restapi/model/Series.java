package com.example.restapi.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Series {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private int anio;
    private String descripcion;

    @Enumerated(EnumType.STRING)
    private Generos genero;

    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Temporada> temporadas;

    // Constructor vacío (necesario para JPA)
    public Series() {
    }

    // Constructor con parámetros
    public Series(String titulo, int anio, List<Temporada> temporadas) {
        this.titulo = titulo;
        this.anio = anio;
        this.temporadas = temporadas;
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

    public List<Temporada> getTemporadas() {
        return temporadas;
    }

    public void setTemporadas(List<Temporada> temporadas) {
        this.temporadas = temporadas;
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

    @Override
    public String toString() {
        return "Series{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", anio=" + anio +
                ", temporadas=" + temporadas +
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
                Objects.equals(temporadas, series.temporadas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titulo, anio, temporadas);
    }
}
