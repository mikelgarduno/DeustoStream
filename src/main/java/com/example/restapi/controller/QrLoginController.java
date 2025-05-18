package com.example.restapi.controller;

import com.example.restapi.model.Usuario;
import com.example.restapi.repository.UsuarioRepository;
import com.example.restapi.service.QrLoginService;

import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
public class QrLoginController {

    private static final Logger logger = LoggerFactory.getLogger(QrLoginController.class);

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
        logger.info("Mostrar formulario de login QR para token: {}", token);
        if (!StringUtils.hasText(token)) {
            logger.warn("Token inválido recibido en /qr-login-form");
            model.addAttribute("error", "Token inválido");
            return "qr-login-form";
        }
        model.addAttribute("token", token);
        return "qr-login-form";
    }

    @PostMapping("/qr-login-submit")
    public String procesarLoginQr(@RequestParam("token") String token,
            @RequestParam("correo") String correo,
            @RequestParam("contrasenya") String contrasenya,
            Model model) {
        logger.info("Procesando login QR para token: {}, correo: {}", token, correo);
        Usuario usuario = usuarioRepository.findByCorreoAndContrasenya(correo, contrasenya);
        if (usuario == null) {
            logger.warn("Credenciales inválidas para correo: {}", correo);
            model.addAttribute("error", "Credenciales inválidas");
            model.addAttribute("token", token);
            return "qr-login-form";
        }

        // Asociar el usuario con el token para que la sesión del PC pueda recuperarlo
        qrLoginService.marcarAutorizado(token, usuario);
        logger.info("Login QR autorizado para usuario: {}", correo);
        model.addAttribute("mensaje", "Inicio de sesión autorizado correctamente. Puedes cerrar esta ventana.");
        return "qr-login-success"; // Nueva página de éxito
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

        String url = "https://fz75p9zf-8080.uks1.devtunnels.ms/qr-login-form?token=" + token;
        return qrLoginService.generarCodigoQR(url);
    }
}
