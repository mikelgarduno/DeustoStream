package com.example.restapi.client;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.example.restapi.model.Pelicula;
import com.example.restapi.model.Series;
import com.example.restapi.model.Usuario;

public class DeustoStreamManager {

    private String BASE_URL_TEMPLATE  = "http://%s:%s/api/";
    private final String USER_CONTROLLER_URL;
    private final String PELICULA_CONTROLLER_URL;
    private final String SERIES_CONTROLLER_URL;
    private final RestTemplate restTemplate;

    public DeustoStreamManager(String hostname, String port) {
        USER_CONTROLLER_URL = String.format(BASE_URL_TEMPLATE + "registro", hostname, port);
        PELICULA_CONTROLLER_URL = String.format(BASE_URL_TEMPLATE + "peliculas", hostname, port);
        SERIES_CONTROLLER_URL = String.format(BASE_URL_TEMPLATE + "series", hostname, port);
        this.restTemplate = new RestTemplate();
    }

    public void registerUser(Usuario user) {
        ResponseEntity<Void> response = restTemplate.postForEntity(USER_CONTROLLER_URL, user, Void.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("User registered successfully.");
        } else {
            System.out.println("Failed to register user. Status code: " + response.getStatusCode());
        }
    }

    public List<Usuario> getAllUsers() {
        ResponseEntity<Usuario[]> response = restTemplate.getForEntity(USER_CONTROLLER_URL, Usuario[].class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return List.of(response.getBody());
        } else {
            System.out.println("Failed to retrieve users. Status code: " + response.getStatusCode());
            return List.of();
        }
    }

    public void deleteUser(Long userId) {
        try {
            restTemplate.delete(USER_CONTROLLER_URL + "/" + userId);
            System.out.println("User deleted successfully.");
        } catch (RestClientException e) {
            System.out.println("Failed to delete user. " + e.getMessage());
        }
    }

    // Métodos para Películas
    public void addPelicula(Pelicula pelicula) {
        ResponseEntity<Void> response = restTemplate.postForEntity(PELICULA_CONTROLLER_URL, pelicula, Void.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Pelicula added successfully.");
        } else {
            System.out.println("Failed to add pelicula. Status code: " + response.getStatusCode());
        }
    }

    public List<Pelicula> getAllPeliculas() {
        ResponseEntity<Pelicula[]> response = restTemplate.getForEntity(PELICULA_CONTROLLER_URL, Pelicula[].class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return List.of(response.getBody());
        } else {
            System.out.println("Failed to retrieve peliculas. Status code: " + response.getStatusCode());
            return List.of();
        }
    }

    public void deletePelicula(Long peliculaId) {
        try {
            restTemplate.delete(PELICULA_CONTROLLER_URL + "/" + peliculaId);
            System.out.println("Pelicula deleted successfully.");
        } catch (RestClientException e) {
            System.out.println("Failed to delete pelicula. " + e.getMessage());
        }
    }

    // Métodos para Series
    public void addSeries(Series series) {
        ResponseEntity<Void> response = restTemplate.postForEntity(SERIES_CONTROLLER_URL, series, Void.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("Series added successfully.");
        } else {
            System.out.println("Failed to add series. Status code: " + response.getStatusCode());
        }
    }

    public List<Series> getAllSeries() {
        ResponseEntity<Series[]> response = restTemplate.getForEntity(SERIES_CONTROLLER_URL, Series[].class);
        if (response.getStatusCode().is2xxSuccessful()) {
            return List.of(response.getBody());
        } else {
            System.out.println("Failed to retrieve series. Status code: " + response.getStatusCode());
            return List.of();
        }
    }

    public void deleteSeries(Long seriesId) {
        try {
            restTemplate.delete(SERIES_CONTROLLER_URL + "/" + seriesId);
            System.out.println("Series deleted successfully.");
        } catch (RestClientException e) {
            System.out.println("Failed to delete series. " + e.getMessage());
        }
    }
}
