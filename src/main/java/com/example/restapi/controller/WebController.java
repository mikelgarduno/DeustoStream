package com.example.restapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.restapi.model.Pelicula;
import com.example.restapi.model.Series;
import com.example.restapi.service.DeustoStreamService;

@Controller
public class WebController {

    @Autowired
    private DeustoStreamService deustoStreamService;

    @GetMapping("/")
    public String mostrarIndex() {
        return "index"; // Muestra la página principal con las opciones de login/registro
    }

    @GetMapping("/admin")
    public String mostrarPanelAdmin(Model model) {
        model.addAttribute("peliculas", deustoStreamService.getAllPeliculas());
        model.addAttribute("series", deustoStreamService.getAllSeries());
        model.addAttribute("usuarios", deustoStreamService.getAllUsuarios());
        return "panelAdmin"; // Redirige a un panel de administración
    }

    @GetMapping("/peliculas")
    public String mostrarPeliculas(Model model) {
        List<Pelicula> peliculas = deustoStreamService.getAllPeliculas();
        model.addAttribute("peliculas", peliculas);
        return "peliculas"; // Retorna el nombre del archivo HTML en /resources/templates/
    }

    @GetMapping("/series")
    public String mostrarSeries(Model model) {
        model.addAttribute("series", deustoStreamService.getAllSeries());
        return "series";
    }

    @GetMapping("/usuarios")
    public String mostrarUsuarios(Model model) {
        model.addAttribute("usuarios", deustoStreamService.getAllUsuarios());
        return "usuarios";
    }
    @GetMapping("/catalogo")
    public String mostrarCatalogo(Model model) {
        List<Pelicula> peliculas = deustoStreamService.getAllPeliculas();
        List<Series> series = deustoStreamService.getAllSeries();

        model.addAttribute("peliculas", peliculas);
        model.addAttribute("series", series);

        return "catalogo"; // Mapea a catalogo.html en /templates/
    }
}