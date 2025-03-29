package com.example.restapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;// AGREGADO

import com.example.restapi.model.Pelicula;
import com.example.restapi.model.Series;
import com.example.restapi.model.Usuario; //AGRAGADO
import com.example.restapi.service.DeustoStreamService;
import com.example.restapi.service.AuthService; //AGREGADO

@Controller
public class WebController {

    @Autowired
    private DeustoStreamService deustoStreamService;
    @Autowired // AGREGADO
    private AuthService authService; // AGREGADO

    @GetMapping("/")
    public String mostrarIndex() {
        return "index"; // Muestra la página principal con las opciones de login/registro
    }

    @GetMapping("/login") // AGREGADO INICIO
    public String mostrarFormularioLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(@RequestParam String correo,
            @RequestParam String contrasenya,
            HttpSession session,
            Model model) {

        return authService.login(correo, contrasenya)
                .map(usuario -> {
                    session.setAttribute("usuario", usuario);
                    return "redirect:" + authService.obtenerRedireccion(usuario);
                })
                .orElseGet(() -> {
                    model.addAttribute("error", "Correo o contraseña incorrectas");
                    return "login";
                });
    }

    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    } // AGREGADO FIN

    @GetMapping("/admin")
    public String mostrarPanelAdmin(Model model) {
        model.addAttribute("peliculas", deustoStreamService.getAllPeliculas());
        model.addAttribute("series", deustoStreamService.getAllSeries());
        model.addAttribute("usuarios", deustoStreamService.getAllUsuarios());
        return "panelAdmin"; // Redirige a un panel de administración
    }

    @GetMapping("/registro") // AGREGADO INICIO
    public String mostrarFormularioRegistro() {
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(@ModelAttribute Usuario nuevoUsuario, Model model) {
        if (authService.register(nuevoUsuario)) {
            return "redirect:/login";
        } else {
            model.addAttribute("error", "El correo ya está registrado.");
            return "registro";
        }
    } // AGREGADO FIN

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
    public String catalogo(Model model) {
        List<Pelicula> peliculas = deustoStreamService.getAllPeliculas();
        List<Series> series = deustoStreamService.getAllSeries();
        model.addAttribute("peliculas", peliculas);
        model.addAttribute("series", series);
        return "catalogo"; // Asegúrate de que este es el nombre del archivo HTML en templates
    }

    // ver detalle de la pelicula
    @GetMapping("/pelicula/{id}")
    public String mostrarDetallePelicula(@PathVariable Long id, Model model) {
        Pelicula pelicula = deustoStreamService.getPeliculaById(id)
                .orElseThrow(() -> new RuntimeException("Película no encontrada"));

        model.addAttribute("pelicula", pelicula);

        return "detallePelicula";
    }

    //ver detalle de las series
    @GetMapping("/serie/{id}")
    public String mostrarDetalleSerie(@PathVariable Long id, Model model) {
        Series serie = deustoStreamService.getSeriesById(id)
                .orElseThrow(() -> new RuntimeException("Serie no encontrada"));

        model.addAttribute("serie", serie);
        
        return "detalleSerie"; 
    }

}