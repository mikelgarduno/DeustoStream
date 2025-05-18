package com.example.restapi.client;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Cookie;

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
    private static final String USUARIO_STRING = "usuario";
    private static final String USUARIOS_STRING = "usuarios";
    private static final String PERFIL_STRING = "perfil";
    private static final String PELICULA_STRING = "peliculas";
    private static final String PEL_STRING = "pelicula";
    private static final String PEL_FAV_STRING = "peliculasFavoritas";
    private static final String SERIE_STRING = "series";
    private static final String SERIES_FAV_STRING = "seriesFavoritas";
    private static final String GENEROS_STRING = "generos";
    private static final String ERROR_STRING = "error";
    private static final String REL_STRING = "relacionadas";

    private final DeustoStreamService deustoStreamService;
    private final AuthService authService;

    @Autowired
    public WebController(DeustoStreamService deustoStreamService, AuthService authService) {
        this.deustoStreamService = deustoStreamService;
        this.authService = authService;
    }

    @GetMapping("/")
    public String mostrarIndex() {
        return "index"; // Muestra la página principal con las opciones de login/registro
    }

    @GetMapping("/login")
    public String mostrarFormularioLogin(
            Model model,
            @CookieValue(value = "correo", defaultValue = "") String correoValor,
            @CookieValue(value = "contrasenya", defaultValue = "") String contrasenyaValor,
            @CookieValue(value = "guardarContrasenya", defaultValue = "false") boolean guardarContrasenya) {
        model.addAttribute("correoValor", correoValor);
        model.addAttribute("contrasenyaValor", contrasenyaValor);
        model.addAttribute("guardarContrasenya", guardarContrasenya);
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(
            @RequestParam String correo,
            @RequestParam String contrasenya,
            @RequestParam(required = false) String guardarContrasenya, // viene sólo si está marcado
            HttpServletResponse response,
            HttpSession session,
            Model model) {
        boolean remember = (guardarContrasenya != null);

        if (remember) {
            // Crear cookies con validez, p.e. 7 días
            Cookie cCorreo = new Cookie("correo", correo);
            cCorreo.setMaxAge(7 * 24 * 60 * 60);
            cCorreo.setPath("/");
            cCorreo.setSecure(true); // Asegúrate de que la cookie sea segura
            response.addCookie(cCorreo);

            Cookie cPass = new Cookie("contrasenya", contrasenya);
            cPass.setMaxAge(7 * 24 * 60 * 60);
            cPass.setPath("/");
            cPass.setSecure(true); // Asegúrate de que la cookie sea segura
            response.addCookie(cPass);

            Cookie cGuardar = new Cookie("guardarContrasenya", "true");
            cGuardar.setMaxAge(7 * 24 * 60 * 60);
            cGuardar.setPath("/");
            cGuardar.setSecure(true); // Asegúrate de que la cookie sea segura
            response.addCookie(cGuardar);
        } else {
            // Borrar cookies
            Cookie borrarCorreo = new Cookie("correo", "");
            borrarCorreo.setMaxAge(0);
            borrarCorreo.setPath("/");
            borrarCorreo.setSecure(true); // Asegúrate de que la cookie sea segura
            response.addCookie(borrarCorreo);

            Cookie borrarPass = new Cookie("contrasenya", "");
            borrarPass.setMaxAge(0);
            borrarPass.setPath("/");
            borrarPass.setSecure(true); // Asegúrate de que la cookie sea segura
            response.addCookie(borrarPass);

            Cookie cGuardar = new Cookie("guardarContrasenya", "false");
            cGuardar.setMaxAge(7 * 24 * 60 * 60);
            cGuardar.setPath("/");
            cGuardar.setSecure(true); // Asegúrate de que la cookie sea segura
            response.addCookie(cGuardar);
        }

        return authService.login(correo, contrasenya)
                .map(usuario -> {
                    session.setAttribute(USUARIO_STRING, usuario);
                    return "redirect:" + authService.obtenerRedireccion(usuario);
                })
                .orElseGet(() -> {
                    model.addAttribute(ERROR_STRING, "Correo o contraseña incorrectas");
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
        model.addAttribute(PELICULA_STRING, deustoStreamService.getAllPeliculas());
        model.addAttribute(SERIE_STRING, deustoStreamService.getAllSeries());
        model.addAttribute(USUARIOS_STRING, deustoStreamService.getAllUsuarios());
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
                session.setAttribute(USUARIO_STRING, usuario);

                // Redireccionar según tipo
                return "redirect:" + authService.obtenerRedireccion(usuario);
            }
        }

        // Si no pudo registrarse (correo ya usado)
        model.addAttribute(ERROR_STRING, "El correo ya está registrado.");
        return "registro";
    }

    @GetMapping("admin/peliculas")
    public String mostrarPeliculas(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute(USUARIO_STRING);

        if (usuario != null && usuario.getCorreo() != null) {
            logger.info("Correo del usuario: {}", usuario.getCorreo());

            if (usuario.getCorreo().endsWith("@deustostream.es")) {
                model.addAttribute(PELICULA_STRING, deustoStreamService.getAllPeliculas());
                return PELICULA_STRING;
            }
        }

        logger.warn("Acceso denegado a /peliculas");
        return "redirect:/acceso-denegado";
    }

    @GetMapping("admin/series")
    public String mostrarSeries(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute(USUARIO_STRING);

        if (usuario != null && usuario.getCorreo() != null) {
            logger.info("Correo del usuario: {}", usuario.getCorreo());

            if (usuario.getCorreo().endsWith("@deustostream.es")) {
                model.addAttribute(SERIE_STRING, deustoStreamService.getAllSeries());
                return SERIE_STRING;
            }
        }

        logger.warn("Acceso denegado a /series");
        return "redirect:/acceso-denegado";
    }

    @GetMapping("/suscripcion")
    public String verSuscripcion(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute(USUARIO_STRING);

        if (usuario != null) {
            model.addAttribute(USUARIO_STRING, usuario);
            return "suscripcion";
        } else {
            return "redirect:/login"; // Si no hay sesión, redirigir a login
        }
    }

    @GetMapping("admin/usuarios")
    public String mostrarUsuarios(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute(USUARIO_STRING);

        if (usuario != null && usuario.getCorreo() != null) {
            logger.info("Correo del usuario: {}", usuario.getCorreo());

            if (usuario.getCorreo().endsWith("@deustostream.es")) {
                model.addAttribute(USUARIOS_STRING, deustoStreamService.getAllUsuarios());
                return USUARIOS_STRING;
            }
        }

        logger.warn("Acceso denegado a /usuarios");
        return "redirect:/acceso-denegado";
    }

    @GetMapping("/catalogo")
    public String catalogo(
            @RequestParam(value = "titulo", required = false) String titulo,
            @RequestParam(value = "genero", required = false) Generos genero,
            @RequestParam(value = "duracion", required = false) String duracion,
            @RequestParam(value = "anio", required = false) String anio,
            Model model, HttpSession session) {
        List<Pelicula> peliculas = deustoStreamService.buscarPeliculasFiltradas(titulo, genero);
        List<Series> series = deustoStreamService.buscarSeriesFiltradas(titulo, genero);

        // Filtrar por duración y año si se proporcionan
        if (duracion != null && !duracion.isEmpty()) {
            for (int i = 0; i < peliculas.size(); i++) {
                int duracionPelicula = peliculas.get(i).getDuracion();
                if (duracionPelicula > Integer.parseInt(duracion)) {
                    peliculas.remove(i);
                    i--;
                }
            }
        }

        if (anio != null && !anio.isEmpty()) {
            for (int i = 0; i < peliculas.size(); i++) {
                int anioPelicula = peliculas.get(i).getAnio();
                if (Integer.parseInt(anio) == 2000) {
                    if (anioPelicula <= Integer.parseInt(anio)) {
                        peliculas.remove(i);
                        i--;
                    }
                } else {
                    if (anioPelicula > Integer.parseInt(anio)) {
                        peliculas.remove(i);
                        i--;
                    }
                }

            }

            for (int i = 0; i < series.size(); i++) {
                int anioSerie = series.get(i).getAnio();
                if (Integer.parseInt(anio) == 2000) {
                    if (anioSerie <= Integer.parseInt(anio)) {
                        series.remove(i);
                        i--;
                    }
                } else {
                    if (anioSerie > Integer.parseInt(anio)) {
                        series.remove(i);
                        i--;
                    }
                }
            }
        }

        Usuario usuario = (Usuario) session.getAttribute(USUARIO_STRING);
        Perfil perfil = session.getAttribute(PERFIL_STRING) != null ? (Perfil) session.getAttribute(PERFIL_STRING)
                : usuario.getPerfiles().get(0);
        model.addAttribute(PELICULA_STRING, peliculas);
        model.addAttribute(SERIE_STRING, series);
        model.addAttribute(PEL_FAV_STRING, perfil.getListaMeGustaPeliculas());
        model.addAttribute(SERIES_FAV_STRING, perfil.getListaMeGustaSeries());
        model.addAttribute(USUARIO_STRING, usuario);
        model.addAttribute(GENEROS_STRING, Generos.values()); // Importa tu enum Generos
        model.addAttribute("avatar", perfil.getAvatar());

        return "catalogo"; // Asegúrate de que este es el nombre del archivo HTML en templates
    }

    @GetMapping("/peliculas")
    public String verPeliculas(Model model, HttpSession session,
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) Generos genero) {
        Usuario usuario = (Usuario) session.getAttribute(USUARIO_STRING);
        List<Pelicula> peliculas = deustoStreamService.buscarPeliculasFiltradas(titulo, genero);
        Perfil perfil = usuario.getPerfiles().get(0);// Obtener el primer perfil basico del usuario
        model.addAttribute(PELICULA_STRING, peliculas);
        model.addAttribute(PEL_FAV_STRING, perfil.getListaMeGustaPeliculas());
        model.addAttribute(USUARIO_STRING, usuario);
        model.addAttribute(GENEROS_STRING, Generos.values());

        return "catPeliculas"; // apunta a templates/peliculas.html
    }

    @GetMapping("/series")
    public String verSeries(Model model, HttpSession session,
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) Generos genero) {
        Usuario usuario = (Usuario) session.getAttribute(USUARIO_STRING);
        List<Series> series = deustoStreamService.buscarSeriesFiltradas(titulo, genero);
        Perfil perfil = usuario.getPerfiles().get(0);// Obtener el primer perfil basico del usuario

        model.addAttribute(SERIE_STRING, series);
        model.addAttribute(SERIES_FAV_STRING, perfil.getListaMeGustaSeries());
        model.addAttribute(USUARIO_STRING, usuario);
        model.addAttribute(GENEROS_STRING, Generos.values());

        return "catSeries"; // apunta a templates/series.html
    }

    @GetMapping("/pelicula/{id}")
    public String mostrarDetallePelicula(@PathVariable Long id, Model model) {
        Pelicula pelicula = deustoStreamService.getPeliculaById(id)
                .orElseThrow(() -> new RuntimeException("Película no encontrada"));

        model.addAttribute(PEL_STRING, pelicula);
        // ← NUEVO: lista de relacionadas
        model.addAttribute(REL_STRING,
                deustoStreamService.getPeliculasRelacionadas(id));
        model.addAttribute("valoraciones", deustoStreamService.getValoracionesPelicula(id));

        return "detallePelicula";
    }

    // Detalle de película para ADMIN
    @GetMapping("/admin/pelicula/{id}")
    public String mostrarDetallePeliculaAdmin(@PathVariable Long id, Model model) {
        Pelicula pelicula = deustoStreamService.getPeliculaById(id)
                .orElseThrow(() -> new RuntimeException("Película no encontrada"));

        model.addAttribute(PEL_STRING, pelicula);
        model.addAttribute(REL_STRING,
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
        model.addAttribute(REL_STRING,
                deustoStreamService.getSeriesRelacionadas(id));

        model.addAttribute("valoraciones", deustoStreamService.getValoracionesSerie(id));

        return "detalleSerie";
    }

    @GetMapping("/admin/serie/{id}")
    public String mostrarDetalleSerieAdmin(@PathVariable Long id, Model model) {
        Series serie = deustoStreamService.getSeriesById(id)
                .orElseThrow(() -> new RuntimeException("Serie no encontrada"));

        model.addAttribute("serie", serie);
        return "detalleSerieAdmin";
    }

    // Ver lista de peliculas y series favoritas
    @GetMapping("/guardados")
    public String mostrarGuardados(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute(USUARIO_STRING);
        if (usuario != null) {
            Perfil perfil = session.getAttribute(PERFIL_STRING) != null ? (Perfil) session.getAttribute(PERFIL_STRING)
                    : usuario.getPerfiles().get(0);
            model.addAttribute(PEL_FAV_STRING, perfil.getListaMeGustaPeliculas());
            model.addAttribute(SERIES_FAV_STRING, perfil.getListaMeGustaSeries());
        } else {
            model.addAttribute(ERROR_STRING, "Debes iniciar sesión para ver tus películas favoritas.");
        }
        return "guardados"; // Retorna el nombre del archivo HTML en /resources/templates/
    }

    // Entrar a configuración de perfil
    @GetMapping("/perfil")
    public String mostrarPerfil(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute(USUARIO_STRING);
        if (usuario != null) {
            Perfil perfil = session.getAttribute(PERFIL_STRING) != null ? (Perfil) session.getAttribute(PERFIL_STRING)
                    : usuario.getPerfiles().get(0);
            model.addAttribute(USUARIO_STRING, usuario);
            model.addAttribute("perfiles", usuario.getPerfiles());
            model.addAttribute("avatar", perfil.getAvatar());
            return PERFIL_STRING; // Retorna el nombre del archivo HTML en /resources/templates/
        } else {
            model.addAttribute(ERROR_STRING, "Debes iniciar sesión para ver tu perfil.");
            return "redirect:/login";
        }
    }

    @GetMapping("/perfil/{id}")
    public String cambiarPerfil(@PathVariable Long id, HttpSession session) {
        Optional<Perfil> perfilSeleccionado = deustoStreamService.getPerfilById(id);
        if (perfilSeleccionado.isPresent()) {
            session.setAttribute(PERFIL_STRING, perfilSeleccionado.get());
            return "redirect:/catalogo";
        } else {
            return "redirect:/acceso-denegado";
        }
    }

    @PostMapping("/pelicula/{id}/valorar")
    public String valorarPelicula(
            @PathVariable Long id,
            @RequestParam int puntuacion,
            @RequestParam String comentario,
            HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute(USUARIO_STRING);
        Perfil perfil = (Perfil) session.getAttribute(PERFIL_STRING);
        if (usuario != null) {
            deustoStreamService.valorarPelicula(id, perfil, puntuacion, comentario);
            return "redirect:/pelicula/" + id;
        } else {
            return "redirect:/login"; // Si no hay sesión, redirigir a login
        }
    }

    @PostMapping("/serie/{id}/valorar")
    public String valorarSerie(
            @PathVariable Long id,
            @RequestParam int puntuacion,
            @RequestParam String comentario,
            HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute(USUARIO_STRING);
        Perfil perfil = (Perfil) session.getAttribute(PERFIL_STRING);
        if (usuario != null) {
            deustoStreamService.valorarSerie(id, perfil, puntuacion, comentario);
            return "redirect:/serie/" + id;
        } else {
            return "redirect:/login"; // Si no hay sesión, redirigir a login
        }
    }


    // Detalle de usuario 
    @GetMapping("/admin/usuario/{id}")
    public String mostrarDetalleUsuario(@PathVariable Long id, Model model) {
        Usuario usuario = deustoStreamService.getUsuarioById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + id));
        model.addAttribute("usuario", usuario);
        return "detalleUsuarioAdmin";   // Thymeleaf renderiza detalleUsuario.html
    }

    @GetMapping("/usuario/{id}")
    public String mostrarDetalleUsuario(@PathVariable Long id, Model model, HttpSession session) {
        Usuario usuario = deustoStreamService.getUsuarioById(id)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + id));
        model.addAttribute("usuario", usuario);
        return "detalleUsuario";
}

}