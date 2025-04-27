package com.example.restapi.client;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

import com.example.restapi.model.Generos;
import com.example.restapi.model.Pelicula;
import com.example.restapi.model.Perfil;
import com.example.restapi.model.Series;
import com.example.restapi.model.Usuario;
import com.example.restapi.service.DeustoStreamService;
import com.example.restapi.service.AuthService;

@Controller
public class WebController {

    // Logger para registrar mensajes
    private static final Logger logger = LoggerFactory.getLogger(WebController.class);

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

    @GetMapping("/acceso-denegado")
    public String accesoDenegado() {
        return "accesoDenegado"; // Devuelve la vista de acceso denegado
    }

    @GetMapping("/registro") // AGREGADO INICIO
    public String mostrarFormularioRegistro() {
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(@ModelAttribute Usuario nuevoUsuario, HttpSession session, Model model) {
        if (authService.register(nuevoUsuario)) {
            // Buscar al usuario registrado
            Optional<Usuario> usuarioOpt = authService.login(nuevoUsuario.getCorreo(), nuevoUsuario.getContrasenya());

            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();
                // Guardarlo en la sesión
                session.setAttribute("usuario", usuario);

                // Redireccionar según tipo
                return "redirect:" + authService.obtenerRedireccion(usuario);
            }
        }

        // Si no pudo registrarse (correo ya usado)
        model.addAttribute("error", "El correo ya está registrado.");
        return "registro";
    }

    @GetMapping("admin/peliculas")
    public String mostrarPeliculas(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario != null && usuario.getCorreo() != null) {
            logger.info("Correo del usuario: {}", usuario.getCorreo());

            if (usuario.getCorreo().endsWith("@deustostream.es")) {
                model.addAttribute("peliculas", deustoStreamService.getAllPeliculas());
                return "peliculas";
            }
        }

        logger.warn("Acceso denegado a /peliculas");
        return "redirect:/acceso-denegado";
    }

    @GetMapping("admin/series")
    public String mostrarSeries(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario != null && usuario.getCorreo() != null) {
            logger.info("Correo del usuario: {}", usuario.getCorreo());

            if (usuario.getCorreo().endsWith("@deustostream.es")) {
                model.addAttribute("series", deustoStreamService.getAllSeries());
                return "series";
            }
        }

        logger.warn("Acceso denegado a /series");
        return "redirect:/acceso-denegado";
    }

    @GetMapping("/suscripcion")
    public String verSuscripcion(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        
        if (usuario != null) {
            model.addAttribute("usuario", usuario);
            return "suscripcion";
        } else {
            return "redirect:/login"; // Si no hay sesión, redirigir a login
        }
    }

    @GetMapping("admin/usuarios")
    public String mostrarUsuarios(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario != null && usuario.getCorreo() != null) {
            logger.info("Correo del usuario: {}", usuario.getCorreo());

            if (usuario.getCorreo().endsWith("@deustostream.es")) {
                model.addAttribute("usuarios", deustoStreamService.getAllUsuarios());
                return "usuarios";
            }
        }

        logger.warn("Acceso denegado a /usuarios");
        return "redirect:/acceso-denegado";
    }

    @GetMapping("/catalogo")
    public String catalogo(
            @RequestParam(value = "titulo", required = false) String titulo,
            @RequestParam(value = "genero", required = false) Generos genero,
            Model model, HttpSession session) {
        List<Pelicula> peliculas = deustoStreamService.buscarPeliculasFiltradas(titulo, genero);
        List<Series> series = deustoStreamService.buscarSeriesFiltradas(titulo, genero);
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        Perfil perfil = usuario.getPerfiles().get(0);// Obtener el primer perfil basico del usuario
        model.addAttribute("peliculas", peliculas);
        model.addAttribute("series", series);
        model.addAttribute("peliculasFavoritas", perfil.getListaMeGustaPeliculas());
        model.addAttribute("seriesFavoritas", perfil.getListaMeGustaSeries());
        model.addAttribute("usuario", usuario);
        model.addAttribute("generos", Generos.values()); // Importa tu enum Generos

        return "catalogo"; // Asegúrate de que este es el nombre del archivo HTML en templates
    }

    @GetMapping("/peliculas")
    public String verPeliculas(Model model, HttpSession session,
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) Generos genero) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        List<Pelicula> peliculas = deustoStreamService.buscarPeliculasFiltradas(titulo, genero);
        Perfil perfil = usuario.getPerfiles().get(0);// Obtener el primer perfil basico del usuario
        model.addAttribute("peliculas", peliculas);
        model.addAttribute("peliculasFavoritas", perfil.getListaMeGustaPeliculas());
        model.addAttribute("usuario", usuario);
        model.addAttribute("generos", Generos.values());

        return "catPeliculas"; // apunta a templates/peliculas.html
    }

    @GetMapping("/series")
    public String verSeries(Model model, HttpSession session,
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) Generos genero) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        List<Series> series = deustoStreamService.buscarSeriesFiltradas(titulo, genero);
        Perfil perfil = usuario.getPerfiles().get(0);// Obtener el primer perfil basico del usuario

        model.addAttribute("series", series);
        model.addAttribute("seriesFavoritas", perfil.getListaMeGustaSeries());
        model.addAttribute("usuario", usuario);
        model.addAttribute("generos", Generos.values());

        return "catSeries"; // apunta a templates/series.html
    }

    @GetMapping("/pelicula/{id}")
    public String mostrarDetallePelicula(@PathVariable Long id, Model model) {
        Pelicula pelicula = deustoStreamService.getPeliculaById(id)
                .orElseThrow(() -> new RuntimeException("Película no encontrada"));

        model.addAttribute("pelicula", pelicula);
        // ← NUEVO: lista de relacionadas
        model.addAttribute("relacionadas",
                deustoStreamService.getPeliculasRelacionadas(id));

        return "detallePelicula";
    }

    // Detalle de película para ADMIN
    @GetMapping("/admin/pelicula/{id}")
    public String mostrarDetallePeliculaAdmin(@PathVariable Long id, Model model) {
        Pelicula pelicula = deustoStreamService.getPeliculaById(id)
                .orElseThrow(() -> new RuntimeException("Película no encontrada"));

        model.addAttribute("pelicula", pelicula);
        model.addAttribute("relacionadas",
                deustoStreamService.getPeliculasRelacionadas(id));

        return "detallePeliculaAdmin";
    }

    // ---------------- Detalle SERIE (usuario) ------------------
    @GetMapping("/serie/{id}")
    public String mostrarDetalleSerieUsuario(@PathVariable Long id, Model model) {
        Series serie = deustoStreamService.getSeriesById(id)
                .orElseThrow(() -> new RuntimeException("Serie no encontrada"));

        model.addAttribute("serie", serie);
        // ← NUEVO
        model.addAttribute("relacionadas",
                deustoStreamService.getSeriesRelacionadas(id));

        return "detalleSerie";
    }

    @GetMapping("/admin/serie/{id}")
    public String mostrarDetalleSerieAdmin(@PathVariable Long id, Model model) {
        Series serie = deustoStreamService.getSeriesById(id)
                .orElseThrow(() -> new RuntimeException("Serie no encontrada"));

        model.addAttribute("serie", serie);
        return "detalleSerieAdmin";
    }

    // Ver lista de peliculas favoritas
    @GetMapping("/guardados")
    public String mostrarGuardados(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario != null) {
            Perfil perfil = usuario.getPerfiles().get(0);// Obtener el primer perfil basico del usuario
            model.addAttribute("peliculasFavoritas", perfil.getListaMeGustaPeliculas());
            model.addAttribute("seriesFavoritas", perfil.getListaMeGustaSeries());
        } else {
            model.addAttribute("error", "Debes iniciar sesión para ver tus películas favoritas.");
        }
        return "guardados"; // Retorna el nombre del archivo HTML en /resources/templates/
    }

}