package com.example.restapi.model;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Valoracion implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int puntuacion; // por ejemplo de 1 a 5
    private String comentario;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    private Pelicula pelicula;

    @ManyToOne
    private Series serie;

    @ManyToOne
    private Perfil perfil;

    public Valoracion() {
    }

    public Valoracion(int puntuacion, String comentario, Usuario usuario, Pelicula pelicula, Series serie, Perfil perfil) {
        this.puntuacion = puntuacion;
        this.comentario = comentario;
        this.usuario = usuario;
        this.pelicula = pelicula;
        this.serie = serie;
        this.perfil = perfil;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Pelicula getPelicula() {
        return pelicula;
    }

    public void setPelicula(Pelicula pelicula) {
        this.pelicula = pelicula;
    }

    public Series getSerie() {
        return serie;
    }

    public void setSerie(Series serie) {
        this.serie = serie;

    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    @Override
    public String toString() {
        return "Valoracion{" +
                "id=" + id +
                ", puntuacion=" + puntuacion +
                ", comentario='" + comentario + '\'' +
                ", usuario=" + usuario +
                ", pelicula=" + pelicula +
                ", serie=" + serie +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Valoracion)) return false;

        Valoracion that = (Valoracion) o;

        if (puntuacion != that.puntuacion) return false;
        if (!id.equals(that.id)) return false;
        if (!comentario.equals(that.comentario)) return false;
        if (!usuario.equals(that.usuario)) return false;
        if (!pelicula.equals(that.pelicula)) return false;
        return serie.equals(that.serie);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + puntuacion;
        result = 31 * result + comentario.hashCode();
        result = 31 * result + usuario.hashCode();
        result = 31 * result + pelicula.hashCode();
        result = 31 * result + serie.hashCode();
        return result;
    }
    
}
