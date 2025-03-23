package com.example.restapi.client;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.example.restapi.model.Pelicula;
import com.example.restapi.model.Series;
import com.example.restapi.model.Usuario;

public class DeustoStreamManager {

    private String BASE_URL_TEMPLATE = "http://%s:%s/api/";
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

    // ----------------------------------------
    // MÉTODOS PARA USUARIOS
    // ----------------------------------------
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

    // ----------------------------------------
    // MÉTODOS PARA PELÍCULAS
    // ----------------------------------------
    public void addPelicula(Pelicula pelicula) {
        try {
            ResponseEntity<Void> response = restTemplate.postForEntity(PELICULA_CONTROLLER_URL, pelicula, Void.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Película añadida correctamente.");
            }
        } catch (RestClientException e) {
            System.out.println("Error al añadir película: " + e.getMessage());
        }
    }

    public List<Pelicula> getAllPeliculas() {
        try {
            ResponseEntity<Pelicula[]> response = restTemplate.getForEntity(PELICULA_CONTROLLER_URL, Pelicula[].class);
            return response.getStatusCode().is2xxSuccessful() ? List.of(response.getBody()) : List.of();
        } catch (RestClientException e) {
            System.out.println("Error al obtener películas: " + e.getMessage());
            return List.of();
        }
    }

    public void updatePelicula(Long id, Pelicula pelicula) {
        try {
            restTemplate.put(PELICULA_CONTROLLER_URL + "/" + id, pelicula);
            System.out.println("Película actualizada correctamente.");
        } catch (RestClientException e) {
            System.out.println("Error al actualizar película: " + e.getMessage());
        }
    }

    public void deletePelicula(Long peliculaId) {
        try {
            restTemplate.delete(PELICULA_CONTROLLER_URL + "/" + peliculaId);
            System.out.println("Película eliminada correctamente.");
        } catch (RestClientException e) {
            System.out.println("Error al eliminar película: " + e.getMessage());
        }
    }

    // ----------------------------------------
    // MÉTODOS PARA SERIES
    // ----------------------------------------
    public void addSeries(Series series) {
        try {
            ResponseEntity<Void> response = restTemplate.postForEntity(SERIES_CONTROLLER_URL, series, Void.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Serie añadida correctamente.");
            }
        } catch (RestClientException e) {
            System.out.println("Error al añadir serie: " + e.getMessage());
        }
    }

    public List<Series> getAllSeries() {
        try {
            ResponseEntity<Series[]> response = restTemplate.getForEntity(SERIES_CONTROLLER_URL, Series[].class);
            return response.getStatusCode().is2xxSuccessful() ? List.of(response.getBody()) : List.of();
        } catch (RestClientException e) {
            System.out.println("Error al obtener series: " + e.getMessage());
            return List.of();
        }
    }

    public void updateSeries(Long id, Series series) {
        try {
            restTemplate.put(SERIES_CONTROLLER_URL + "/" + id, series);
            System.out.println("Serie actualizada correctamente.");
        } catch (RestClientException e) {
            System.out.println("Error al actualizar serie: " + e.getMessage());
        }
    }

    public void deleteSeries(Long seriesId) {
        try {
            restTemplate.delete(SERIES_CONTROLLER_URL + "/" + seriesId);
            System.out.println("Serie eliminada correctamente.");
        } catch (RestClientException e) {
            System.out.println("Error al eliminar serie: " + e.getMessage());
        }
    }
}
