package com.example.restapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.restapi.model.Generos;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class GenerosController {

    @GetMapping("/api/generos")
    public List<String> getGeneros() {
        return Arrays.stream(Generos.values())  // Convierte el enum a un stream
                     .map(Enum::name)  // Obtiene el nombre de cada valor
                     .collect(Collectors.toList());  // Lo convierte en una lista
    }
}

