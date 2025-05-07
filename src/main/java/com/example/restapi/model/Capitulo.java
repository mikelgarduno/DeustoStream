package com.example.restapi.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
public class Capitulo implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private int duracion; // en minutos

    @ManyToOne
    @JoinColumn(name = "serie_id")
    @JsonBackReference 
    private Series serie;

    public Capitulo() {
    }

    public Capitulo(String titulo, int duracion) {
        this.titulo = titulo;
        this.duracion = duracion;
    }

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

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public Series getSerie() {
        return serie;
    }

    public void setSerie(Series serie) {
        this.serie = serie;
    }

    @Override
    public String toString() {
        return "Capitulo{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", duracion=" + duracion +
                '}';
    }
}
