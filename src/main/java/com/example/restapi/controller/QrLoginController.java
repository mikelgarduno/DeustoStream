package com.example.restapi.controller;

import com.example.restapi.model.Usuario;
import com.example.restapi.repository.UsuarioRepository;
import com.example.restapi.service.QrLoginService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


import java.util.UUID;

@Controller
public class QrLoginController {

   
    private final QrLoginService qrLoginService;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public QrLoginController(QrLoginService qrLoginService, UsuarioRepository usuarioRepository) {
        this.qrLoginService = qrLoginService;
        this.usuarioRepository = usuarioRepository;
    }

    // Vista del formulario de login QR cuando se escanea el código
    @GetMapping("/qr-login-form")
    public String mostrarFormularioLogin(@RequestParam("token") String token, Model model) {
        if (!StringUtils.hasText(token)) {
            model.addAttribute("error", "Token inválido");
            return "qr-login-form";
        }
        model.addAttribute("token", token);
        return "qr-login-form";
    }

    // Procesa el login desde móvil
    @PostMapping("/qr-login-submit")
    public String procesarLoginQr(@RequestParam("token") String token,
                                  @RequestParam("correo") String correo,
                                  @RequestParam("contrasenya") String contrasenya,
                                  Model model) {
        Usuario usuario = usuarioRepository.findByCorreoAndContrasenya(correo, contrasenya);

        if (usuario == null) {
            model.addAttribute("error", "Credenciales inválidas");
            model.addAttribute("token", token);
            return "qr-login-form";
        }

        qrLoginService.marcarAutorizado(token);
        model.addAttribute("mensaje", "Inicio de sesión autorizado correctamente.");
        return "qr-login-form";
    }

    // Genera y muestra el código QR (accedido desde /qr-login)
    @GetMapping(value = "/qr-login", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseBody
    public byte[] mostrarCodigoQr(HttpSession session) throws Exception {
        String token = (String) session.getAttribute("qr-token");

        if (token == null) {
            token = UUID.randomUUID().toString();
            session.setAttribute("qr-token", token);
            qrLoginService.registrarToken(token);
        }

        String url = "http://localhost:8080/qr-login-form?token=" + token;
        return qrLoginService.generarCodigoQR(url);
    }
}
