package com.example.restapi.client;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.example.restapi.model.Usuario;

public class DeustoStreamManager {

    private String USER_CONTROLLER_URL_TEMPLATE = "http://%s:%s/api/registro";
    private final String USER_CONTROLLER_URL;
    private final RestTemplate restTemplate;

    public DeustoStreamManager(String hostname, String port) {
        USER_CONTROLLER_URL = String.format(USER_CONTROLLER_URL_TEMPLATE, hostname, port);
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

    public static void main(String[] args) {
        if (args.length != 2) {
            System.exit(0);
        }

        String hostname = args[0];
        String port = args[1];

        DeustoStreamManager manager = new DeustoStreamManager(hostname, port);
        java.util.Scanner scanner = new java.util.Scanner(System.in);

        while (true) {
            System.out.println("1. Register User");
            System.out.println("2. List All Users");
            System.out.println("3. Delete User");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter nombre: ");
                    String nombre = scanner.nextLine();
                    System.out.print("Enter apellido: ");
                    String apellido = scanner.nextLine();
                    System.out.print("Enter fecha nacimiento: ");
                    String fechaNac = scanner.nextLine();
                    System.out.print("Enter correo: ");
                    String correo = scanner.nextLine();
                    System.out.print("Enter contraseña: ");
                    String contrasenya = scanner.nextLine();
                    Usuario user = new Usuario();
                    user.setNombre(nombre);
                    user.setApellido(apellido);
                    user.setFechaNac(fechaNac);
                    user.setCorreo(correo);
                    user.setContrasenya(contrasenya);
                    manager.registerUser(user);
                    break;
                case 2:
                    List<Usuario> users = manager.getAllUsers();
                    for (Usuario u : users) {
                        System.out.println("ID: " + u.getId());
                        System.out.println("Nombre: " + u.getNombre());
                        System.out.println("Apellido: " + u.getApellido());
                        System.out.println("Fecha Nacimiento: " + u.getFechaNac());
                        System.out.println("Correo: " + u.getCorreo());
                        System.out.println("---------------------------");
                    }
                    break;
                case 3:
                    System.out.print("Enter user ID to delete: ");
                    Long userId = scanner.nextLong();
                    manager.deleteUser(userId);
                    break;
                case 4:
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
