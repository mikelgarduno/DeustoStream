package com.example.restapi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;

import com.example.restapi.model.Usuario;
import com.example.restapi.service.DeustoStreamService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CatalogoController { 

     // Logger para registrar mensajes
    private static final Logger logger = LoggerFactory.getLogger(CatalogoController.class);

    
    @Autowired
    private DeustoStreamService deustoStreamService; // Asegúrate de que este servicio esté definido y configurado correctamente

    @GetMapping("/peliculas")
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
        return "redirect:/"; 
    }

    @GetMapping("/series")
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
        return "redirect:/"; 
    }

    @GetMapping("/usuarios")
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
        return "redirect:/"; 
    }
}
