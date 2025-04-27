package com.example.restapi.model;

import java.util.ArrayList;
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

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Perfil> perfiles = new ArrayList<>();

    // --- CAMPOS PARA SUSCRIPCIÓN ---
    @Column(nullable = false)
    private boolean suscripcionActiva = false;

    @Column
    private String tipoSuscripcion; // "mensual" o "anual"

    @Column
    private java.time.LocalDate fechaInicioSuscripcion;

    @Column
    private java.time.LocalDate fechaFinSuscripcion;

    // No-argument constructor
    public Usuario() {
    }

    // All-argument constructor (optional, for convenience)
    public Usuario(String nombre, String apellido, String correo, String contrasenya) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.contrasenya = contrasenya;
        this.perfiles = new ArrayList<>();

        Perfil perfilBase = new Perfil();
        perfilBase.setNombre(nombre + " " + apellido);
        perfilBase.setUsuario(this);
        this.perfiles.add(perfilBase);
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

    public List<Perfil> getPerfiles() {
        return perfiles;
    }

    public void setPerfiles(List<Perfil> perfiles) {
        this.perfiles = perfiles;
    }

    public boolean isSuscripcionActiva() {
        return suscripcionActiva;
    }

    public void setSuscripcionActiva(boolean suscripcionActiva) {
        this.suscripcionActiva = suscripcionActiva;
    }

    public String getTipoSuscripcion() {
        return tipoSuscripcion;
    }

    public void setTipoSuscripcion(String tipoSuscripcion) {
        this.tipoSuscripcion = tipoSuscripcion;
    }

    public java.time.LocalDate getFechaInicioSuscripcion() {
        return fechaInicioSuscripcion;
    }

    public void setFechaInicioSuscripcion(java.time.LocalDate fechaInicioSuscripcion) {
        this.fechaInicioSuscripcion = fechaInicioSuscripcion;
    }

    public java.time.LocalDate getFechaFinSuscripcion() {
        return fechaFinSuscripcion;
    }

    public void setFechaFinSuscripcion(java.time.LocalDate fechaFinSuscripcion) {
        this.fechaFinSuscripcion = fechaFinSuscripcion;
    }

    public void addPerfil(Perfil perfil) {
        perfiles.add(perfil);
        perfil.setUsuario(this);
    }

    public void removePerfil(Perfil perfil) {
        perfiles.remove(perfil);
        perfil.setUsuario(null);
    }

    
    @Override
    public String toString() {
        return "Usuario [id=" + id + ", nombre=" + nombre + ", apellido=" + apellido + ", correo=" + correo
                + ", contrasenya=" + contrasenya + "]";
    }

    @Override
    public boolean equals(Object o) {
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