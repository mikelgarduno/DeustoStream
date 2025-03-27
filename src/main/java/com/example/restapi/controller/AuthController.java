package com.example.restapi.controller;
// definicion de los endpoints para logi y registro

import com.example.restapi.model.Usuario;
import com.example.restapi.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/login")
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
                        session.setAttribute("usuario", usuario); // guarda sesion 
                        return "redirect:" + authService.obtenerRedireccion(usuario);
                    })
                    .orElseGet(() -> {
                        model.addAttribute("error", "Correo o contraseña incorrectas");
                        return "login";
                    });
    }

    @GetMapping("/registro")
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
        }

    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
    
}
